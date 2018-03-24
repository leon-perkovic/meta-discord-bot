package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;

/**
 * !getEvent <id or name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for getting event entries from a database
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * @author Leon, created on 22/03/2018
 */
@Component
public class GetEventCommand extends AbstractCommand{

    @Autowired
    EventService eventService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public GetEventCommand(){
        super("getevent", "Get event info from a database", "N/A", CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.GET_EVENT_INVALID_ARGUMENTS);
        }
        if(arguments.size() == 2){
            if(!eventValidator.validateIfTime(arguments.get(1))){
                return new ResponseForm(CommandResponses.GET_EVENT_INVALID_ARGUMENTS);
            }
        }

        Event event;

        if(eventValidator.validateIfNumeric(arguments.get(0))){
            event = eventService.findById(Long.valueOf(arguments.get(0)));

        }else if(eventValidator.validateIfDay(arguments.get(0)) && arguments.size() == 2){
            String name = commandUtil.createEventName(arguments.get(0), arguments.get(1));

            event = eventService.findByName(name);

        }else{
            event = eventService.findByName(arguments.get(0));
        }

        if(event != null){
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("__Event info:__");
            embedBuilder.setColor(Color.decode("#D02F00"));
            embedBuilder.addField(
                    event.getName() + " (id: " + event.getId() + ")",
                    "*Description:*  **" + event.getDescription() + "**\n"
                    + "*Time:*  **" + event.getEventTime().toString("dd/MM/yyyy - HH:mm") + "**\n"
                    + "*Player limit:*  **" + event.getPlayerLimit() + "**\n"
                    + "*Leader:*  **" + event.getEventLeader() + "**",
                    true);

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
    }

}
