package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.Role;

import java.util.List;

/**
 * Interface for Role service
 *
 * Created by Leon on 18/03/2018
 */
public interface RoleService{

    List<Role> findAll();

    Role findById(Long id);

    Role findByRoleName(String roleName);

    Role findByShortName(String shortName);

    Role saveRole(Role role);

    Integer removeById(Long id);

    Integer removeByRoleName(String roleName);

    Integer removeByShortName(String shortName);

}
