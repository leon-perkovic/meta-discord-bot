package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
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
 * Created by Leon on 22/03/2018
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
        super("getevents",
                "**!getEvents**"
                + "\n -> Get information about all upcoming events.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

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

                String fieldValue = commandUtil.createEventBody(event);
                fieldValue += "\n------------------------------";

                embedBuilder.addField(event.getName() + " (id: " + event.getId() + ")", fieldValue, false);
            }

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.GET_EVENTS_NONE_FOUND);
    }

}
