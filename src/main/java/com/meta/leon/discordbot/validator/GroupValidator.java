package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Leon on 20/04/2018
 */
@Component
public class GroupValidator extends GlobalValidator {

    @Autowired
    GroupService groupService;

    public boolean validateIfUniqueGroup(String name) {
        return groupService.findByName(name) == null;
    }

}
