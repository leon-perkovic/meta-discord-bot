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
import java.util.List;

/**
 * !getPlayers
 * Command for getting all player entries from a database
 *
 * @author Leon, created on 18/03/2018
 */
@Component
public class GetPlayersCommand extends AbstractCommand{

    @Autowired
    PlayerService playerService;

    @Autowired
    CommandValidator commandValidator;


    public GetPlayersCommand(){
        super("getplayers", "Get all players from a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    public ResponseForm execute(ArrayList<String> arguments){

        // validate passed arguments
        if(!commandValidator.validateNumberOfArguments(arguments, 0)){
            return new ResponseForm(CommandResponses.GET_PLAYERS_INVALID_ARGUMENTS);
        }

        List<Player> players = playerService.findAll();

        if(!players.isEmpty()){
            String playerFields = "";

            for(Player player : players){
                playerFields += player.toStringForEmbed() + "\n";
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("__Player info:__");
            embedBuilder.setColor(Color.decode("#D02F00"));
            embedBuilder.setDescription(playerFields);

            return new ResponseForm(embedBuilder.build());
        }
        return new ResponseForm(CommandResponses.GET_PLAYERS_NONE_FOUND);
    }

}
