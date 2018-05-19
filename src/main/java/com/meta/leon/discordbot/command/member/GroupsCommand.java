package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Group;
import com.meta.leon.discordbot.service.GroupService;
import com.meta.leon.discordbot.validator.GroupValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !groups
 * Command for getting information about all groups
 * <p>
 * Created by Leon on 20/04/2018
 */
@Component
public class GroupsCommand extends AbstractCommand {

    @Autowired
    GroupService groupService;

    @Autowired
    GroupValidator groupValidator;

    public GroupsCommand() {
        super("groups",
                "**!groups**"
                        + "\n -> Get information about all groups.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!groupValidator.validateNumberOfArguments(arguments, 0)) {
            messageChannel.sendMessage(CommandResponses.GROUPS_INVALID_ARGUMENTS).queue();
            return;
        }

        List<Group> groups = groupService.findAll();
        if(groups.isEmpty()) {
            messageChannel.sendMessage(CommandResponses.GROUPS_NONE_FOUND).queue();
            return;
        }

        StringBuilder groupsInfo = new StringBuilder("");
        for(Group group : groups) {
            groupsInfo.append("**")
                    .append(group.getName())
                    .append("** (*id:* ")
                    .append(group.getId())
                    .append(")\n\n");
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("__Groups:__");
        embedBuilder.setDescription(groupsInfo.toString());
        embedBuilder.setColor(Color.decode("#D02F00"));

        messageChannel.sendMessage(embedBuilder.build()).queue();
    }

}
