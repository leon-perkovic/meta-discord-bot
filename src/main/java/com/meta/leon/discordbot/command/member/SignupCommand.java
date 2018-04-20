package com.meta.leon.discordbot.command.member;

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
 * !signup <day> <HH:mm>
 * Command for signing up for an event
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 * <p>
 * Created by Leon on 01/04/2018
 */
@Component
public class SignupCommand extends AbstractCommand {

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


    public SignupCommand() {
        super("signup",
                "**!signup <day> <HH:mm>**"
                        + "\n -> Sign up for a specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();
        User user = discordEvent.getAuthor();

        // validate passed arguments
        if(!eventSignupValidator.validateNumberOfArguments(arguments, 2)) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_INVALID_ARGUMENTS).queue();
            return;
        }
        if(!eventSignupValidator.validateIfDay(arguments.get(0))) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_INVALID_ARGUMENTS).queue();
            return;
        }
        if(!eventSignupValidator.validateIfTime(arguments.get(1))) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_INVALID_ARGUMENTS).queue();
            return;
        }

        // get player
        Player player = playerService.findByDiscordId(user.getAsMention());
        if(player == null) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_INVALID_PLAYER).queue();
            return;
        }
        this.playerId = player.getId();

        // get event
        String eventName = commandUtil.createEventName(arguments.get(0), arguments.get(1));
        Event event = eventService.findByName(eventName);
        if(event == null) {
            messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
            return;
        }
        this.eventId = event.getId();

        // check if player is already signed up for this event
        if(!eventSignupValidator.validateIfUniqueSignup(eventId, playerId)) {
            messageChannel.sendMessage(CommandResponses.SIGNUP_ALREADY_EXISTS).queue();
            return;
        }

        // determine if user is member or trial
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
            messageChannel.sendMessage(CommandResponses.SIGNUP_FULL).queue();
            return;
        }

        messageChannel.sendMessage(CommandResponses.SIGNUP_SUCCESS).queue();
    }

}
