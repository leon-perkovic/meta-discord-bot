package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Group;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.model.PlayerGroup;
import com.meta.leon.discordbot.service.GroupService;
import com.meta.leon.discordbot.service.PlayerGroupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.util.CommandUtil;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !addPG <group_id or group_name> <player_id or player_nickname or @username>
 * Command for connecting players to a group
 * <p>
 * Created by Leon on 20/04/2018
 */
@Component
public class AddPgCommand extends AbstractCommand {

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

    public AddPgCommand() {
        super("addpg",
                "**!addPG <id or group_name> <player_id or player_nickname or @username ...>**"
                        + "\n -> Add players to a specific specific group.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 2)) {
            messageChannel.sendMessage(CommandResponses.ADD_PG_INVALID_ARGUMENTS).queue();
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
            PlayerGroup playerGroup = new PlayerGroup(playerId, groupId);
            playerGroupService.savePlayerGroup(playerGroup);
        }
        messageChannel.sendMessage("Successfully added players to group: **" + group.getName() + "** :white_check_mark:").queue();
    }

}
