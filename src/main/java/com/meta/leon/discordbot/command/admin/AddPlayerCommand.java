package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.CommandValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !addPlayer [nickname] [account_name]
 * Command for adding new player entries to a database
 *
 * @author Leon, created on 17/03/2018
 */
@Component
public class AddPlayerCommand extends AbstractCommand{

    private String nickname;
    private String accountName;

    @Autowired
    PlayerService playerService;

    @Autowired
    CommandValidator commandValidator;


    public AddPlayerCommand(){
        super("addplayer", "Add new player to a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!commandValidator.validateNumberOfArguments(arguments, 2)){
            return new ResponseForm(CommandResponses.ADD_PLAYER_INVALID_ARGUMENTS);
        }

        this.nickname = arguments.get(0);
        this.accountName = arguments.get(1);

        if(!commandValidator.validateIfUniquePlayer(nickname, accountName)){
            return new ResponseForm(CommandResponses.PLAYER_ALREADY_EXISTS);
        }

        Player player = new Player(nickname, accountName);
        playerService.savePlayer(player);

        return new ResponseForm("Player **" + nickname + "** added successfully :white_check_mark:");
    }

}
