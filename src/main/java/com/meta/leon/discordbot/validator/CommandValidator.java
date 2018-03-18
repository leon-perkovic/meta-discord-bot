package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for passed arguments
 *
 * @author Leon, created on 18/03/2018
 */
@Component
public class CommandValidator{

    public static final String NUMBER_PATTERN = "^(0|[1-9][0-9]*)$";

    @Autowired
    PlayerService playerService;


    public boolean validateNumberOfArguments(ArrayList<String> arguments, int expectedArguments){
        if(arguments.size() == expectedArguments){
            return true;
        }
        return false;
    }

    public boolean validateIfUniquePlayer(String nickname, String accountName){
        if(playerService.findByNickname(nickname) == null &&
                playerService.findByAccountName(accountName) == null){
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

}
