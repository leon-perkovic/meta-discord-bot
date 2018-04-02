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
import java.util.List;

/**
 * !getRoles
 * Command for getting all role entries from a database
 *
 * Created by Leon on 19/03/2018
 */
@Component
public class GetRolesCommand extends AbstractCommand{

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public GetRolesCommand(){
        super("getroles",
                "**!getRoles**"
                + "\n -> Get information about all roles.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 0)){
            return new ResponseForm(CommandResponses.GET_ROLES_INVALID_ARGUMENTS);
        }

        List<Role> roles = roleService.findAll();

        if(roles.isEmpty()){
            return new ResponseForm(CommandResponses.GET_ROLES_NONE_FOUND);
        }

        StringBuilder rolesInfo = new StringBuilder("");

        for(Role role : roles){
            rolesInfo.append("**")
                    .append(role.getRoleName())
                    .append("** (")
                    .append(role.getShortName())
                    .append(", *id:* ")
                    .append(role.getId())
                    .append(")\n");
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("__Roles:__");
        embedBuilder.setDescription(rolesInfo.toString());
        embedBuilder.setColor(Color.decode("#D02F00"));

        return new ResponseForm(embedBuilder.build());
    }

}
