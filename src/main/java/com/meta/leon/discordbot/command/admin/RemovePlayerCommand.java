package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.PlayerValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removePlayer <id or nickname or @username>
 * Command for removing player entries from a database
 * <p>
 * Created by Leon on 18/03/2018
 */
@Component
public class RemovePlayerCommand extends AbstractCommand {

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;

    @Autowired
    CommandUtil commandUtil;


    public RemovePlayerCommand() {
        super("removeplayer",
                "**!removePlayer <id or nickname or @username>**"
                        + "\n -> Remove a specific player from the roster.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 1)) {
            messageChannel.sendMessage(CommandResponses.REMOVE_PLAYER_INVALID_ARGUMENTS).queue();
            return;
        }

        Player player = commandUtil.findPlayerByAnyReference(arguments.get(0));
        if(player == null) {
            messageChannel.sendMessage(CommandResponses.PLAYER_NOT_FOUND).queue();
        }

        playerService.removeById(player.getId());

        messageChannel.sendMessage(CommandResponses.REMOVE_PLAYER_SUCCESS).queue();
    }

}
