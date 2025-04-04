package com.springboot.service.Role;


import com.springboot.entities.Role;
import com.springboot.repository.AuthRepository.RoleRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RoleServiceImpl {

    private  final RoleRepository roleRepository;

    @PostConstruct
    public  void initRoles(){
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ADMIN");
    }

    private void createRoleIfNotExists(String roleName) {
        if(roleRepository.findByName(roleName).isEmpty()){
            Role role = Role.builder()
                    .name(roleName)
                    .build();
            roleRepository.save(role);
        }
    }

}
