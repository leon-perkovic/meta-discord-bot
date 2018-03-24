package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !getEvents
 * Command for getting upcoming event entries from a database
 *
 * @author Leon, created on 22/03/2018
 */
@Component
public class GetEventsCommand extends AbstractCommand{

    @Autowired
    EventService eventService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public GetEventsCommand(){
        super("getevents", "Get upcoming events info from a database", "N/A", CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!eventValidator.validateNumberOfArguments(arguments, 0)){
            return new ResponseForm(CommandResponses.GET_EVENTS_INVALID_ARGUMENTS);
        }

        List<Event> events = eventService.findUpcoming(new DateTime());

        if(!events.isEmpty()){
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("__Upcoming events:__");
            embedBuilder.setColor(Color.decode("#D02F00"));

            for(Event event : events){
                embedBuilder.addField(
                        event.getName() + " (id: " + event.getId() + ")",
                        "*Description:*  **" + event.getDescription() + "**\n"
                        + "*Time:*  **" + event.getEventTime().toString("dd/MM/yyyy - HH:mm") + "**\n"
                        + "*Player limit:*  **" + event.getPlayerLimit() + "**\n"
                        + "*Leader:*  **" + event.getEventLeader() + "**\n"
                        + "--------------------",
                        true);
            }

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.GET_EVENTS_NONE_FOUND);
    }

}
