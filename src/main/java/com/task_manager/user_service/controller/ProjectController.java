package com.task_manager.user_service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.task_manager.user_service.service.OrganizationService;
import com.task_manager.user_service.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("v1/project/create")
    public ResponseEntity<?> createProject(@RequestBody JsonNode project){
        return ResponseEntity.ok(projectService.createProject(project));
    }

    @DeleteMapping("v1/project/delete/project/{projId}/owner/{ownerId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projId,
                                           @PathVariable Long ownerId){
        projectService.deleteProject(projId, ownerId);
        return ResponseEntity.ok().build();
    }




}
