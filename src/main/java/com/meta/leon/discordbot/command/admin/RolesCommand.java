package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.RoleValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !roles
 * Command for getting all role entries from a database
 *
 * Created by Leon on 19/03/2018
 */
@Component
public class RolesCommand extends AbstractCommand{

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public RolesCommand(){
        super("roles",
                "**!roles**"
                + "\n -> Get information about all roles.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 0)){
            messageChannel.sendMessage(CommandResponses.ROLES_INVALID_ARGUMENTS).queue();
            return;
        }

        List<Role> roles = roleService.findAll();
        if(roles.isEmpty()){
            messageChannel.sendMessage(CommandResponses.ROLES_NONE_FOUND).queue();
            return;
        }

        StringBuilder rolesInfo = new StringBuilder("");
        for(Role role : roles){
            rolesInfo.append("**")
                    .append(role.getRoleName())
                    .append("** (")
                    .append(role.getShortName())
                    .append(", *id:* ")
                    .append(role.getId())
                    .append(")\n\n");
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("__Roles:__");
        embedBuilder.setDescription(rolesInfo.toString());
        embedBuilder.setColor(Color.decode("#D02F00"));

        messageChannel.sendMessage(embedBuilder.build()).queue();
    }

}
