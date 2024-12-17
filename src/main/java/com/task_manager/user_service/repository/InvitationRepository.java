package com.task_manager.user_service.repository;

import com.task_manager.user_service.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByToken(String token);
    Optional<Invitation> findByEmailAndOrganizationId(String email, Long organizationId);
}
