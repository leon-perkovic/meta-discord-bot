package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.model.PlayerRole;
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
 * !addPR <id or nickname or @username> <role_name or short_name ...>
 * Multiple <role_name or short_name> arguments can be passed
 * Command for connecting a role to a player
 *
 * Created by Leon on 19/03/2018
 */
@Component
public class AddPrCommand extends AbstractCommand{

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


    public AddPrCommand(){
        super("addpr",
                "**!addPR <id or nickname or @username> <role_name or short_name ...>**"
                + "\n -> Add roles for a specific player.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 2)){
            messageChannel.sendMessage(CommandResponses.ADD_PR_INVALID_ARGUMENTS).queue();
            return;
        }

        Player player = commandUtil.findPlayerByAnyReference(arguments);
        if(player == null){
            messageChannel.sendMessage(CommandResponses.PLAYER_NOT_FOUND).queue();
            return;
        }

        Long playerId = player.getId();
        // remove player reference (first argument)
        arguments.remove(0);

        // create a list for role IDs
        ArrayList<Long> roleIds = new ArrayList<>();

        // check if all passed roles exist and get their IDs
        for(String roleName : arguments){
            Role role = roleService.findByRoleName(roleName);
            if(role == null){
                role = roleService.findByShortName(roleName);
            }

            if(role == null){
                messageChannel.sendMessage(CommandResponses.ROLE_NOT_FOUND).queue();
                return;
            }
            roleIds.add(role.getId());
        }

        for(Long roleId : roleIds){
            PlayerRole playerRole = new PlayerRole(playerId, roleId);
            playerRoleService.savePlayerRole(playerRole);
        }
        messageChannel.sendMessage("Successfully added roles for player: **" + player.getNickname() + "** :white_check_mark:").queue();
    }

}
