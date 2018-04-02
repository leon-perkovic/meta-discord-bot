package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.DiscordBotApp;
import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !announce <id or event_name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for announcing an event
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * Created by Leon on 02/04/2018
 */
@Component
public class AnnounceCommand extends AbstractCommand{

    private Long eventId;

    @Autowired
    EventService eventService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;


    public AnnounceCommand(){
        super("announce",
                "**!announce <id or event_name or day> [HH:mm]**"
                + "\n -> Make an announcement for a specific event.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.SIGNUP_INVALID_ARGUMENTS);
        }
        if(arguments.size() == 2){
            if(!globalValidator.validateIfTime(arguments.get(1))){
                return new ResponseForm(CommandResponses.SIGNUP_INVALID_ARGUMENTS);
            }
        }

        Event event;

        // check if event exists
        if(globalValidator.validateIfNumeric(arguments.get(0))){
            this.eventId = Long.valueOf(arguments.get(0));

            event = eventService.findById(eventId);

            if(event == null){
                return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
            }

        }else if(globalValidator.validateIfDay(arguments.get(0)) && arguments.size() == 2){
            String eventName = commandUtil.createEventName(arguments.get(0), arguments.get(1));

            event = eventService.findByName(eventName);

            if(event == null){
                return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
            }
            this.eventId = event.getId();

        }else{
            event = eventService.findByName(arguments.get(0));

            if(event == null){
                return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
            }
            this.eventId = event.getId();
        }


        // get roles for Member and Trial
        Role memberRole = commandUtil.getRoleByName(user, DiscordBotApp.getMemberRole());
        Role trialRole = commandUtil.getRoleByName(user, DiscordBotApp.getTrialRole());

        String day = event.getName().split("-")[0];
        day = day.substring(0, 1).toUpperCase() + day.substring(1);

        DateTime eventTime = event.getEventTime();

        DateTimeZone timeZone = event.getEventTime().getZone();
        String zone = timeZone.getShortName(event.getEventTime().getMillis());

        // build announcement message
        String announcement = memberRole.getAsMention() + ", " + trialRole.getAsMention()
                            + "\n**" + day + "**, **"
                            + eventTime.toString("dd/MM/yyyy - HH:mm") + " " + zone + "**."
                            + "\n*Description:* **" + event.getDescription()
                            + "**\n*Player limit:* **" + event.getPlayerLimit() + "**";

        if(event.getMemberLimit() < event.getPlayerLimit()){
            announcement += "\n*Member limit:* **" + event.getMemberLimit() + "**";
        }
        if(event.getTrialLimit() < event.getPlayerLimit()){
            announcement += "\n*Trial limit:* **" + event.getTrialLimit() + "**";
        }
        announcement += "\n*Event leader:* " + event.getEventLeader()
                    + "\n--------------------------\nTo sign-up, please use: "
                    + "```!signup " + day + " " + eventTime.toString("HH:mm") + "```";

        return new ResponseForm(announcement);
    }

}