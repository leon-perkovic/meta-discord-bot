package com.meta.leon.discordbot.command;

import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Utility class for common command operations
 *
 * @author Leon, created on 20/03/2018
 */
@Component
public class CommandUtil{

    @Autowired
    PlayerService playerService;

    @Autowired
    GlobalValidator globalValidator;


    public Player findPlayerByAnyReference(ArrayList<String> arguments){

        Player player;

        // determine which reference was used for a player and get its ID
        if(globalValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));

            player = playerService.findById(id);

            if(player == null){
                return player;
            }

        }else if(globalValidator.validateIfDiscordId(arguments.get(0))){
            player = playerService.findByDiscordId(arguments.get(0));

            if(player == null){
                return player;
            }

        }else{
            player = playerService.findByNickname(arguments.get(0));
        }

        return player;
    }

}
