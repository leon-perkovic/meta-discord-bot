package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.PlayerRoleService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removePR <id or nickname or @username> <role_name or short_name ...>
 * Multiple <role_name or short_name> arguments can be passed
 * Command for removing a role from a player
 * <p>
 * Created by Leon on 20/03/2018
 */
@Component
public class RemovePrCommand extends AbstractCommand {

    @Autowired
    PlayerRoleService playerRoleService;

    @Autowired
    PlayerService playerService;

    @Autowired
    RoleService roleService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;


    public RemovePrCommand() {
        super("removepr",
                "**!removePR <id or nickname or @username> <role_name or short_name ...>**"
                        + "\n -> Remove roles for a specific player.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 2)) {
            messageChannel.sendMessage(CommandResponses.REMOVE_PR_INVALID_ARGUMENTS).queue();
            return;
        }

        Player player = commandUtil.findPlayerByAnyReference(arguments.get(0));
        if(player == null) {
            messageChannel.sendMessage(CommandResponses.PLAYER_NOT_FOUND).queue();
            return;
        }

        Long playerId = player.getId();
        // remove player reference (first argument)
        arguments.remove(0);

        // create a list for role IDs
        ArrayList<Long> roleIds = new ArrayList<>();

        // check if all passed roles exist and get their IDs
        for(String roleName : arguments) {
            Role role = roleService.findByRoleName(roleName);
            if(role == null) {
                role = roleService.findByShortName(roleName);
            }

            if(role == null) {
                messageChannel.sendMessage(CommandResponses.ROLE_NOT_FOUND).queue();
                return;
            }
            roleIds.add(role.getId());
        }

        for(Long roleId : roleIds) {
            playerRoleService.removePlayerRole(playerId, roleId);
        }
        messageChannel.sendMessage("Successfully removed roles for player: **" + player.getNickname() + "** :white_check_mark:").queue();
    }

}
