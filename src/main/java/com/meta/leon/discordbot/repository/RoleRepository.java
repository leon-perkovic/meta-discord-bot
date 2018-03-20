package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Role repository - used to manage Role entries in a database
 *
 * @author Leon, created on 18/03/2018
 */
public interface RoleRepository extends JpaRepository<Role, Long>{

    List<Role> findAllByOrderByRoleName();

    Role findById(Long id);

    Role findByRoleNameIgnoreCase(String roleName);

    Role findByShortNameIgnoreCase(String shortName);

    Integer deleteById(Long id);

    Integer deleteByRoleNameIgnoreCase(String roleName);

    Integer deleteByShortNameIgnoreCase(String shortName);

}
