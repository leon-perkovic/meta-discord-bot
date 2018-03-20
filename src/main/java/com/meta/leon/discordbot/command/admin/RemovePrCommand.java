package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.PlayerRoleService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removePR <id or nickname or @username> <role_name or short_name ...>
 * Multiple <role_name or short_name> arguments can be passed
 * Command for removing a role from a player
 *
 * @author Leon, created on 20/03/2018
 */
@Component
public class RemovePrCommand extends AbstractCommand{

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


    public RemovePrCommand(){
        super("removepr", "Remove a role from a player", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 2)){
            return new ResponseForm(CommandResponses.ADD_PR_INVALID_ARGUMENTS);
        }

        Player player = commandUtil.findPlayerByAnyReference(arguments);

        if(player == null){
            return new ResponseForm(CommandResponses.PLAYER_NOT_FOUND);
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
                return new ResponseForm(CommandResponses.ROLE_NOT_FOUND);
            }
            roleIds.add(role.getId());
        }

        for(Long roleId : roleIds){
            playerRoleService.removePlayerRole(playerId, roleId);
        }
        return new ResponseForm("Successfully removed roles for player: **" + player.getNickname() + "** :white_check_mark:");
    }

}
