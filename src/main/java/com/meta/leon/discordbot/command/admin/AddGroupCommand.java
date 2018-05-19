package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Group;
import com.meta.leon.discordbot.service.GroupService;
import com.meta.leon.discordbot.validator.GroupValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !addGroup <name>
 * Command for adding new group entries to a database
 * <p>
 * Created by Leon on 20/04/2018
 */
@Component
public class AddGroupCommand extends AbstractCommand {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupValidator groupValidator;

    public AddGroupCommand() {
        super("addgroup",
                "**!addGroup <name>**"
                        + "\n -> Add a new group.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!groupValidator.validateNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.ADD_GROUP_INVALID_ARGUMENTS).queue();
            return;
        }
        String name = arguments.get(0);

        if(!groupValidator.validateIfUniqueGroup(name)) {
            messageChannel.sendMessage(CommandResponses.GROUP_ALREADY_EXISTS).queue();
            return;
        }

        Group group = new Group(name);
        groupService.saveGroup(group);

        messageChannel.sendMessage("Successfully added group: **" + name + "** :white_check_mark:").queue();
    }

}
