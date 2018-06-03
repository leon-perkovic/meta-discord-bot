package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.util.CommandUtil;
import com.meta.leon.discordbot.validator.GlobalValidator;
import com.meta.leon.discordbot.validator.PlayerValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * !player <id or nickname or @username>
 * Command for getting detailed information about a player
 * <p>
 * Created by Leon on 03/06/2018
 */
@Component
public class PlayerDetailCommand extends AbstractCommand {

    private static final int PAGE_SIZE = 10;

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;

    @Autowired
    EventService eventService;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;

    public PlayerDetailCommand() {
        super("playerdetail",
                "**!playerDetail <id or nickname or @username>**"
                        + "\n -> Get detailed information about a specific player.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.PLAYER_DETAIL_INVALID_ARGUMENTS).queue();
            return;
        }

        Player player = commandUtil.findPlayerByAnyReference(arguments.get(0));
        if(player == null) {
            messageChannel.sendMessage(CommandResponses.PLAYER_NOT_FOUND).queue();
            return;
        }

        // find upcoming events and check signups for them
        List<Event> events = eventService.findUpcoming(new DateTime());

        StringBuilder signups = new StringBuilder();
        int numOfSignups = 0;
        for(Event event : events) {
            EventSignup eventSignup = eventSignupService.findEventSignup(event.getId(), player.getId());
            if(eventSignup != null) {
                numOfSignups++;

                String day = event.getName().split("-")[0];
                day = day.substring(0, 1).toUpperCase() + day.substring(1);

                DateTime eventTime = event.getEventTime();
                DateTimeZone timeZone = eventTime.getZone();
                String zone = timeZone.getShortName(eventTime.getMillis());

                signups.append("**")
                        .append(day)
                        .append("**, *id:* ")
                        .append(event.getId());
                if(eventSignup.isBackup()) {
                    signups.append(" (Backup)");
                }
                signups.append(" - ")
                        .append(event.getDescription())
                        .append("\n- *")
                        .append(eventTime.toString("dd/MM/yyyy - HH:mm"))
                        .append(" ").append(zone).append("*\n");
            }
        }

        // find past events and check signups for them
        List<Event> pastEvents = eventService.findPast(new DateTime());

        ArrayList<Event> joinedEvents = new ArrayList<>();
        for(Event event : pastEvents) {
            EventSignup eventSignup = eventSignupService.findEventSignup(event.getId(), player.getId());
            if(eventSignup != null) {
                if(!eventSignup.isBackup()) {
                    joinedEvents.add(event);
                }
            }
        }

        if(joinedEvents.size() > PAGE_SIZE) {
            joinedEvents = new ArrayList<>(joinedEvents.subList(0, PAGE_SIZE));
        }

        // reverse joined events list
        Collections.reverse(joinedEvents);

        StringBuilder joined = new StringBuilder();
        for(Event event : joinedEvents) {
            String day = event.getName().split("-")[0];
            day = day.substring(0, 1).toUpperCase() + day.substring(1);

            DateTime eventTime = event.getEventTime();
            DateTimeZone timeZone = eventTime.getZone();
            String zone = timeZone.getShortName(eventTime.getMillis());

            joined.append("**")
                    .append(day)
                    .append("**, *id:* ")
                    .append(event.getId())
                    .append(" - ")
                    .append(event.getDescription())
                    .append("\n- *")
                    .append(eventTime.toString("dd/MM/yyyy - HH:mm"))
                    .append(" ").append(zone).append("*\n");
        }

        // build embedded message with player information
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("__Player info:__");
        embedBuilder.setColor(Color.decode("#D02F00"));
        embedBuilder.setDescription("**" + player.getNickname()
                + "**, " + player.getAccountName()
                + ", " + "*id:* " + player.getId()
                + ", " + player.getDiscordId()
                + "\n- *Roles:* " + player.rolesToString()
                + "\n- *Groups:* " + player.groupsToString()
                + "\n\n__**Events:**__"
                + "\n*Signed up for:* **" + numOfSignups + "**"
                + "\n*Joined:* **" + joinedEvents.size() + "**");

        messageChannel.sendMessage(embedBuilder.build()).queue();

        if(signups.length() > 0) {
            embedBuilder.setTitle("Signups:");
            embedBuilder.setDescription(signups.toString());
            messageChannel.sendMessage(embedBuilder.build()).queue();
        }
        if(joined.length() > 0) {
            embedBuilder.setTitle("Joined (last 10):");
            embedBuilder.setDescription(joined.toString());
            messageChannel.sendMessage(embedBuilder.build()).queue();
        }
    }

}
