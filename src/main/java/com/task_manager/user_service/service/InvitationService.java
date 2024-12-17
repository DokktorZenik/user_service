package com.task_manager.user_service.service;

import com.task_manager.user_service.entity.Invitation;
import com.task_manager.user_service.entity.User;
import com.task_manager.user_service.entity.UserOrganizationRole;
import com.task_manager.user_service.entity.UserProjectRole;
import com.task_manager.user_service.repository.InvitationRepository;
import com.task_manager.user_service.repository.UserOrganizationRoleRepository;
import com.task_manager.user_service.repository.UserProjectRoleRepository;
import com.task_manager.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final UserOrganizationRoleRepository userOrganizationRoleRepository;
    private final UserProjectRoleRepository userProjectRoleRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RoleService roleService;

    public Invitation createInvitation(String email, Long organizationId) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long currentUserId = userRepository.findIdByUsername(username).orElseThrow();


        Long invitedUserId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invited user does not exist."));

        boolean isInvitedUserInOrganization = userOrganizationRoleRepository
                .existsByUserIdAndOrganizationId(invitedUserId, organizationId);

        if (isInvitedUserInOrganization) {
            throw new RuntimeException("The invited user is already a member of this organization.");
        }


        UserOrganizationRole userRole = userOrganizationRoleRepository
                .findByUserIdAndOrganizationId(currentUserId, organizationId)
                .orElseThrow(() -> new RuntimeException("You do not have permission to invite users to this organization."));

        if (!userRole.getRole().getName().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Only admins can invite users to this organization.");
        }

        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setOrganizationId(organizationId);
        invitation.setToken(generateToken());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7));

        Invitation savedInvitation = invitationRepository.save(invitation);

        emailService.sendInvitationOrganizationEmail(email, savedInvitation.getToken());

        return savedInvitation;
    }

    public Invitation createProjectInvitation(String email, Long organizationId, Long projectId) {
        Long currentUserId = getAuthenticatedUserId();

        UserOrganizationRole currentUserOrgRole = userOrganizationRoleRepository
                .findByUserIdAndOrganizationId(currentUserId, organizationId)
                .orElseThrow(() -> new RuntimeException("You are not a member of this organization."));

        if (!currentUserOrgRole.getRole().getName().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Only admins can invite users to a project.");
        }

        Long invitedUserId = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invited user does not exist."));

        boolean isInvitedUserInOrganization = userOrganizationRoleRepository
                .existsByUserIdAndOrganizationId(invitedUserId, organizationId);

        boolean isInvitedUserInProject = userProjectRoleRepository
                .existsByUserIdAndProjectId(invitedUserId, projectId);

        if (!isInvitedUserInOrganization) {
            throw new RuntimeException("The invited user is not a member of this organization.");
        }

        if (isInvitedUserInProject) {
            throw new RuntimeException("The invited user is already a member of this project.");
        }

        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setOrganizationId(organizationId);
        invitation.setProjectId(projectId);
        invitation.setToken(generateToken());
        invitation.setExpiresAt(LocalDateTime.now().plusDays(7));

        Invitation savedInvitation = invitationRepository.save(invitation);

        emailService.sendInvitationProjectEmail(email, savedInvitation.getToken());

        return savedInvitation;
    }

    public String acceptOrganizationInvitation(String token) {
        Invitation invitation = getInvitationByToken(token);

        if (invitation.isAccepted()) {
            return "Invitation already accepted!";
        }

        if (isInvitationExpired(invitation)) {
            return "Invitation has expired!";
        }

        Long authenticatedUserId = getAuthenticatedUserId();
        UserOrganizationRole role = new UserOrganizationRole();
        role.setUserId(authenticatedUserId);
        role.setOrganizationId(invitation.getOrganizationId());
        role.setRole(roleService.getRoleByName("ROLE_USER"));

        userOrganizationRoleRepository.save(role);
        invitation.setAccepted(true);
        invitationRepository.save(invitation);

        return "Invitation accepted. Welcome to the organization!";
    }

    public String acceptProjectInvitation(String token) {
        Invitation invitation = getInvitationByToken(token);

        if (invitation.isAccepted()) {
            return "Invitation already accepted!";
        }

        if (isInvitationExpired(invitation)) {
            return "Invitation has expired!";
        }

        Long authenticatedUserId = getAuthenticatedUserId();
        if (!userOrganizationRoleRepository.existsByUserIdAndOrganizationId(authenticatedUserId, invitation.getOrganizationId())) {
            throw new RuntimeException("You must be a member of the organization to accept this project invitation.");
        }

        UserProjectRole role = new UserProjectRole();
        role.setUserId(authenticatedUserId);
        role.setProjectId(invitation.getProjectId());
        role.setRole(roleService.getRoleByName("ROLE_USER"));

        userProjectRoleRepository.save(role);
        invitation.setAccepted(true);
        invitationRepository.save(invitation);

        return "Invitation accepted. Welcome to the project!";
    }


    public Invitation getInvitationByToken(String token) {
        return invitationRepository.findByToken(token).orElseThrow(() ->
                new IllegalArgumentException("Invalid invitation token"));
    }

    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(uuid.toString().getBytes());
    }

    public Long getAuthenticatedUserId() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findIdByUsername(userName).orElseThrow();
        return userId;
    }

    public boolean isInvitationExpired(Invitation invitation) {
        return invitation.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
