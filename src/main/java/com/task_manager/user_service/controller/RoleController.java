package com.task_manager.user_service.controller;

import com.task_manager.user_service.service.RoleService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/getRole")
    public ResponseEntity<?> getRole(@RequestHeader("Authorization") String auth,
                                     @RequestParam(name = "organizationId", required = false) Long organizationId,
                                     @RequestParam(name = "projectId", required = false) Long projectId,
                                     HttpServletResponse response){
        return roleService.getRole(auth,organizationId, projectId, response);
    }
}
