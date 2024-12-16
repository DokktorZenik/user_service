package com.task_manager.user_service.controller;

import com.task_manager.user_service.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/invitations")
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping("/organization/accept")
    public String acceptInvitation(@RequestParam String token) {
        return invitationService.acceptOrganizationInvitation(token);
    }

    @PostMapping("/organization/send")
    public String sendInvitation(@RequestParam String email, @RequestParam Long organizationId) {
        invitationService.createInvitation(email, organizationId);
        return "Invitation to Organization sent to " + email;
    }

    @PostMapping("/project/send")
    public String sendProjectInvitation(@RequestParam String email, @RequestParam Long organizationId, Long projectId){
        invitationService.createProjectInvitation(email, organizationId, projectId);
        return "Invitation to Project sent to " + email;
    }

    @PostMapping("/project/accept")
    public String acceptProjectInvitation(@RequestParam String token) {
        return invitationService.acceptProjectInvitation(token);
    }
}
