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
import java.util.LinkedList;
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
        super("eventdetail",
                "**!eventDetail <id or name or day> [HH:mm]**"
                + "\n -> Get detailed information about a specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.EVENT_DETAIL_INVALID_ARGUMENTS);
        }
        if(arguments.size() == 2){
            if(!eventValidator.validateIfTime(arguments.get(1))){
                return new ResponseForm(CommandResponses.EVENT_DETAIL_INVALID_ARGUMENTS);
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

            String signup;
            String dropout;

            LinkedList<String> backupsList = new LinkedList<>();
            LinkedList<String> signupsList = new LinkedList<>();
            LinkedList<String> dropoutsList = new LinkedList<>();

            for(Player player : event.getPlayers()){
                EventSignup eventSignup = eventSignupService.findEventSignup(event.getId(), player.getId());
                String discordRank = eventSignup.getDiscordRank();

                DateTimeZone timeZone = event.getEventTime().getZone();
                String zone = timeZone.getShortName(event.getEventTime().getMillis());

                signup = "\n**" + player.getNickname() + "** ("
                        + discordRank + "), "
                        + player.getDiscordId() + "\n"
                        + player.rolesToString() + "\n- *Signup time:* **"
                        + eventSignup.getSignupTime().toString("dd/MM/yyyy - HH:mm:ss")
                        + " " + zone + "**";

                if(eventSignup.isBackup()){
                    backupsList.add(signup);

                }else{
                    signupsList.add(signup);
                }
            }

            List<EventDropout> eventDropouts = eventDropoutService.findAllByEventId(event.getId());

            for(EventDropout eventDropout : eventDropouts){
                DateTimeZone timeZone = event.getEventTime().getZone();
                String zone = timeZone.getShortName(event.getEventTime().getMillis());

                dropout = "\n**" + eventDropout.getNickname()
                        + "** (" + eventDropout.getDiscordRank()
                        + ")";

                if(eventDropout.isBackup()){
                    dropout += " [*backup*]";
                }
                dropout += "\n- *Signup time:* **"
                        + eventDropout.getSignupTime().toString("dd/MM/yyyy - HH:mm:ss")
                        + " " + zone + "**"
                        +"\n- *Dropout time:* **"
                        + eventDropout.getDropoutTime().toString("dd/MM/yyyy - HH:mm:ss")
                        + " " + zone + "**";

                dropoutsList.add(dropout);
            }

            embedBuilder.addField(event.getName() + " (id: " + event.getId() + ")", fieldValue, false);

            embedBuilder.addField("------------------------------", "**Signups:**", false);
            if(!signupsList.isEmpty()){
                for(String su : signupsList){
                    embedBuilder.addField("", su, false);

                }
            }
            embedBuilder.addField("------------------------------", "**Backups:**", false);
            if(!backupsList.isEmpty()){
                for(String bu : backupsList){
                    embedBuilder.addField("", bu, false);

                }
            }
            embedBuilder.addField("------------------------------", "**Dropouts:**", false);
            if(!dropoutsList.isEmpty()){
                for(String du : dropoutsList){
                    embedBuilder.addField("", du, false);

                }
            }

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
    }

}
