package com.meta.leon.discordbot.command.everyone;

import com.meta.leon.discordbot.BotListener;
import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandContainer;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * !help [command_name]
 * [command_name] is optional - if used, will return detailed information on specified command
 * Command used to get information on other commands
 * <p>
 * Created by Leon on 02/04/2018
 */
@Component
public class HelpCommand extends AbstractCommand {

    @Autowired
    CommandContainer commandContainer;

    @Autowired
    GlobalValidator globalValidator;

    public HelpCommand() {
        super("help",
                "**!help [command_name]**"
                        + "\n -> Get information about one specific or all commands.",
                "N/A",
                CommandAuthority.PUBLIC);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();
        User user = discordEvent.getAuthor();

        // map commands in command container
        commandContainer.mapCommands();
        LinkedHashMap<String, AbstractCommand> commands = commandContainer.getCommands();

        // get user roles and set authority level
        List<String> roleNames = BotListener.getUserRoles(user);
        CommandAuthority authority = BotListener.getUserAuthority(roleNames);

        if(arguments.size() == 1) {
            if(commands.containsKey(arguments.get(0).toLowerCase())) {
                AbstractCommand command = commands.get(arguments.get(0).toLowerCase());

                if(authority.getLevel() >= command.getAuthority().getLevel()) {
                    messageChannel.sendMessage(command.getDescription()).queue();
                    return;
                }else {
                    messageChannel.sendMessage(CommandResponses.NOT_AUTHORIZED).queue();
                    return;
                }
            }else {
                messageChannel.sendMessage("I don't know any command called **" + arguments.get(0) + "** :cry:");
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("__Commands:__");
        embedBuilder.setDescription("**< ... > - Required\n[ ... ] - Optional**");
        embedBuilder.setColor(Color.decode("#D02F00"));

        StringBuilder commandDesc = new StringBuilder();
        for(String key : commands.keySet()) {
            if(authority.getLevel() >= commands.get(key).getAuthority().getLevel()) {
                commandDesc.append(commands.get(key).getDescription()).append("\n");

                if(commandDesc.length() > 750) {
                    embedBuilder.addField("", commandDesc.toString(), false);
                    commandDesc.setLength(0);
                }
            }
        }
        if(commandDesc.length() > 0) {
            embedBuilder.addField("", commandDesc.toString(), false);
        }
        messageChannel.sendMessage(embedBuilder.build()).queue();
    }

}
