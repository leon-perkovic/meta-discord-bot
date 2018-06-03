package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.util.CommandUtil;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;

/**
 * !event <id or name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for getting event entries from a database
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 * <p>
 * Created by Leon on 22/03/2018
 */
@Component
public class EventCommand extends AbstractCommand {

    @Autowired
    EventService eventService;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;

    public EventCommand() {
        super("event",
                "**!event <id or name or day> [HH:mm]**"
                        + "\n -> Get information about a specific event. Date will be set for the first upcoming day in the week.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.EVENT_INVALID_ARGUMENTS).queue();
            return;
        }
        if(arguments.size() == 2) {
            if(!eventValidator.validateIfTime(arguments.get(1))) {
                messageChannel.sendMessage(CommandResponses.EVENT_INVALID_ARGUMENTS).queue();
                return;
            }
        }

        Event event;
        if(eventValidator.validateIfNumeric(arguments.get(0))) {
            event = eventService.findById(Long.valueOf(arguments.get(0)));

        }else if(eventValidator.validateIfDay(arguments.get(0)) && arguments.size() == 2) {
            String name = commandUtil.createEventName(arguments.get(0), arguments.get(1));
            event = eventService.findByName(name);

        }else {
            event = eventService.findByName(arguments.get(0));
        }

        if(event == null) {
            messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
            return;
        }

        String signup;
        StringBuilder signups = new StringBuilder();
        StringBuilder backups = new StringBuilder();

        for(Player player : event.getPlayers()) {
            EventSignup eventSignup = eventSignupService.findEventSignup(event.getId(), player.getId());
            String discordRank = eventSignup.getDiscordRank();

            signup = "\n**" + player.getNickname() + "** ("
                    + discordRank + "), "
                    + player.getDiscordId()
                    + "\n- *Roles:* " + player.rolesToString();

            if(eventSignup.isBackup()) {
                backups.append(signup);
            }else {
                signups.append(signup);
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(event.getName() + " (id: " + event.getId() + ")");
        embedBuilder.setColor(Color.decode("#D02F00"));

        String fieldValue = commandUtil.createEventBody(event);

        embedBuilder.setDescription(fieldValue);
        messageChannel.sendMessage(embedBuilder.build()).queue();

        if(signups.length() > 0) {
            embedBuilder.setTitle("Signups:");
            embedBuilder.setDescription(signups.toString());
            messageChannel.sendMessage(embedBuilder.build()).queue();
        }
        if(backups.length() > 0) {
            embedBuilder.setTitle("Backups:");
            embedBuilder.setDescription(backups.toString());
            messageChannel.sendMessage(embedBuilder.build()).queue();
        }
    }

}
