package com.krekerok.blogapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;
import com.krekerok.blogapp.repository.RoleRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void testCreateRoleIfNotExist_shouldReturnExistingRole() {

        RoleName roleName = RoleName.USER;
        Role role = Role.builder().roleName(roleName).build();

        Mockito.when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(role));

        Role result = roleService.createRoleIfNotExist(roleName);

        assertEquals(role, result);
        Mockito.verify(roleRepository, Mockito.times(1)).findByRoleName(roleName);
        Mockito.verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void testCreateRoleIfNotExist_shouldCreateNewRole() {

        RoleName roleName = RoleName.ADMIN;

        Mockito.when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());

        Role savedRole = Role.builder().roleName(roleName).build();
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(savedRole);

        Role result = roleService.createRoleIfNotExist(roleName);

        assertEquals(savedRole, result);
        Mockito.verify(roleRepository, Mockito.times(1)).findByRoleName(roleName);
        Mockito.verify(roleRepository, Mockito.times(1)).save(Mockito.any(Role.class));
        Mockito.verifyNoMoreInteractions(roleRepository);
    }
}