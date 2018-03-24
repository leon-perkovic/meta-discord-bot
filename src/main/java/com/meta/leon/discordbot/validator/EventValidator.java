package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.command.CommandUtil;
import com.meta.leon.discordbot.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator for passed arguments
 *
 * @author Leon, created on 21/03/2018
 */
@Component
public class EventValidator extends GlobalValidator{

    @Autowired
    EventService eventService;

    @Autowired
    CommandUtil commandUtil;


    public boolean validateIfUniqueEvent(String name){
        if(eventService.findByName(name) == null){
            return true;
        }
        return false;
    }

}
