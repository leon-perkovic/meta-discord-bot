package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Role Validator for passed arguments
 * <p>
 * Created by Leon on 18/03/2018
 */
@Component
public class RoleValidator extends GlobalValidator {

    @Autowired
    RoleService roleService;


    public boolean validateIfUniqueRole(String roleName, String shortName) {

        if(roleService.findByRoleName(roleName) == null &&
                roleService.findByShortName(shortName) == null) {
            return true;
        }
        return false;
    }

    public boolean validateIfUniqueRoleUpdate(Long id, String roleName, String shortName) {
        Role role = roleService.findById(id);

        if(roleService.findByRoleName(roleName) != null && !role.getRoleName().equalsIgnoreCase(roleName)) {
            return false;
        }
        if(roleService.findByShortName(shortName) != null && !role.getShortName().equalsIgnoreCase(shortName)) {
            return false;
        }
        return true;
    }

}
