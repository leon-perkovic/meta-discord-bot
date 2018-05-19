package com.meta.leon.discordbot.command.everyone;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * !gg
 * <p>
 * Created by Leon on 11/04/2018
 */
@Component
public class GgCommand extends AbstractCommand {

    @Autowired
    GlobalValidator globalValidator;

    public GgCommand() {
        super("gg",
                "**!gg**"
                        + "\n -> Can someone GG please?",
                "N/A",
                CommandAuthority.PUBLIC);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!globalValidator.validateNumberOfArguments(arguments, 0)) {
            messageChannel.sendMessage(CommandResponses.GG_INVALID_ARGUMENTS).queue();
            return;
        }
        messageChannel.sendMessage("***Cuddles you on the floor***  **<(^_^ <)**").queue();
    }


}
