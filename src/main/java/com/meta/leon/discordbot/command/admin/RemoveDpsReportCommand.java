package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.DpsReportService;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removeDpsReport <id or event_name>
 * Command for removing dps report entries from a database
 *
 * Created by Leon on 02/04/2018
 */
@Component
public class RemoveDpsReportCommand extends AbstractCommand{

    private Long eventId;

    @Autowired
    DpsReportService dpsReportService;

    @Autowired
    EventService eventService;

    @Autowired
    GlobalValidator globalValidator;


    public RemoveDpsReportCommand(){
        super("removedpsreport",
                "**!removeDpsReport <id or event_name>**"
                + "\n -> Delete dps reports for a specific event.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!globalValidator.validateNumberOfArguments(arguments, 1)){
            return new ResponseForm(CommandResponses.REMOVE_DPS_REPORT_INVALID_ARGUMENTS);
        }

        Event event;

        // check if event exists
        if(globalValidator.validateIfNumeric(arguments.get(0))){
            this.eventId = Long.valueOf(arguments.get(0));

            event = eventService.findById(eventId);

            if(event == null){
                return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
            }

        }else{
            event = eventService.findByName(arguments.get(0));

            if(event == null){
                return new ResponseForm(CommandResponses.EVENT_NOT_FOUND);
            }
            this.eventId = event.getId();
        }

        dpsReportService.removeAllByEventId(eventId);

        return new ResponseForm(CommandResponses.REMOVE_DPS_REPORT_SUCCESS);
    }

}
