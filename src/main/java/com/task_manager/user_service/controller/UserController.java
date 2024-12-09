package com.task_manager.user_service.controller;

import com.task_manager.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/users/")
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @GetMapping("{projId}")
    public ResponseEntity<?> getProjectUsers(@PathVariable("projId") Long projId) {
        return ResponseEntity.ok(userService.getUsersByProjectId(projId));
    }

}
