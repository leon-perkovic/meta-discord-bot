package com.meta.leon.discordbot.command.everyone;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;

/**
 * !roll
 * Command that rolls a number between 1 and 100
 *
 * Created by Leon on 02/04/2018
 */
@Component
public class RollCommand extends AbstractCommand{

    @Autowired
    GlobalValidator globalValidator;


    public RollCommand(){
        super("roll",
                "**!roll**"
                        + "\n -> Roll a number between 1 and 100.",
                "N/A",
                CommandAuthority.PUBLIC);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();
        User user = discordEvent.getAuthor();

        // validate passed arguments
        if(!globalValidator.validateNumberOfArguments(arguments, 0)){
            messageChannel.sendMessage(CommandResponses.ROLL_INVALID_ARGUMENTS).queue();
            return;
        }

        Random rng = new Random();

        messageChannel.sendMessage(user.getName() + " rolls: **" + (rng.nextInt(100) + 1) + "**").queue();
    }

}
