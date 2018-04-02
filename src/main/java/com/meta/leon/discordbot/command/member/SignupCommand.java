package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.BotListener;
import com.meta.leon.discordbot.DiscordBotApp;
import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.EventSignupValidator;
import net.dv8tion.jda.core.entities.User;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * !signup <id or event_name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for signing up for an event
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * Created by Leon on 01/04/2018
 */
@Component
public class SignupCommand extends AbstractCommand{

    private Long eventId;
    private Long playerId;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventService eventService;

    @Autowired
    PlayerService playerService;

    @Autowired
    EventSignupValidator eventSignupValidator;

    @Autowired
    CommandUtil commandUtil;


    public SignupCommand(){
        super("signup",
                "**!signup <id or event_name or day> [HH:mm]**"
                + "\n -> Sign up for a specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!eventSignupValidator.validateMinNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.SIGNUP_INVALID_ARGUMENTS);
        }
        if(arguments.size() == 2){
            if(!eventSignupValidator.validateIfTime(arguments.get(1))){
                return new ResponseForm(CommandResponses.SIGNUP_INVALID_ARGUMENTS);
            }
        }

        Player player = playerService.findByDiscordId(user.getAsMention());

        // check if player exists
        if(player == null){
            return new ResponseForm(CommandResponses.SIGNUP_INVALID_PLAYER);
        }
        this.playerId = player.getId();

        Event event;

        // check if event exists
        if(eventSignupValidator.validateIfNumeric(arguments.get(0))){
            this.eventId = Long.valueOf(arguments.get(0));

            event = eventService.findById(eventId);

            if(event == null){
                return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
            }

        }else if(eventSignupValidator.validateIfDay(arguments.get(0)) && arguments.size() == 2){
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

        // check if player is already signed up for this event
        if(!eventSignupValidator.validateIfUniqueSignup(eventId, playerId)){
            return new ResponseForm(CommandResponses.SIGNUP_ALREADY_EXISTS);
        }

        // determine if user is member or trial
        List<String> roleNames = BotListener.getUserRoles(user);

        String userRole = "";
        if(roleNames.contains(DiscordBotApp.getMemberRole())){
            userRole = DiscordBotApp.getMemberRole();

        }else if(roleNames.contains(DiscordBotApp.getTrialRole())){
            userRole = DiscordBotApp.getTrialRole();
        }

        // determine if user is backup
        boolean isBackup = false;

        Integer totalSignups = eventSignupService.getNumOfSignups(eventId, false);
        Integer totalSignupsForRank = eventSignupService.getNumOfSignupsByRank(eventId, userRole, false);

        if(totalSignups >= event.getPlayerLimit()){
            isBackup = true;

        }else if(DiscordBotApp.getMemberRole().equals(userRole) && totalSignupsForRank >= event.getMemberLimit()){
            isBackup = true;

        }else if(DiscordBotApp.getTrialRole().equals(userRole) && totalSignupsForRank >= event.getTrialLimit()){
            isBackup = true;
        }

        EventSignup eventSignup = new EventSignup(eventId, playerId, userRole, isBackup, new DateTime());
        eventSignupService.saveEventSignup(eventSignup);

        if(isBackup){
            return new ResponseForm(CommandResponses.SIGNUP_FULL);
        }

        return new ResponseForm(CommandResponses.SIGNUP_SUCCESS);
    }

}
