package com.meta.leon.discordbot.validator;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Global Validator for passed arguments
 *
 * @author Leon, created on 18/03/2018
 */
@Component
public class GlobalValidator{

    public static final String NUMBER_PATTERN = "^(0|[1-9][0-9]*)$";

    public static final String DISCORD_ID_PATTERN = "^<@(0|[1-9][0-9]*)>$";


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

        if(matcher.matches()){
            return true;
        }
        return false;
    }

    public boolean validateIfDiscordId(String argument){
        Pattern pattern = Pattern.compile(DISCORD_ID_PATTERN);
        Matcher matcher = pattern.matcher(argument);

        if(matcher.matches()){
            return true;
        }
        return false;
    }

}
