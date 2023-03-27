package com.krekerok.blogapp.repository;

import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleName(RoleName name);
}
