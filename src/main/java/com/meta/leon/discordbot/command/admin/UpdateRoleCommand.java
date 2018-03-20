package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !updateRole <id> <role_name> <short_name>
 * Command for updating role entries in a database
 *
 * @author Leon, created on 19/03/2018
 */
@Component
public class UpdateRoleCommand extends AbstractCommand{

    private Long id;
    private String roleName;
    private String shortName;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public UpdateRoleCommand(){
        super("updaterole", "Update role info in a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 3)){
            return new ResponseForm(CommandResponses.UPDATE_ROLE_INVALID_ARGUMENTS);
        }
        if(roleValidator.validateIfNumeric(arguments.get(0))){
            this.id = Long.valueOf(arguments.get(0));
            this.roleName = arguments.get(1);
            this.shortName = arguments.get(2);

            Role role = roleService.findById(id);
            if(role == null){
                return new ResponseForm(CommandResponses.ROLE_NOT_FOUND);
            }

            if(!roleValidator.validateIfUniqueRoleUpdate(id, roleName, shortName)){
                return new ResponseForm(CommandResponses.UPDATE_ROLE_ALREADY_TAKEN);
            }

            role.setRoleName(roleName);
            role.setShortName(shortName);

            roleService.saveRole(role);

            return new ResponseForm("Successfully updated role with *id:* **" + id + "** :white_check_mark:");
        }
        return new ResponseForm(CommandResponses.UPDATE_ROLE_INVALID_ID);
    }
}
