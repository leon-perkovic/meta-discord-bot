package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.PlayerValidator;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removePlayer <id or nickname or @username>
 * Command for removing player entries from a database
 *
 * Created by Leon on 18/03/2018
 */
@Component
public class RemovePlayerCommand extends AbstractCommand{

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;


    public RemovePlayerCommand(){
        super("removeplayer",
                "**!removePlayer <id or nickname or @username>**"
                + "\n -> Remove a specific player from the roster.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.REMOVE_PLAYER_INVALID_ARGUMENTS);
        }

        int numOfRemoved;
        if(playerValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            numOfRemoved = playerService.removeById(id);

        }else if(playerValidator.validateIfDiscordId(arguments.get(0))){
            numOfRemoved = playerService.removeByDiscordId(arguments.get(0).replace("!", ""));

        }else{
            numOfRemoved = playerService.removeByNickname(arguments.get(0));
        }

        if(numOfRemoved > 0){
            return new ResponseForm(CommandResponses.REMOVE_PLAYER_SUCCESS);
        }
        return new ResponseForm(CommandResponses.PLAYER_NOT_FOUND);
    }

}
