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
 * !updatePlayer <id> <nickname> <account_name> [@username]
 * [@username] is optional
 * Command for updating player entries in a database
 *
 * @author Leon, created on 18/03/2018
 */
@Component
public class UpdatePlayerCommand extends AbstractCommand{

    private Long id;
    private String nickname;
    private String accountName;
    private String discordId;

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;


    public UpdatePlayerCommand(){
        super("updateplayer", "Update player info in a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // if @username wasn't specified - add it as null
        if(arguments.size() == 3){
            arguments.add(null);
        }

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 4)){
            return new ResponseForm(CommandResponses.UPDATE_PLAYER_INVALID_ARGUMENTS);
        }
        if(arguments.get(3) != null){
            if(!playerValidator.validateIfDiscordId(arguments.get(3))){
                return new ResponseForm(CommandResponses.UPDATE_PLAYER_INVALID_DISCORD_ID);
            }
        }

        if(playerValidator.validateIfNumeric(arguments.get(0))){
            this.id = Long.valueOf(arguments.get(0));
            this.nickname = arguments.get(1);
            this.accountName = arguments.get(2);
            this.discordId = arguments.get(3);

            Player player = playerService.findById(id);
            if(player == null){
                return new ResponseForm(CommandResponses.PLAYER_NOT_FOUND);
            }

            if(!playerValidator.validateIfUniquePlayerUpdate(id, nickname, accountName, discordId)){
                return new ResponseForm(CommandResponses.UPDATE_PLAYER_ALREADY_TAKEN);
            }

            player.setNickname(nickname);
            player.setAccountName(accountName);
            player.setDiscordId(discordId);

            playerService.savePlayer(player);

            return new ResponseForm("Successfully updated player with *id:* **" + id + "** :white_check_mark:");
        }
        return new ResponseForm(CommandResponses.UPDATE_PLAYER_INVALID_ID);
    }
}
