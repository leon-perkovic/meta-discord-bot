package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.DiscordBotApp;
import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventDropout;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventDropoutService;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.EventSignupValidator;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !dropout <id or name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for dropping out of an event
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * Created by Leon on 01/04/2018
 */
@Component
public class DropoutCommand extends AbstractCommand{

    private Long eventId;
    private Long playerId;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventDropoutService eventDropoutService;

    @Autowired
    EventService eventService;

    @Autowired
    PlayerService playerService;

    @Autowired
    EventSignupValidator eventSignupValidator;

    @Autowired
    CommandUtil commandUtil;


    public DropoutCommand(){
        super("dropout", "Drop out of an event", "N/A", CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!eventSignupValidator.validateMinNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.DROPOUT_INVALID_ARGUMENTS);
        }
        if(arguments.size() == 2){
            if(!eventSignupValidator.validateIfTime(arguments.get(1))){
                return new ResponseForm(CommandResponses.DROPOUT_INVALID_ARGUMENTS);
            }
        }

        Player player = playerService.findByDiscordId(user.getAsMention());

        // check if player exists
        if(player == null){
            return new ResponseForm(CommandResponses.DROPOUT_INVALID_PLAYER);
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
        if(eventSignupValidator.validateIfUniqueSignup(eventId, playerId)){
            return new ResponseForm(CommandResponses.SIGNUP_NOT_FOUND);
        }

        EventSignup eventSignup = eventSignupService.findEventSignup(eventId, playerId);

        // if event is full and user dropping out wasn't backup - notify first backup a spot opened up
        if(!eventSignup.isBackup()){
            String discordRank = eventSignup.getDiscordRank();
            EventSignup backupEventSignup = eventSignupService.findFirstByRankAndBackup(eventId, discordRank, true);

            // check if there are candidates
            if(backupEventSignup != null){
                Player backupPlayer = playerService.findById(backupEventSignup.getPlayerId());

                if(backupPlayer.getDiscordId() != null){
                    eventSignupService.updateBackup(eventId, backupPlayer.getId(), false);

                    DateTimeZone timeZone = event.getEventTime().getZone();
                    String zone = timeZone.getShortName(event.getEventTime().getMillis());
                    String message = "Hey! Spot opened up for the event on **"
                            + event.getEventTime().toString("dd/MM/yyyy - HH:mm") + " " + zone
                            + "**, so you've been moved from backup to main signup :slight_smile:";

                    User backupUser = DiscordBotApp.getJdaBot().getUserById(commandUtil.convertDiscordMentionToId(backupPlayer.getDiscordId()));
                    backupUser.openPrivateChannel().queue((channel) -> channel.sendMessage(message).queue());
                }
            }
        }

        EventDropout eventDropout = new EventDropout(eventId, playerId, player.getNickname(),
                eventSignup.getDiscordRank(), eventSignup.isBackup(), eventSignup.getSignupTime(), new DateTime());
        eventDropoutService.saveEventDropout(eventDropout);

        eventSignupService.removeEventSignup(eventId, playerId);

        return new ResponseForm(CommandResponses.DROPOUT_SUCCESS);
    }

}