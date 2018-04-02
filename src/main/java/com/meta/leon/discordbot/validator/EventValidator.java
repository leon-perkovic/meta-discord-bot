package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator for passed arguments
 *
 * Created by Leon on 21/03/2018
 */
@Component
public class EventValidator extends GlobalValidator{

    @Autowired
    EventService eventService;


    public boolean validateIfUniqueEvent(String name){
        if(eventService.findByName(name) == null){
            return true;
        }
        return false;
    }

}
