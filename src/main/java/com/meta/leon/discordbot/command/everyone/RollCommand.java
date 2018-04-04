package com.meta.leon.discordbot.command.everyone;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.User;
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
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!globalValidator.validateNumberOfArguments(arguments, 0)){
            return new ResponseForm(CommandResponses.ROLL_INVALID_ARGUMENTS);
        }

        Random rng = new Random();

        return new ResponseForm(user.getName() + " rolls: **" + (rng.nextInt(100) + 1) + "**");
    }

}
