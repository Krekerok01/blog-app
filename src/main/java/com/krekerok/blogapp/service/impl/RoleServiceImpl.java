package com.krekerok.blogapp.service.impl;


import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;
import com.krekerok.blogapp.repository.RoleRepository;
import com.krekerok.blogapp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Role createRoleIfNotExist(RoleName roleName) {
        return roleRepository.findByRoleName(roleName).orElseGet(
            () -> roleRepository.save(
                Role.builder()
                    .roleName(roleName)
                    .build()));
    }

}
