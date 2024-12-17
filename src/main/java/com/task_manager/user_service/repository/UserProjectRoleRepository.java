package com.task_manager.user_service.repository;

import com.task_manager.user_service.entity.Role;
import com.task_manager.user_service.entity.UserProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProjectRoleRepository extends JpaRepository<UserProjectRole, Long> {
    @Query("SELECT upr.role.name FROM UserProjectRole upr WHERE upr.userId = :userId AND upr.projectId = :projectId")
    Optional<String> findRoleByUserIdAndProjectId(Long userId, Long projectId);

    Optional<UserProjectRole> findByUserIdAndProjectId(Long userId, Long projectId);

    boolean existsByUserIdAndProjectId(Long userId,Long projectId);

    @Query("SELECT upr.userId FROM UserProjectRole upr where upr.projectId = :projId")
    List<Long> findAllUsersByProjectId(Long projId);

}
