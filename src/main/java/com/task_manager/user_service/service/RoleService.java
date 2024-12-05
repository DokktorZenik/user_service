package com.task_manager.user_service.service;

import com.task_manager.user_service.entity.Role;
import com.task_manager.user_service.repository.RoleRepository;
import com.task_manager.user_service.repository.UserOrganizationRoleRepository;
import com.task_manager.user_service.repository.UserProjectRoleRepository;
import com.task_manager.user_service.repository.UserRepository;
import com.task_manager.user_service.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserOrganizationRoleRepository userOrganizationRoleRepository;
    private final UserProjectRoleRepository userProjectRoleRepository;
    private final UserRepository userRepository;

    public void initializeRoles(){
        if(!roleRepository.existsByName("ROLE_USER")){
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        if(!roleRepository.existsByName("ROLE_ADMIN")){
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }

    public Role getRoleByName(String name){
        return roleRepository.findByName(name).orElseThrow();
    }

    public ResponseEntity<?> getRole(String authHeader, Long organizationId, Long projectId, HttpServletResponse response){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid or missing token");
        }

        String token = authHeader.substring(7);
        String username = jwtTokenUtils.getUsername(token);

        Long userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String orgRole = getUserRoleInOrganization(userId, organizationId);
        String projectRole = getUserRoleInProject(userId, projectId);

        response.setHeader("Org-Role", orgRole);
        response.setHeader("Project-Role", projectRole);

        return ResponseEntity.ok(Map.of(
                "organizationRole", orgRole,
                "projectRole", projectRole
        ));
    }

    public String getUserRoleInOrganization(Long userId, Long organizationId){
        if (organizationId == null) {
            return "No organization specified";
        }

        return userOrganizationRoleRepository.findRoleByUserIdAndOrganizationId(userId, organizationId)
                .orElse("No role found");
    }

    public String getUserRoleInProject(Long userId, Long projectId) {
        if (projectId == null) {
            return "No project specified";
        }
        return userProjectRoleRepository.findRoleByUserIdAndProjectId(userId, projectId)
                .orElse("No role found");
    }
}
