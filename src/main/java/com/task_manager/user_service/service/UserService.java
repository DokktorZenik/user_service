package com.task_manager.user_service.service;

import com.task_manager.user_service.dto.RegistrationUserDto;
import com.task_manager.user_service.entity.User;
import com.task_manager.user_service.repository.RoleRepository;
import com.task_manager.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User '%s' not found",username)));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList())
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto){
        User user = new User();
        user.setEmail(registrationUserDto.getEmail());
        user.setUsername(registrationUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(Set.of(roleRepository.findByName("ROLE_USER").get()));
        return userRepository.save(user);
    }

    public String getEncodedPassword(String username) {
        User user = findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getPassword();
    }
}
