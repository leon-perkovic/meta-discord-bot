package com.meta.leon.discordbot.command.everyone;

import com.meta.leon.discordbot.BotListener;
import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * !help [command_name]
 * [command_name] is optional - if used, will return detailed information on specified command
 * Command used to get information on other commands
 *
 * Created by Leon on 02/04/2018
 */
@Component
public class HelpCommand extends AbstractCommand{

    @Autowired
    CommandContainer commandContainer;

    @Autowired
    GlobalValidator globalValidator;

    public HelpCommand(){
        super("help",
                "**!help [command_name]**"
                + "\n -> Get information about one specific or all commands.",
                "N/A",
                CommandAuthority.PUBLIC);
    }

    @Override
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // map commands in command container
        commandContainer.mapCommands();
        TreeMap<String, AbstractCommand> commands = commandContainer.getCommands();

        // get user roles and set authority level
        List<String> roleNames = BotListener.getUserRoles(user);
        CommandAuthority authority = BotListener.getUserAuthority(roleNames);

        if(arguments.size() == 1){
            if(commands.containsKey(arguments.get(0).toLowerCase())){
                AbstractCommand command = commands.get(arguments.get(0).toLowerCase());

                if(authority.getLevel() >= command.getAuthority().getLevel()){
                    return new ResponseForm(command.getDescription());

                }else{
                    return new ResponseForm(CommandResponses.NOT_AUTHORIZED);
                }
            }else{
                return new ResponseForm("I don't know any command called **" + arguments.get(0) + "** :cry:");
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("__Commands:__");
        embedBuilder.setDescription("**< ... > - Required\n[ ... ] - Optional**");
        embedBuilder.setColor(Color.decode("#D02F00"));

        for(String key : commands.keySet()){
            if(authority.getLevel() >= commands.get(key).getAuthority().getLevel()){

                embedBuilder.addField("", commands.get(key).getDescription(), false);
            }
        }
        return new ResponseForm(embedBuilder.build());
    }

}
