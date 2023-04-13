package com.krekerok.blogapp.service.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.krekerok.blogapp.configuration.jwt.JwtUtils;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.RedisUser;
import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;
import com.krekerok.blogapp.repository.AppUserRepository;
import com.krekerok.blogapp.service.RoleService;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AppUserServiceImpl appUserService;



    @Test
    void testCreateUser() {
        Role userRole = getUserRole();
        RedisUser redisUser = getRedisUser();
        AppUser appUser = getAppUser(userRole);

        doReturn(userRole).when(roleService).createRoleIfNotExist(RoleName.USER);
        appUserService.createUser(redisUser);

        verify(appUserRepository).save(appUser);
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

    @Test
    void testSaveAppUser(){
        AppUser appUser = getAppUser(getUserRole());
        AppUser savedUser = getAppUser(getUserRole());
        savedUser.setUserId(1L);

        doReturn(savedUser).when(appUserRepository).save(appUser);

        AppUser actualResult = appUserService.saveAppUser(appUser);

        assertEquals(savedUser, actualResult);
        verify(appUserRepository, times(1)).save(appUser);
    }


    @Test
    void testDeleteAppUserById_WhenIdExists(){
        long id = 1L;
        AppUser appUser = new AppUser();
        appUser.setUserId(id);

        doReturn(Optional.of(appUser)).when(appUserRepository).findById(id);
        doNothing().when(appUserRepository).delete(appUser);


        boolean result = appUserService.deleteAppUserById(id);
        assertTrue(result);
        verify(appUserRepository, times(1)).findById(id);
        verify(appUserRepository, times(1)).delete(appUser);
        verify(appUserRepository, times(1)).flush();
    }


    @Test
    void testDeleteAppUserById_WhenIdDoesNotExists(){

        long id = 1L;

        doReturn(Optional.empty()).when(appUserRepository).findById(id);

        boolean result = appUserService.deleteAppUserById(id);

        assertFalse(result);
        verify(appUserRepository, times(1)).findById(id);
        verify(appUserRepository, times(0)).delete(any(AppUser.class));
        verify(appUserRepository, times(0)).flush();
    }

    @Test
    void testDeleteLinkToTheBlog() {
        Blog blog = mock(Blog.class);
        AppUser appUser = mock(AppUser.class);

        doReturn(appUser).when(appUserRepository).findAppUserByBlog(blog);

        appUserService.deleteLinkToTheBlog(blog);

        verify(appUser).setBlog(null);
        verify(appUserRepository).save(appUser);
    }

}