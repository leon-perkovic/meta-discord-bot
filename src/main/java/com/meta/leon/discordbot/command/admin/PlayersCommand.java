package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.PlayerValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
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
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 0)){
            messageChannel.sendMessage(CommandResponses.PLAYERS_INVALID_ARGUMENTS).queue();
            return;
        }

        List<Player> players = playerService.findAll();
        if(players.isEmpty()){
            messageChannel.sendMessage(CommandResponses.PLAYERS_NONE_FOUND).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("__Players (" + players.size() + "):__");
        embedBuilder.setColor(Color.decode("#D02F00"));

        String playerInfo;
        StringBuilder playersBuilder = new StringBuilder();

        for(Player player : players){
            playerInfo = "\n**" + player.getNickname()
                    + "**, " + player.getAccountName()
                    + ", " + "*id:* " + player.getId()
                    + ", " + player.getDiscordId() + "\n"
                    + player.rolesToString();

            playersBuilder.append(playerInfo).append("\n");

            if(playersBuilder.length() > 750){
                embedBuilder.addField("", playersBuilder.toString(), false);
                playersBuilder.setLength(0);
            }
        }
        if(playersBuilder.length() > 0){
            embedBuilder.addField("", playersBuilder.toString(), false);
        }
        messageChannel.sendMessage(embedBuilder.build()).queue();
    }

}
