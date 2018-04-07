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
 * !players
 * Command for getting all player entries from a database
 *
 * Created by Leon on 18/03/2018
 */
@Component
public class PlayersCommand extends AbstractCommand{

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;


    public PlayersCommand(){
        super("players",
                "**!players**"
                + "\n -> Get information about all players.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 0)){
            return new ResponseForm(CommandResponses.PLAYERS_INVALID_ARGUMENTS);
        }

        List<Player> players = playerService.findAll();

        if(players.isEmpty()){
            return new ResponseForm(CommandResponses.PLAYERS_NONE_FOUND);
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("__Players:__");
        embedBuilder.setColor(Color.decode("#D02F00"));

        for(Player player : players){
            String fieldValue = "**" + player.getNickname()
                        + "**, " + player.getAccountName()
                        + ", " + "*id:* " + player.getId()
                        + ", " + player.getDiscordId() + "\n"
                        + player.rolesToString();

            embedBuilder.addField("", fieldValue, false);
        }


        return new ResponseForm(embedBuilder.build());
    }

}
