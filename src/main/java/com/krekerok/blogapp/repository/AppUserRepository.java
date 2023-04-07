package com.krekerok.blogapp.repository;

import com.krekerok.blogapp.entity.AppUser;
import com.krekerok.blogapp.entity.Blog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    AppUser findAppUserByBlog(Blog blog);
}
