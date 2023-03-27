package com.krekerok.blogapp.service;

import com.krekerok.blogapp.entity.Role;
import com.krekerok.blogapp.entity.RoleName;

public interface RoleService {

    Role createRoleIfNotExist(RoleName roleName);
}
