package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventDropout;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventDropoutService;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !eventDetail <id or name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for getting detailed event entries from a database
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * Created by Leon on 02/04/2018
 */
@Component
public class EventDetailCommand extends AbstractCommand{

    @Autowired
    EventService eventService;

    @Autowired
    PlayerService playerService;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventDropoutService eventDropoutService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public EventDetailCommand(){
        super("eventdetail", "Get detailed event info from a database", "N/A", CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

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

            String fieldValue = commandUtil.createEventBody(event);
            fieldValue += "\n------------------------------";

            StringBuilder signups = new StringBuilder("");
            StringBuilder backups = new StringBuilder("");
            StringBuilder dropouts = new StringBuilder("");

            for(Player player : event.getPlayers()){
                EventSignup eventSignup = eventSignupService.findEventSignup(event.getId(), player.getId());
                String discordRank = eventSignup.getDiscordRank();

                DateTimeZone timeZone = event.getEventTime().getZone();
                String zone = timeZone.getShortName(event.getEventTime().getMillis());

                if(eventSignup.isBackup()){
                    backups.append("\n**")
                            .append(player.getNickname())
                            .append("** (")
                            .append(discordRank)
                            .append("), ")
                            .append(player.getDiscordId())
                            .append("\n")
                            .append(player.rolesToString())
                            .append("\n- *Signup time:* **")
                            .append(eventSignup.getSignupTime().toString("dd/MM/yyyy - HH:mm:ss"))
                            .append(" ").append(zone).append("**");
                }else{
                    signups.append("\n**")
                            .append(player.getNickname())
                            .append("** (")
                            .append(discordRank)
                            .append("), ")
                            .append(player.getDiscordId())
                            .append("\n")
                            .append(player.rolesToString())
                            .append("\n- *Signup time:* **")
                            .append(eventSignup.getSignupTime().toString("dd/MM/yyyy - HH:mm:ss"))
                            .append(" ").append(zone).append("**");
                }
            }

            List<EventDropout> eventDropouts = eventDropoutService.findAllByEventId(event.getId());

            for(EventDropout eventDropout : eventDropouts){
                DateTimeZone timeZone = event.getEventTime().getZone();
                String zone = timeZone.getShortName(event.getEventTime().getMillis());

                dropouts.append("\n**")
                        .append(eventDropout.getNickname())
                        .append("** (")
                        .append(eventDropout.getDiscordRank())
                        .append(")");

                if(eventDropout.isBackup()){
                    dropouts.append(" [*backup*]");
                }
                dropouts.append("\n- *Signup time:* **")
                        .append(eventDropout.getSignupTime().toString("dd/MM/yyyy - HH:mm:ss"))
                        .append(" ").append(zone).append("**")
                        .append("\n- *Dropout time:* **")
                        .append(eventDropout.getDropoutTime().toString("dd/MM/yyyy - HH:mm:ss"))
                        .append(" ").append(zone).append("**");
            }

            signups.append("\n------------------------------");
            backups.append("\n------------------------------");

            embedBuilder.addField(event.getName() + " (id: " + event.getId() + ")", fieldValue, false);
            embedBuilder.addField("Signups:", signups.toString(), false);
            embedBuilder.addField("Backups:", backups.toString(), false);
            embedBuilder.addField("Dropouts:", dropouts.toString(), false);

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
    }

}
