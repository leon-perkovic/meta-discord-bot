package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Group;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.GroupService;
import com.meta.leon.discordbot.validator.GroupValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;

/**
 * !group <id or name>
 * Command for getting information about a specific group
 * <p>
 * Created by Leon on 20/04/2018
 */
@Component
public class GroupCommand extends AbstractCommand {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupValidator groupValidator;


    public GroupCommand() {
        super("group",
                "**!group <id or name>**"
                        + "\n -> Get information about a specific group.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!groupValidator.validateNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.GROUP_INVALID_ARGUMENTS).queue();
            return;
        }

        Group group;
        if(groupValidator.validateIfNumeric(arguments.get(0))) {
            Long id = Long.valueOf(arguments.get(0));
            group = groupService.findById(id);
        }else {
            group = groupService.findByName(arguments.get(0));
        }

        if(group == null) {
            messageChannel.sendMessage(CommandResponses.GROUP_NOT_FOUND).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(group.getName() + " (id: " + group.getId() + ")");
        embedBuilder.setDescription("------------------------------");
        embedBuilder.setColor(Color.decode("#D02F00"));

        String playerInfo;
        StringBuilder playersBuilder = new StringBuilder();

        boolean playersLabel = true;
        for(Player player : group.getPlayers()) {
            playerInfo = "\n**" + player.getNickname()
                    + "**, " + player.getAccountName()
                    + ", " + "*id:* " + player.getId()
                    + ", " + player.getDiscordId()
                    + "\n- *Roles:* " + player.rolesToString();

            playersBuilder.append(playerInfo).append("\n");

            if(playersBuilder.length() > 750) {
                embedBuilder.addField((playersLabel ? ("Players (" + group.getPlayers().size() + "):") : ""),
                        playersBuilder.toString(), false);
                playersLabel = false;
                playersBuilder.setLength(0);
            }
        }
        if(playersBuilder.length() > 0) {
            embedBuilder.addField((playersLabel ? ("Players (" + group.getPlayers().size() + "):") : ""),
                    playersBuilder.toString(), false);
        }
        messageChannel.sendMessage(embedBuilder.build()).queue();
    }

}
