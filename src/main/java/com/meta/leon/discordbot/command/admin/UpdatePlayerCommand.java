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
 * !updatePlayer [id] [nickname] [account_name]
 * Command for updating a player entries in a database
 *
 * @author Leon, created on 18/03/2018
 */
@Component
public class UpdatePlayerCommand extends AbstractCommand{

    private Long id;
    private String nickname;
    private String accountName;

    @Autowired
    PlayerService playerService;

    @Autowired
    CommandValidator commandValidator;


    public UpdatePlayerCommand(){
        super("updateplayer", "Update player info in a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!commandValidator.validateNumberOfArguments(arguments, 3)){
            return new ResponseForm(CommandResponses.UPDATE_PLAYER_INVALID_ARGUMENTS);
        }
        if(commandValidator.validateIfNumeric(arguments.get(0))){
            this.id = Long.valueOf(arguments.get(0));
            this.nickname = arguments.get(1);
            this.accountName = arguments.get(2);

            if(!commandValidator.validateIfUniquePlayer(nickname, accountName)){
                return new ResponseForm(CommandResponses.UPDATE_PLAYER_ALREADY_TAKEN);
            }

            Player player = playerService.findById(id);
            if(player == null){
                return new ResponseForm(CommandResponses.PLAYER_NOT_FOUND);
            }
            player.setNickname(nickname);
            player.setAccountName(accountName);

            playerService.savePlayer(player);

            return new ResponseForm("Player with *id:* **" + id + "** updated successfully :white_check_mark:");
        }
        return new ResponseForm(CommandResponses.UPDATE_PLAYER_INVALID_ID);
    }
}
