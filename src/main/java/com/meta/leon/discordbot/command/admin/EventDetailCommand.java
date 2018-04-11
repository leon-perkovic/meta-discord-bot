package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
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
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
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
        super("eventdetail",
                "**!eventDetail <id or name or day> [HH:mm]**"
                + "\n -> Get detailed information about a specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 1)){
            messageChannel.sendMessage(CommandResponses.EVENT_DETAIL_INVALID_ARGUMENTS).queue();
            return;
        }
        if(arguments.size() == 2){
            if(!eventValidator.validateIfTime(arguments.get(1))){
                messageChannel.sendMessage(CommandResponses.EVENT_DETAIL_INVALID_ARGUMENTS).queue();
                return;
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
            String signup;
            String dropout;

            StringBuilder signups = new StringBuilder();
            StringBuilder backups = new StringBuilder();
            StringBuilder dropouts = new StringBuilder();

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
                    backups.append(signup);
                }else{
                    signups.append(signup);
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

                dropouts.append(dropout);
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(event.getName() + " (id: " + event.getId() + ")");
            embedBuilder.setColor(Color.decode("#D02F00"));

            String fieldValue = commandUtil.createEventBody(event);

            embedBuilder.setDescription(fieldValue);
            messageChannel.sendMessage(embedBuilder.build()).queue();

            if(signups.length() > 0){
                embedBuilder.setTitle("Signups:");
                embedBuilder.setDescription(signups.toString());
                messageChannel.sendMessage(embedBuilder.build()).queue();
            }
            if(backups.length() > 0){
                embedBuilder.setTitle("Backups:");
                embedBuilder.setDescription(backups.toString());
                messageChannel.sendMessage(embedBuilder.build()).queue();
            }
            if(dropouts.length() > 0){
                embedBuilder.setTitle("Dropouts:");
                embedBuilder.setDescription(dropouts.toString());
                messageChannel.sendMessage(embedBuilder.build()).queue();
            }

            return;
        }
        messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
    }

}
