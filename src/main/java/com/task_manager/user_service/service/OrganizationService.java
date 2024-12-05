package com.task_manager.user_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.task_manager.user_service.entity.UserOrganizationRole;
import com.task_manager.user_service.repository.RoleRepository;
import com.task_manager.user_service.repository.UserOrganizationRoleRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final UserOrganizationRoleRepository userOrganizationRoleRepository;
    private final RoleService roleService;

    public UserOrganizationRole createOrganization(JsonNode org){
        System.out.println(org.toString());
        System.out.println(org.get("ownerId") + " owner id");

        UserOrganizationRole userOrganizationRole = new UserOrganizationRole();
        userOrganizationRole.setUserId(org.get("ownerId").asLong());
        userOrganizationRole.setOrganizationId(org.get("organizationId").asLong());
        userOrganizationRole.setRole(roleService.getRoleByName("ROLE_ADMIN"));

        return userOrganizationRoleRepository.save(userOrganizationRole);
    }

    public void deleteOrganization(Long orgId, Long ownerId){
        UserOrganizationRole userOrganizationRole = userOrganizationRoleRepository
                .findByUserIdAndOrganizationId(ownerId,orgId).orElseThrow();

        userOrganizationRoleRepository.delete(userOrganizationRole);
    }
}
