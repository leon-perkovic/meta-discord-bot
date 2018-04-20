package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventDropoutService;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.EventSignupValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !dropoutPlayer <player_id or nickname or @username> <event_id or name or day> [HH:mm]
 * [HH:mm] is optional - only expected in combination with day
 * Command for dropping player out of an event
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 * <p>
 * Created by Leon on 11/04/2018
 */
@Component
public class DropoutPlayerCommand extends AbstractCommand {

    private Long eventId;
    private Long playerId;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventDropoutService eventDropoutService;

    @Autowired
    EventService eventService;

    @Autowired
    PlayerService playerService;

    @Autowired
    EventSignupValidator eventSignupValidator;

    @Autowired
    CommandUtil commandUtil;


    public DropoutPlayerCommand() {
        super("dropoutplayer",
                "**!dropoutPlayer <player_id or nickname or @username> <event_id or name or day> [HH:mm]**"
                        + "\n -> Drop player out of specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!eventSignupValidator.validateMinNumberOfArguments(arguments, 2)) {
            messageChannel.sendMessage(CommandResponses.DROPOUT_PLAYER_INVALID_ARGUMENTS).queue();
            return;
        }
        if(arguments.size() == 3) {
            if(!eventSignupValidator.validateIfTime(arguments.get(2))) {
                messageChannel.sendMessage(CommandResponses.SIGNUP_PLAYER_INVALID_ARGUMENTS).queue();
                return;
            }
        }

        // get player
        Player player = commandUtil.findPlayerByAnyReference(arguments.get(0));
        if(player == null) {
            messageChannel.sendMessage(CommandResponses.DROPOUT_PLAYER_INVALID_PLAYER).queue();
            return;
        }
        this.playerId = player.getId();

        // get event
        Event event;
        if(eventSignupValidator.validateIfNumeric(arguments.get(1))) {
            this.eventId = Long.valueOf(arguments.get(1));
            event = eventService.findById(eventId);

        }else if(eventSignupValidator.validateIfDay(arguments.get(1)) && arguments.size() == 3) {
            String eventName = commandUtil.createEventName(arguments.get(1), arguments.get(2));
            event = eventService.findByName(eventName);

        }else {
            event = eventService.findByName(arguments.get(1));
        }

        if(event == null) {
            messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
            return;
        }
        this.eventId = event.getId();

        // check if player is already signed up for this event
        if(eventSignupValidator.validateIfUniqueSignup(eventId, playerId)) {
            messageChannel.sendMessage(CommandResponses.PLAYER_SIGNUP_NOT_FOUND).queue();
            return;
        }

        eventSignupService.removeEventSignup(eventId, playerId);

        messageChannel.sendMessage(CommandResponses.DROPOUT_PLAYER_SUCCESS).queue();
    }

}
