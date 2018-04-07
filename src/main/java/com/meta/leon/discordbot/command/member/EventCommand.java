package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;

/**
 * !event <id or name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for getting event entries from a database
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * Created by Leon on 22/03/2018
 */
@Component
public class EventCommand extends AbstractCommand{

    @Autowired
    EventService eventService;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public EventCommand(){
        super("event",
                "**!event <id or name or day> [HH:mm]**"
                + "\n -> Get information about a specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.EVENT_INVALID_ARGUMENTS);
        }
        if(arguments.size() == 2){
            if(!eventValidator.validateIfTime(arguments.get(1))){
                return new ResponseForm(CommandResponses.EVENT_INVALID_ARGUMENTS);
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

            String fieldValue = commandUtil.createEventBody(event);
            fieldValue += "\n------------------------------";

            StringBuilder signups = new StringBuilder("");
            StringBuilder backups = new StringBuilder("");

            for(Player player : event.getPlayers()){
                EventSignup eventSignup = eventSignupService.findEventSignup(event.getId(), player.getId());
                String discordRank = eventSignup.getDiscordRank();

                if(eventSignup.isBackup()){
                    backups.append("\n**")
                            .append(player.getNickname())
                            .append("** (")
                            .append(discordRank)
                            .append("), ")
                            .append(player.getDiscordId());
                }else{
                    signups.append("\n**")
                            .append(player.getNickname())
                            .append("** (")
                            .append(discordRank)
                            .append("), ")
                            .append(player.getDiscordId());
                }
            }

            signups.append("\n------------------------------");

            embedBuilder.addField(event.getName() + " (id: " + event.getId() + ")", fieldValue, false);
            embedBuilder.addField("Signups:", signups.toString(), false);
            embedBuilder.addField("Backups:", backups.toString(), false);

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
    }

}
