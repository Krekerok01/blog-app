package com.krekerok.blogapp.service.impl;


import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.RedisUser;
import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;
import com.krekerok.blogapp.repository.AppUserRepository;
import com.krekerok.blogapp.service.RoleService;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private AppUserServiceImpl appUserService;



    @Test
    void createUser() {

        Role userRole = getUserRole();
        RedisUser redisUser = getRedisUser();
        AppUser appUser = getAppUser(userRole);

        Mockito.doReturn(userRole).when(roleService).createRoleIfNotExist(RoleName.USER);
        appUserService.createUser(redisUser);

        Mockito.verify(appUserRepository).save(appUser);
    }

    private AppUser getAppUser(Role userRole) {
        return AppUser.builder()
            .username("username")
            .password("passwordpassword")
            .email("test@gmail.com")
            .createdAt(Instant.now())
            .roles(Set.of(userRole))
            .build();
    }

    private Role getUserRole() {
        return Role.builder()
            .roleId(1L)
            .roleName(RoleName.USER)
            .build();
    }

    private RedisUser getRedisUser() {
        return RedisUser.builder()
            .username("username")
            .password("passwordpassword")
            .email("test@gmail.com")
            .activationCode(UUID.randomUUID().toString())
            .timeOfSendingVerificationLink(Instant.now())
            .createdAt(Instant.now())
            .build();
    }

}