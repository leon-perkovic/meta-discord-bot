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

/**
 * !role <id or role_name or short_name>
 * Command for getting a role entry from a database
 * <p>
 * Created by Leon on 19/03/2018
 */
@Component
public class RoleCommand extends AbstractCommand {

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public RoleCommand() {
        super("role",
                "**!role <id or role_name or short_name>**"
                        + "\n -> Get information about a specific role.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.ROLE_INVALID_ARGUMENTS).queue();
            return;
        }

        Role role;
        if(roleValidator.validateIfNumeric(arguments.get(0))) {
            Long id = Long.valueOf(arguments.get(0));
            role = roleService.findById(id);

        }else {
            role = roleService.findByRoleName(arguments.get(0));

            if(role == null) {
                role = roleService.findByShortName(arguments.get(0));
            }
        }

        if(role != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("__Role info:__");
            embedBuilder.setDescription("**" + role.getRoleName() + "** (" + role.getShortName() + ", *id:* " + role.getId() + ")");
            embedBuilder.setColor(Color.decode("#D02F00"));

            messageChannel.sendMessage(embedBuilder.build()).queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.ROLE_NOT_FOUND).queue();
    }

}
