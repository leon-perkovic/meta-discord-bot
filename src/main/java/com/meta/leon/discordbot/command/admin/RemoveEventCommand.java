package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.EventValidator;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removeEvent <id or name or day> [HH:mm]
 * [HH:mm] is optional, only needed in combination with <day>
 * Command for removing event entries from a database
 * Event name will be determined and set automatically for first upcoming day if only day was specified
 *
 * Created by Leon on 22/03/2018
 */
@Component
public class RemoveEventCommand extends AbstractCommand{

    @Autowired
    EventService eventService;

    @Autowired
    EventValidator eventValidator;

    @Autowired
    CommandUtil commandUtil;


    public RemoveEventCommand(){
        super("removeevent",
                "**!removeEvent <id or name or day> [HH:mm]**"
                + "\n -> Delete/cancel a specific event",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!eventValidator.validateMinNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.REMOVE_EVENT_INVALID_ARGUMENTS);
        }
        if(arguments.size() == 2){
            if(!eventValidator.validateIfTime(arguments.get(1))){
                return new ResponseForm(CommandResponses.REMOVE_EVENT_INVALID_ARGUMENTS);
            }
        }

        int numOfRemoved;

        if(eventValidator.validateIfNumeric(arguments.get(0))){
            numOfRemoved = eventService.removeById(Long.valueOf(arguments.get(0)));

        }else if(eventValidator.validateIfDay(arguments.get(0)) && arguments.size() == 2){
            String name = commandUtil.createEventName(arguments.get(0), arguments.get(1));

            numOfRemoved = eventService.removeByName(name);
        }else{
            numOfRemoved = eventService.removeByName(arguments.get(0));
        }

        if(numOfRemoved > 0){
            return new ResponseForm(CommandResponses.REMOVE_EVENT_SUCCESS);
        }
        return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
    }

}
