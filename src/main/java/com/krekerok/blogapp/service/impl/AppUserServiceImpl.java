package com.krekerok.blogapp.service.impl;

import com.krekerok.blogapp.configuration.jwt.JwtUtils;
import com.krekerok.blogapp.configuration.user_details.UserDetailsImpl;
import com.krekerok.blogapp.dto.requests.AppUserLoginRequestDto;
import com.krekerok.blogapp.dto.responses.AppUserLoginResponseDto;
import com.krekerok.blogapp.dto.responses.AppUserResponseDto;
import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Blog;
import com.krekerok.blogapp.entity.RedisUser;
import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;
import com.krekerok.blogapp.exception.FieldExistsException;
import com.krekerok.blogapp.exception.UserNotFoundException;
import com.krekerok.blogapp.mapper.AppUserMapper;
import com.krekerok.blogapp.repository.AppUserRepository;
import com.krekerok.blogapp.service.AppUserService;
import com.krekerok.blogapp.service.RoleService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    public AppUserLoginResponseDto loginUser(AppUserLoginRequestDto appUserLoginRequestDto) {
        Authentication authentication = getAuthentication(appUserLoginRequestDto);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        AppUser appUser = appUserRepository
            .findById(userDetails.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Long blogId = appUser.getBlog() != null ? appUser.getBlog().getBlogId() : null;

        return AppUserLoginResponseDto.builder()
            .token(jwt)
            .userId(userDetails.getId())
            .username(userDetails.getUsername())
            .email(userDetails.getEmail())
            .blogId(blogId)
            .build();
    }



    private Authentication getAuthentication(AppUserLoginRequestDto appUserLoginRequestDto) {
        return authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(appUserLoginRequestDto.getUsername(),
                appUserLoginRequestDto.getPassword()));
    }

    @Override
    public void createUser(RedisUser redisUser) {
        appUserRepository.save(getUserFromRedisUser(redisUser));
    }



    private AppUser getUserFromRedisUser(RedisUser redisUser) {
        Role role = roleService.createRoleIfNotExist(RoleName.USER);

        AppUser appUser = AppUserMapper.INSTANCE.toAppUser(redisUser);
        appUser.setRoles(Set.of(role));
        return appUser;

//        return AppUser.builder()
//            .username(redisUser.getUsername())
//            .password(redisUser.getPassword())
//            .email(redisUser.getEmail())
//            .createdAt(redisUser.getCreatedAt())
//            .roles(Set.of(role))
//            .build();
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

    @Override
    public List<AppUserResponseDto> findAll() {
        List<AppUser> users = appUserRepository.findAll();

        if (!users.isEmpty()) {
            return users.stream()
                .map(user -> AppUserMapper.INSTANCE.toAppUserResponseDto(user))
                .collect(Collectors.toList());
        } else {
            throw new UserNotFoundException("There are no users in the database");
        }
    }

    @Override
    public boolean deleteAppUserById(long id) {
        return appUserRepository.findById(id)
            .map(entity -> {
                appUserRepository.delete(entity);
                appUserRepository.flush();
                return true;
            })
            .orElse(false);
    }

    @Override
    public boolean checkingForDataCompliance(Long blogId, String jwt) {
        AppUser appUser = appUserRepository.findByUsername
            (jwtUtils.getUserNameFromJwtToken(jwt)).orElseThrow(() -> new UserNotFoundException("User not found"));
        return appUser.getBlog().getBlogId().equals(blogId);
    }

    @Override
    public void deleteLinkToTheBlog(Blog blog) {
        AppUser appUser = appUserRepository.findAppUserByBlog(blog);
        appUser.setBlog(null);
        appUserRepository.save(appUser);
    }
}
