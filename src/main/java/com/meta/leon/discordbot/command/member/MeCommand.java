package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.EventSignupValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !me
 * Command used for displaying user's information
 * <p>
 * Created by Leon on 12/04/2018
 */
@Component
public class MeCommand extends AbstractCommand {

    private Long playerId;

    @Autowired
    PlayerService playerService;

    @Autowired
    EventService eventService;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventSignupValidator eventSignupValidator;


    public MeCommand() {
        super("me",
                "**!me**"
                        + "\n -> Get information about yourself, such as roles and current signups.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();
        User user = discordEvent.getAuthor();

        // validate passed arguments
        if(!eventSignupValidator.validateNumberOfArguments(arguments, 0)) {
            messageChannel.sendMessage(CommandResponses.ME_INVALID_ARGUMENTS).queue();
            return;
        }

        // get player
        Player player = playerService.findByDiscordId(user.getAsMention());
        if(player == null) {
            messageChannel.sendMessage(CommandResponses.ME_INVALID_PLAYER).queue();
            return;
        }
        this.playerId = player.getId();

        List<Event> events = eventService.findUpcoming(new DateTime());
        if(!events.isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(player.getNickname());
            embedBuilder.setDescription(
                    "\n*Account name:* **" + player.getAccountName() + "**"
                            + "\n*Discord:* " + player.getDiscordId()
                            + "\n" + player.rolesToString()
                            + "\n" + player.groupsToString()
            );
            embedBuilder.setColor(Color.decode("#D02F00"));

            StringBuilder signups = new StringBuilder();
            for(Event event : events) {
                EventSignup eventSignup = eventSignupService.findEventSignup(event.getId(), playerId);
                if(eventSignup != null) {
                    String day = event.getName().split("-")[0];
                    day = day.substring(0, 1).toUpperCase() + day.substring(1);

                    DateTime eventTime = event.getEventTime();
                    DateTimeZone timeZone = eventTime.getZone();
                    String zone = timeZone.getShortName(eventTime.getMillis());

                    signups.append("**")
                            .append(day)
                            .append("**");
                    if(eventSignup.isBackup()) {
                        signups.append(" (Backup)");
                    }
                    signups.append(" - ")
                            .append(event.getDescription())
                            .append("\n*")
                            .append(eventTime.toString("dd/MM/yyyy - HH:mm"))
                            .append(" ").append(zone).append("*\n\n");
                }
            }
            embedBuilder.addField("Signups:", signups.toString(), false);

            messageChannel.sendMessage(embedBuilder.build()).queue();
        }
    }

}
