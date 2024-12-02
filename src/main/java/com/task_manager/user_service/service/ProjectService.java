package com.task_manager.user_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.task_manager.user_service.entity.UserOrganizationRole;
import com.task_manager.user_service.entity.UserProjectRole;
import com.task_manager.user_service.repository.UserProjectRoleRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final UserProjectRoleRepository userProjectRoleRepository;
    private final RoleService roleService;

    public UserProjectRole createProject(JsonNode project){
        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setUserId(project.get("ownerId").asLong());
        userProjectRole.setProjectId(project.get("projectId").asLong());
        userProjectRole.setRole(roleService.getRoleByName("ROLE_ADMIN"));

        return userProjectRoleRepository.save(userProjectRole);
    }

    public void deleteProject(Long projectId, Long ownerId){
        UserProjectRole userProjectRole = userProjectRoleRepository
                .findByUserIdAndProjectId(ownerId, projectId).orElseThrow();

        userProjectRoleRepository.delete(userProjectRole);
    }
}
