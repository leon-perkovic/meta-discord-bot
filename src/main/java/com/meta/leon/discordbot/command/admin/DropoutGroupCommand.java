package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.util.CommandUtil;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.Group;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.GroupService;
import com.meta.leon.discordbot.validator.EventSignupValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !dropoutGroup <group_id or group_name> <event_id or event_name or day> [HH:mm]
 * [HH:mm] is optional - only expected in combination with day
 * Command for signing group up for an event
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * Created by Leon on 20/04/2018
 */
@Component
public class DropoutGroupCommand extends AbstractCommand{

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventService eventService;

    @Autowired
    GroupService groupService;

    @Autowired
    EventSignupValidator eventSignupValidator;

    @Autowired
    CommandUtil commandUtil;

    public DropoutGroupCommand(){
        super("dropoutgroup",
                "**!dropoutGroup <group_id or group_name> <event_id or event_name or day> [HH:mm]**"
                        + "\n -> Drop group out of specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!eventSignupValidator.validateMinNumberOfArguments(arguments, 2)){
            messageChannel.sendMessage(CommandResponses.DROPOUT_GROUP_INVALID_ARGUMENTS).queue();
            return;
        }
        if(arguments.size() == 3){
            if(!eventSignupValidator.validateIfTime(arguments.get(2))){
                messageChannel.sendMessage(CommandResponses.DROPOUT_GROUP_INVALID_ARGUMENTS).queue();
                return;
            }
        }

        // get group
        Group group;
        if(eventSignupValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            group = groupService.findById(id);
        }else{
            group = groupService.findByName(arguments.get(0));
        }
        if(group == null){
            messageChannel.sendMessage(CommandResponses.GROUP_NOT_FOUND).queue();
            return;
        }

        // get event
        Event event;
        Long eventId;
        if(eventSignupValidator.validateIfNumeric(arguments.get(1))){
            eventId = Long.valueOf(arguments.get(1));
            event = eventService.findById(eventId);

        }else if(eventSignupValidator.validateIfDay(arguments.get(1)) && arguments.size() == 3){
            String eventName = commandUtil.createEventName(arguments.get(1), arguments.get(2));
            event = eventService.findByName(eventName);

        }else{
            event = eventService.findByName(arguments.get(1));
        }

        if(event == null){
            messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
            return;
        }
        eventId = event.getId();

        for(Player player : group.getPlayers()){
            eventSignupService.removeEventSignup(eventId, player.getId());
        }
        messageChannel.sendMessage(CommandResponses.DROPOUT_GROUP_SUCCESS).queue();
    }

}
