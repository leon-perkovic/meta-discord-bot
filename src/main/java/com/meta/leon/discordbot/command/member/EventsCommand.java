package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !events
 * Command for getting upcoming event entries from a database
 *
 * Created by Leon on 22/03/2018
 */
@Component
public class EventsCommand extends AbstractCommand{

    @Autowired
    EventService eventService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public EventsCommand(){
        super("events",
                "**!events**"
                + "\n -> Get information about all upcoming events.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!eventValidator.validateNumberOfArguments(arguments, 0)){
            messageChannel.sendMessage(CommandResponses.EVENTS_INVALID_ARGUMENTS).queue();
            return;
        }

        List<Event> events = eventService.findUpcoming(new DateTime());
        if(!events.isEmpty()){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("__Upcoming events (" + events.size() + "):__");
            embedBuilder.setColor(Color.decode("#D02F00"));

            for(Event event : events){
                String fieldValue = commandUtil.createEventBody(event);
                fieldValue += "\n------------------------------";

                embedBuilder.addField(event.getName() + " (id: " + event.getId() + ")", fieldValue, false);
            }

            messageChannel.sendMessage(embedBuilder.build()).queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.EVENTS_NONE_FOUND).queue();
    }

}
