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
import java.util.Collections;
import java.util.List;

/**
 * !pastEvents [page_number]
 * [page_number] is optional, if not specified it defaults at 1
 * Command for getting past event entries from a database, 10 per page
 *
 * Created by Leon on 22/03/2018
 */
@Component
public class PastEventsCommand extends AbstractCommand{

    private static final Integer PAGE_SIZE = 10;

    @Autowired
    EventService eventService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public PastEventsCommand(){
        super("pastevents",
                "**!pastEvents [page_number]**"
                + "\n -> Get information about all past events. Shows 10 events per page.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 0)){
            return new ResponseForm(CommandResponses.PAST_EVENTS_INVALID_ARGUMENT);
        }

        // set default page
        int page = 1;

        if(arguments.size() == 1){
            if(!eventValidator.validateIfNumeric(arguments.get(0))){
                return new ResponseForm(CommandResponses.PAST_EVENTS_INVALID_ARGUMENT);
            }
            page = Integer.valueOf(arguments.get(0));
        }

        // get all events in descending order
        List<Event> events = eventService.findPast(new DateTime());

        if(page < 1){
            page = 1;
        }

        int maxPage = events.size()/PAGE_SIZE + 1;
        if(maxPage < page){
            page = maxPage;
        }

        if(!events.isEmpty()){
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("__Past events - page " + page + ":__");
            embedBuilder.setColor(Color.decode("#D02F00"));

            List<Event> eventsPage;

            if(events.size() >= page*PAGE_SIZE){
                eventsPage = events.subList((page-1)*PAGE_SIZE, page*PAGE_SIZE);
            }else{
                eventsPage = events.subList((page-1)*PAGE_SIZE, events.size());
            }

            // reverse events list
            Collections.reverse(eventsPage);

            for(Event event : eventsPage){

                String fieldValue = commandUtil.createEventBody(event);
                fieldValue += "\n------------------------------";

                embedBuilder.addField(event.getName() + " (id: " + event.getId() + ")", fieldValue, false);
            }

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.EVENTS_NONE_FOUND);
    }

}