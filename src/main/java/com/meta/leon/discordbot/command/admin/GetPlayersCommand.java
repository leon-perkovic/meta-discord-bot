package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.PlayerValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !getPlayers
 * Command for getting all player entries from a database
 *
 * Created by Leon on 18/03/2018
 */
@Component
public class GetPlayersCommand extends AbstractCommand{

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;


    public GetPlayersCommand(){
        super("getplayers", "Get all players from a database", "N/A", CommandAuthority.ADMIN);
    }

    @Override
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 0)){
            return new ResponseForm(CommandResponses.GET_PLAYERS_INVALID_ARGUMENTS);
        }

        List<Player> players = playerService.findAll();

        if(players.isEmpty()){
            return new ResponseForm(CommandResponses.GET_PLAYERS_NONE_FOUND);
        }

        StringBuilder playersInfo = new StringBuilder("");

        for(Player player : players){
            playersInfo.append("**")
                        .append(player.getNickname())
                        .append("**, ")
                        .append(player.getAccountName())
                        .append(", *id:* ")
                        .append(player.getId())
                        .append(", ")
                        .append(player.getDiscordId())
                        .append("\n")
                        .append(player.rolesToString())
                        .append("\n");
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("__Players:__");
        embedBuilder.setDescription(playersInfo.toString());
        embedBuilder.setColor(Color.decode("#D02F00"));

        return new ResponseForm(embedBuilder.build());
    }

}
