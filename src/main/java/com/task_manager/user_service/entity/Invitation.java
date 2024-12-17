package com.task_manager.user_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitations")
@Data
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "organization_id")
    private Long organizationId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "token")
    private String token;

    private LocalDateTime expiresAt;

    private boolean accepted = false;
}
