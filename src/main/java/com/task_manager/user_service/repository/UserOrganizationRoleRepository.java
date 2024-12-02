package com.task_manager.user_service.repository;


import com.task_manager.user_service.entity.UserOrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserOrganizationRoleRepository extends JpaRepository<UserOrganizationRole, Long> {
    @Query("SELECT uor.role.name FROM UserOrganizationRole uor WHERE uor.userId = :userId AND uor.organizationId = :organizationId")
    Optional<String> findRoleByUserIdAndOrganizationId(Long userId, Long organizationId);
    Optional<UserOrganizationRole> findByUserIdAndOrganizationId(Long userId, Long organizationId);
}
