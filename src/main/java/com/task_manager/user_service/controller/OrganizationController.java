package com.task_manager.user_service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.task_manager.user_service.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("v1/organization/create")
    public ResponseEntity<?> createOrganization(@RequestBody JsonNode organization){
        return ResponseEntity.ok(organizationService.createOrganization(organization));
    }

    @DeleteMapping("v1/organization/delete/organization/{orgId}/owner/{ownerId}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long orgId,
                                                @PathVariable Long ownerId){
        organizationService.deleteOrganization(orgId,ownerId);
        return ResponseEntity.ok().build();
    }
}
