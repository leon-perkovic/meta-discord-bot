package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.RoleValidator;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !addRole <role_name> <short_name>
 * Command for adding new role entries to a database
 *
 * Created by Leon on 18/03/2018
 */
@Component
public class AddRoleCommand extends AbstractCommand{

    private String roleName;
    private String shortName;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public AddRoleCommand(){
        super("addrole",
                "**!addRole <role_name> <short_name>**"
                + "\n -> Add a new role.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 2)){
            return new ResponseForm(CommandResponses.ADD_ROLE_INVALID_ARGUMENTS);
        }

        this.roleName = arguments.get(0);
        this.shortName = arguments.get(1);

        if(!roleValidator.validateIfUniqueRole(roleName, shortName)){
            return new ResponseForm(CommandResponses.ROLE_ALREADY_EXISTS);
        }

        Role role = new Role(roleName, shortName);
        roleService.saveRole(role);

        return new ResponseForm("Successfully added role: **" + roleName + "** :white_check_mark:");
    }

}
