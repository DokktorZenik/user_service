package com.task_manager.user_service.service;

import com.task_manager.user_service.entity.Role;
import com.task_manager.user_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void initializeRoles(){
        if(!roleRepository.existsByName("ROLE_USER")){
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }
        if(!roleRepository.existsByName("ROLE_ADMIN")){
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }
}
