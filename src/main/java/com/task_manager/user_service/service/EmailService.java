package com.task_manager.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    public void sendInvitationOrganizationEmail(String to, String token) {
        String url = "http://localhost:8083/v1/invitations/organization/accept?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Invitation to Join Organization");
        message.setText("You have been invited to join an organization. Click the link to accept the invitation: " + url);

        javaMailSender.send(message);
    }

    public void sendInvitationProjectEmail(String to, String token) {
        String url = "http://localhost:8083/v1/invitations/project/accept?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Invitation to Join Project");
        message.setText("You have been invited to join an project. Click the link to accept the invitation: " + url);

        javaMailSender.send(message);
    }
}
