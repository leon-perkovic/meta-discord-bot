package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.util.CommandUtil;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removeEvent <id or name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for removing event entries from a database
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 * <p>
 * Created by Leon on 22/03/2018
 */
@Component
public class RemoveEventCommand extends AbstractCommand {

    @Autowired
    EventService eventService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;

    public RemoveEventCommand() {
        super("removeevent",
                "**!removeEvent <id or name or day> [HH:mm]**"
                        + "\n -> Delete/cancel a specific event",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.REMOVE_EVENT_INVALID_ARGUMENTS).queue();
            return;
        }
        if(arguments.size() == 2) {
            if(!eventValidator.validateIfTime(arguments.get(1))) {
                messageChannel.sendMessage(CommandResponses.REMOVE_EVENT_INVALID_ARGUMENTS).queue();
                return;
            }
        }

        int numOfRemoved;
        if(eventValidator.validateIfNumeric(arguments.get(0))) {
            numOfRemoved = eventService.removeById(Long.valueOf(arguments.get(0)));

        }else if(eventValidator.validateIfDay(arguments.get(0)) && arguments.size() == 2) {
            String name = commandUtil.createEventName(arguments.get(0), arguments.get(1));

            numOfRemoved = eventService.removeByName(name);
        }else {
            numOfRemoved = eventService.removeByName(arguments.get(0));
        }

        if(numOfRemoved > 0) {
            messageChannel.sendMessage(CommandResponses.REMOVE_EVENT_SUCCESS).queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
    }

}
