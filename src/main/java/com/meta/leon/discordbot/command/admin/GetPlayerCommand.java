package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.CommandValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;

/**
 * !getPlayer [id or nickname]
 * Command for getting a player entry from a database
 *
 * @author Leon, created on 18/03/2018
 */
@Component
public class GetPlayerCommand extends AbstractCommand{

    @Autowired
    PlayerService playerService;

    @Autowired
    CommandValidator commandValidator;


    public GetPlayerCommand(){
        super("getplayer", "Get a player from a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!commandValidator.validateNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.GET_PLAYER_INVALID_ARGUMENTS);
        }

        Player player;
        if(commandValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            player = playerService.findById(id);

        }else{
            player = playerService.findByNickname(arguments.get(0));
        }

        if(player != null){
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("__Player info:__");
            embedBuilder.setColor(Color.decode("#D02F00"));
            embedBuilder.setDescription(player.toStringForEmbed());

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.PLAYER_NOT_FOUND);
    }

}
