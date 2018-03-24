package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removeRole <id or role_name or short_name>
 * Command for removing role entries from a database
 *
 * @author Leon, created on 19/03/2018
 */
@Component
public class RemoveRoleCommand extends AbstractCommand{

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public RemoveRoleCommand(){
        super("removerole", "Remove a role from a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 1)){
        return new ResponseForm(CommandResponses.REMOVE_ROLE_INVALID_ARGUMENTS);
        }

        int numOfRemoved;
        if(roleValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            numOfRemoved = roleService.removeById(id);

        }else{
            numOfRemoved = roleService.removeByRoleName(arguments.get(0));

            if(numOfRemoved == 0){
                numOfRemoved = roleService.removeByShortName(arguments.get(0));
            }
        }

        if(numOfRemoved > 0){
            return new ResponseForm(CommandResponses.REMOVE_ROLE_SUCCESS);
        }
        return new ResponseForm(CommandResponses.ROLE_NOT_FOUND);
    }

}
