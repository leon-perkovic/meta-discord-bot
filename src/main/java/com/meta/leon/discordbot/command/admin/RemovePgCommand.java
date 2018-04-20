package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.model.Group;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.GroupService;
import com.meta.leon.discordbot.service.PlayerGroupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removePG <id or group_name> <player_id or player_nickname or @username ...>
 * Command for removing players from a group
 * <p>
 * Created by Leon on 20/04/2018
 */
@Component
public class RemovePgCommand extends AbstractCommand {

    @Autowired
    PlayerGroupService playerGroupService;

    @Autowired
    PlayerService playerService;

    @Autowired
    GroupService groupService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;

    public RemovePgCommand() {
        super("removepg",
                "**!removePG <id or group_name> <player_id or player_nickname or @username ...>**"
                        + "\n -> Remove players from a specific specific group.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 2)) {
            messageChannel.sendMessage(CommandResponses.REMOVE_PG_INVALID_ARGUMENTS).queue();
            return;
        }

        Group group;
        if(globalValidator.validateIfNumeric(arguments.get(0))) {
            Long groupId = Long.valueOf(arguments.get(0));
            group = groupService.findById(groupId);
        }else {
            group = groupService.findByName(arguments.get(0));
        }

        if(group == null) {
            messageChannel.sendMessage(CommandResponses.GROUP_NOT_FOUND).queue();
            return;
        }
        arguments.remove(0);

        Long groupId = group.getId();
        ArrayList<Long> playerIds = new ArrayList<>();

        for(String playerReference : arguments) {
            Player player = commandUtil.findPlayerByAnyReference(playerReference);

            if(player == null) {
                messageChannel.sendMessage(CommandResponses.PLAYER_NOT_FOUND).queue();
                return;
            }
            playerIds.add(player.getId());
        }

        for(Long playerId : playerIds) {
            playerGroupService.removePlayerGroup(playerId, groupId);
        }

        messageChannel.sendMessage("Successfully removed players from group: **" + group.getName() + "** :white_check_mark:").queue();
    }

}
