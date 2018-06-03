package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.repository.RoleRepository;
import com.meta.leon.discordbot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Role service - uses Role repository to manage Role entries in a database
 * <p>
 * Created by Leon on 18/03/2018
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;


    @Override
    public List<Role> findAll() {
        return roleRepository.findAllByOrderByRoleName();
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleNameIgnoreCase(roleName);
    }

    @Override
    public Role findByShortName(String shortName) {
        return roleRepository.findByShortNameIgnoreCase(shortName);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Integer removeById(Long id) {
        return roleRepository.deleteById(id);
    }

    @Override
    public Integer removeByRoleName(String roleName) {
        return roleRepository.deleteByRoleNameIgnoreCase(roleName);
    }

    @Override
    public Integer removeByShortName(String shortName) {
        return roleRepository.deleteByShortNameIgnoreCase(shortName);
    }

}
