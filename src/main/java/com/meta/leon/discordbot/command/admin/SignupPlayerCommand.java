package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.BotListener;
import com.meta.leon.discordbot.DiscordBotApp;
import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.EventSignupValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * !signupPlayer <player_id or nickname or @username> <event_id or name or day> [HH:mm]
 * [HH:mm] is optional - only expected in combination with day
 * Command for signing player up for an event
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 * <p>
 * Created by Leon on 11/04/2018
 */
@Component
public class SignupPlayerCommand extends AbstractCommand {

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


    public SignupPlayerCommand() {
        super("signupplayer",
                "**!signupPlayer <player_id or nickname or @username> <event_id or name or day> [HH:mm]**"
                        + "\n -> Sign player up for specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!eventSignupValidator.validateMinNumberOfArguments(arguments, 2)) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_PLAYER_INVALID_ARGUMENTS).queue();
            return;
        }
        if(arguments.size() == 3) {
            if(!eventSignupValidator.validateIfTime(arguments.get(2))) {
                messageChannel.sendMessage(CommandResponses.SIGNUP_PLAYER_INVALID_ARGUMENTS).queue();
                return;
            }
        }

        // get player
        Player player = commandUtil.findPlayerByAnyReference(arguments.get(0));
        if(player == null) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_PLAYER_INVALID_PLAYER).queue();
            return;
        }
        this.playerId = player.getId();

        // get event
        Event event;
        if(eventSignupValidator.validateIfNumeric(arguments.get(1))) {
            this.eventId = Long.valueOf(arguments.get(1));
            event = eventService.findById(eventId);

        }else if(eventSignupValidator.validateIfDay(arguments.get(1)) && arguments.size() == 3) {
            String eventName = commandUtil.createEventName(arguments.get(1), arguments.get(2));
            event = eventService.findByName(eventName);

        }else {
            event = eventService.findByName(arguments.get(1));
        }

        if(event == null) {
            messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
            return;
        }
        this.eventId = event.getId();

        // check if player is already signed up for this event
        if(!eventSignupValidator.validateIfUniqueSignup(eventId, playerId)) {
            messageChannel.sendMessage(CommandResponses.PLAYER_SIGNUP_ALREADY_EXISTS).queue();
            return;
        }

        // determine if user is member or trial
        User user = DiscordBotApp.getJdaBot().getUserById(commandUtil.convertDiscordMentionToId(player.getDiscordId()));
        List<String> roleNames = BotListener.getUserRoles(user);
        String userRole = "";
        if(roleNames.contains(DiscordBotApp.getMemberRole())) {
            userRole = DiscordBotApp.getMemberRole();

        }else if(roleNames.contains(DiscordBotApp.getTrialRole())) {
            userRole = DiscordBotApp.getTrialRole();
        }

        // determine if user is backup
        boolean isBackup = false;
        Integer totalSignups = eventSignupService.getNumOfSignups(eventId, false);
        Integer totalSignupsForRank = eventSignupService.getNumOfSignupsByRank(eventId, userRole, false);

        if(totalSignups >= event.getPlayerLimit()) {
            isBackup = true;

        }else if(DiscordBotApp.getMemberRole().equals(userRole) && totalSignupsForRank >= event.getMemberLimit()) {
            isBackup = true;

        }else if(DiscordBotApp.getTrialRole().equals(userRole) && totalSignupsForRank >= event.getTrialLimit()) {
            isBackup = true;
        }

        EventSignup eventSignup = new EventSignup(eventId, playerId, userRole, isBackup, new DateTime());
        eventSignupService.saveEventSignup(eventSignup);

        if(isBackup) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_PLAYER_FULL).queue();
            return;
        }

        messageChannel.sendMessage(CommandResponses.SIGNUP_PLAYER_SUCCESS).queue();
    }

}
