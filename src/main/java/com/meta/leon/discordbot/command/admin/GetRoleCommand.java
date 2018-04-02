package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.RoleValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;

/**
 * !getRole <id or role_name or short_name>
 * Command for getting a role entry from a database
 *
 * Created by Leon on 19/03/2018
 */
@Component
public class GetRoleCommand extends AbstractCommand{

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public GetRoleCommand(){
        super("getrole",
                "**!getRole <id or role_name or short_name>**"
                + "\n -> Get information about a specific role.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.GET_ROLE_INVALID_ARGUMENTS);
        }

        Role role;
        if(roleValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            role = roleService.findById(id);

        }else{
            role = roleService.findByRoleName(arguments.get(0));

            if(role == null){
                role = roleService.findByShortName(arguments.get(0));
            }
        }

        if(role != null){
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("__Role info:__");
            embedBuilder.setDescription("**" + role.getRoleName() + "** (" + role.getShortName() + ", *id:* " + role.getId() + ")");
            embedBuilder.setColor(Color.decode("#D02F00"));

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.ROLE_NOT_FOUND);
    }

}
