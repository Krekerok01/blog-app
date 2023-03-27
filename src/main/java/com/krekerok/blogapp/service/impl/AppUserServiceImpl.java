package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.configuration.jwt.JwtUtils;
import com.krekerok.blogapp.configuration.user_details.UserDetailsImpl;
import com.krekerok.blogapp.dto.requests.AppUserLoginDto;
import com.krekerok.blogapp.dto.responses.AppUserLoginReadDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.RedisUser;
import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;
import com.krekerok.blogapp.exception.FieldExistsException;
import com.krekerok.blogapp.exception.UserNotFoundException;
import com.krekerok.blogapp.repository.AppUserRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.RoleService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public AppUserLoginReadDto loginUser(AppUserLoginDto appUserLoginDto) {
        Authentication authentication = getAuthentication(appUserLoginDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        return AppUserLoginReadDto.builder()
            .token(jwt)
            .userId(userDetails.getId())
            .username(userDetails.getUsername())
            .email(userDetails.getEmail())
            .build();
    }



    private Authentication getAuthentication(AppUserLoginDto appUserLoginDto) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(appUserLoginDto.getUsername(),
                appUserLoginDto.getPassword()));
    }

    @Override
    public void createUser(RedisUser redisUser) {
        appUserRepository.save(getUserFromRedisUser(redisUser));
    }



    private AppUser getUserFromRedisUser(RedisUser redisUser) {
        Role role = roleService.createRoleIfNotExist(RoleName.USER);
        return AppUser.builder()
            .username(redisUser.getUsername())
            .password(redisUser.getPassword())
            .email(redisUser.getEmail())
            .createdAt(redisUser.getCreatedAt())
            .roles(Set.of(role))
            .build();
    }

    @Override
    public void checkingForExistenceInTheDatabase(String username, String email) {
        if (appUserRepository.existsByUsername(username)) {
            throw new FieldExistsException("Error: Username already exists");
        } else if (appUserRepository.existsByEmail(email)) {
            throw new FieldExistsException("Error: Email already exists");
        }
    }

    @Override
    public AppUser findAppUserByAppUserId(Long appUserId) {
        return appUserRepository.findById(appUserId).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public AppUser saveBlogToTheAppUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }
}
