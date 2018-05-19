package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.service.EventSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator for passed arguments
 * <p>
 * Created by Leon on 01/04/2018
 */
@Component
public class EventSignupValidator extends GlobalValidator {

    @Autowired
    EventSignupService eventSignupService;

    public boolean validateIfUniqueSignup(Long eventId, Long playerId) {
        return eventSignupService.findEventSignup(eventId, playerId) == null;
    }

}
