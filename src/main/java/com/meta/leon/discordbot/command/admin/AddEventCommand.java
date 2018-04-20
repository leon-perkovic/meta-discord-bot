package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !addEvent <day> <HH:mm> <player_limit> <member_limit> <trial_limit> [event_leader] [description]
 * [event_leader] and [description] are optional
 * Command for adding new event entries to a database
 * Event date will be determined and set automatically for first upcoming day in the week
 * <p>
 * Created by Leon on 21/03/2018
 */
@Component
public class AddEventCommand extends AbstractCommand {

    private String name;
    private DateTime eventTime;
    private Integer playerLimit;
    private Integer memberLimit;
    private Integer trialLimit;
    private String eventLeader;
    private String description;

    @Autowired
    EventService eventService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public AddEventCommand() {
        super("addevent",
                "**!addEvent <day> <HH:mm> <player_limit> <member_limit> <trial_limit> [event_leader] [description]**"
                        + "\n -> Create new event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        int descStart = 5;
        // if @username was passed as an argument, push start of description
        if(arguments.size() > 5) {
            if(eventValidator.validateIfDiscordId(arguments.get(5))) {
                this.eventLeader = arguments.get(5);
                descStart = 6;
            }else {
                this.eventLeader = null;
            }
        }

        // if description wasn't specified - add it as null
        if(arguments.size() == descStart) {
            arguments.add(null);
        }

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, descStart + 1)) {
            messageChannel.sendMessage(CommandResponses.ADD_EVENT_INVALID_ARGUMENTS).queue();
            return;
        }
        if(!eventValidator.validateIfDay(arguments.get(0))) {
            messageChannel.sendMessage(CommandResponses.ADD_EVENT_INVALID_ARGUMENTS).queue();
            return;
        }
        if(!eventValidator.validateIfTime(arguments.get(1))) {
            messageChannel.sendMessage(CommandResponses.ADD_EVENT_INVALID_ARGUMENTS).queue();
            return;
        }
        if(!eventValidator.validateIfNumeric(arguments.get(2))) {
            messageChannel.sendMessage(CommandResponses.ADD_EVENT_INVALID_ARGUMENTS).queue();
            return;
        }
        if(!eventValidator.validateIfNumeric(arguments.get(3))) {
            messageChannel.sendMessage(CommandResponses.ADD_EVENT_INVALID_ARGUMENTS).queue();
            return;
        }
        if(!eventValidator.validateIfNumeric(arguments.get(4))) {
            messageChannel.sendMessage(CommandResponses.ADD_EVENT_INVALID_ARGUMENTS).queue();
            return;
        }

        // if description was split as multiple arguments - combine them
        if(arguments.size() > descStart + 1) {
            String combinedDesc = arguments.get(descStart);
            for(int i = descStart + 1; i < arguments.size(); i++) {
                combinedDesc += " " + arguments.get(i);
            }
            this.description = combinedDesc.trim();

        }else {
            this.description = arguments.get(descStart);
            if(description != null) {
                description = description.trim();
            }
        }

        this.eventTime = commandUtil.getEventDateTime(arguments.get(0), arguments.get(1));
        this.name = arguments.get(0).toLowerCase()
                + "-" + eventTime.getYearOfCentury()
                + "-" + eventTime.getMonthOfYear()
                + "-" + eventTime.getDayOfMonth()
                + "-" + eventTime.getHourOfDay()
                + "-" + eventTime.getMinuteOfHour();

        this.playerLimit = Integer.valueOf(arguments.get(2));
        this.memberLimit = Integer.valueOf(arguments.get(3));
        this.trialLimit = Integer.valueOf(arguments.get(4));

        if(memberLimit > playerLimit) {
            memberLimit = playerLimit;
        }
        if(trialLimit > playerLimit) {
            trialLimit = playerLimit;
        }

        if(!eventValidator.validateIfUniqueEvent(name)) {
            messageChannel.sendMessage(CommandResponses.EVENT_ALREADY_EXISTS).queue();
            return;
        }

        Event event = new Event(name, eventTime, description, playerLimit, memberLimit, trialLimit, eventLeader);
        eventService.saveEvent(event);

        messageChannel.sendMessage("Successfully added event: **" + name + "** :white_check_mark:").queue();
    }
}
