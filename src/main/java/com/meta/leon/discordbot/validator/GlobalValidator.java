package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.command.CommandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Global Validator for passed arguments
 *
 * Created by Leon on 18/03/2018
 */
@Component
public class GlobalValidator{

    public static final String NUMBER_PATTERN = "^(0|[1-9][0-9]*)$";

    public static final String TIME_PATTERN = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

    public static final String DISCORD_ID_PATTERN = "^<@(0|[1-9][0-9]*)>$";

    @Autowired
    CommandUtil commandUtil;


    public boolean validateNumberOfArguments(ArrayList<String> arguments, int expectedArguments){
        if(arguments.size() == expectedArguments){
            return true;
        }
        return false;
    }

    public boolean validateMinNumberOfArguments(ArrayList<String> arguments, int expectedMinArguments){
        if(arguments.size() >= expectedMinArguments){
            return true;
        }
        return false;
    }

    public boolean validateIfNumeric(String argument){
        Pattern pattern = Pattern.compile(NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(argument);

        return matcher.matches();
    }

    public boolean validateIfDiscordId(String argument){
        argument = argument.replace("!", "");

        Pattern pattern = Pattern.compile(DISCORD_ID_PATTERN);
        Matcher matcher = pattern.matcher(argument);

        return matcher.matches();
    }

    public boolean validateIfDay(String argument){
        if(commandUtil.getDays().containsKey(argument.toLowerCase())){
            return true;
        }
        return false;
    }

    public boolean validateIfTime(String argument){
        Pattern pattern = Pattern.compile(TIME_PATTERN);
        Matcher matcher = pattern.matcher(argument);

        if(validateIfNumeric(argument)){
            int time = Integer.valueOf(argument);

            if(time >= 0 && time <= 23){
                return true;
            }
        }
        return matcher.matches();
    }

}
