package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.CommandValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removePlayer [id or nickname]
 * Command for removing player entries from a database
 *
 * @author Leon, created on 18/03/2018
 */
@Component
public class RemovePlayerCommand extends AbstractCommand{

    @Autowired
    PlayerService playerService;

    @Autowired
    CommandValidator commandValidator;


    public RemovePlayerCommand(){
        super("removeplayer", "Remove a player from a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!commandValidator.validateNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.REMOVE_PLAYER_INVALID_ARGUMENTS);
        }

        int numOfRemoved = 0;
        if(commandValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            numOfRemoved = playerService.removePlayerById(id);

        }else{
            numOfRemoved = playerService.removePlayerByNickname(arguments.get(0));
        }

        if(numOfRemoved > 0){
            return new ResponseForm(CommandResponses.REMOVE_PLAYER_SUCCESS);
        }
        return new ResponseForm(CommandResponses.PLAYER_NOT_FOUND);
    }

}
