package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.service.GroupService;
import com.meta.leon.discordbot.validator.GroupValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removeGroup <id or name>
 * Command for removing group entries from a database
 * <p>
 * Created by Leon on 20/04/2018
 */
@Component
public class RemoveGroupCommand extends AbstractCommand {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupValidator groupValidator;

    public RemoveGroupCommand() {
        super("removegroup",
                "**!removeGroup <id or name>**"
                        + "\n -> Remove a specific group.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!groupValidator.validateNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.REMOVE_GROUP_INVALID_ARGUMENTS).queue();
            return;
        }

        int numOfRemoved;
        if(groupValidator.validateIfNumeric(arguments.get(0))) {
            Long id = Long.valueOf(arguments.get(0));
            numOfRemoved = groupService.removeById(id);
        }else {
            numOfRemoved = groupService.removeByName(arguments.get(0));
        }

        if(numOfRemoved > 0) {
            messageChannel.sendMessage(CommandResponses.REMOVE_GROUP_SUCCESS).queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.GROUP_NOT_FOUND).queue();
    }

}
