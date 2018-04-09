package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
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

/**
 * !player <id or nickname or @username>
 * Command for getting a player entry from a database
 *
 * Created by Leon on 18/03/2018
 */
@Component
public class PlayerCommand extends AbstractCommand{

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;


    public PlayerCommand(){
        super("player",
                "**!player <id or nickname or @username>**"
                + "\n -> Get information about a specific player.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 1)){
            messageChannel.sendMessage(CommandResponses.PLAYER_INVALID_ARGUMENTS).queue();
            return;
        }

        Player player;
        if(playerValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            player = playerService.findById(id);

        }else if(playerValidator.validateIfDiscordId(arguments.get(0))){
            player = playerService.findByDiscordId(arguments.get(0).replace("!", ""));

        }else{
            player = playerService.findByNickname(arguments.get(0));
        }

        if(player != null){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("__Player info:__");
            embedBuilder.setDescription("**" + player.getNickname()
                            + "**, " + player.getAccountName()
                            + ", " + "*id:* " + player.getId()
                            + ", " + player.getDiscordId() + "\n"
                            + player.rolesToString());
            embedBuilder.setColor(Color.decode("#D02F00"));

            messageChannel.sendMessage(embedBuilder.build()).queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.PLAYER_NOT_FOUND).queue();
    }

}
