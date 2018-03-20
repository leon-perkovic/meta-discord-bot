package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.PlayerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !addPlayer <nickname> <account_name> [@username]
 * [@username] is optional
 * Command for adding new player entries to a database
 *
 * @author Leon, created on 17/03/2018
 */
@Component
public class AddPlayerCommand extends AbstractCommand{

    private String nickname;
    private String accountName;
    private String discordId;

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;


    public AddPlayerCommand(){
        super("addplayer", "Add new player to a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // if @username wasn't specified - add it as null
        if(arguments.size() == 2){
            arguments.add(null);
        }

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 3)){
            return new ResponseForm(CommandResponses.ADD_PLAYER_INVALID_ARGUMENTS);
        }
        if(arguments.get(2) != null){
            if(!playerValidator.validateIfDiscordId(arguments.get(2))){
                return new ResponseForm(CommandResponses.ADD_PLAYER_INVALID_DISCORD_ID);
            }
        }

        this.nickname = arguments.get(0);
        this.accountName = arguments.get(1);
        this.discordId = arguments.get(2);

        if(!playerValidator.validateIfUniquePlayer(nickname, accountName, discordId)){
            return new ResponseForm(CommandResponses.PLAYER_ALREADY_EXISTS);
        }

        Player player = new Player(nickname, accountName, discordId);
        playerService.savePlayer(player);

        return new ResponseForm("Successfully added player: **" + nickname + "** :white_check_mark:");
    }

}
