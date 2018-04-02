package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.DpsReport;
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
 * !addDpsReport <id or event_name> <dps_report_links...>
 * Command for adding new dps report entries to a database
 *
 * Created by Leon on 02/04/2018
 */
@Component
public class AddDpsReportCommand extends AbstractCommand{

    private Long eventId;

    @Autowired
    DpsReportService dpsReportService;

    @Autowired
    EventService eventService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;


    public AddDpsReportCommand(){
        super("adddpsreport",
                "**!addDpsReport <id or event_name> <dps_report_links...>**"
                + "\n -> Add one or multiple dps reports for a specific event.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public ResponseForm execute(User user, ArrayList<String> arguments){

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 2)){
            return new ResponseForm(CommandResponses.ADD_DPS_REPORT_INVALID_ARGUMENTS);
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

        // format arguments
        String dpsArguments = "";

        for(int i=1; i<arguments.size(); i++){
            dpsArguments += arguments.get(i) + " ";
        }
        dpsArguments = dpsArguments.replaceAll("\n", " ");
        System.out.println(dpsArguments);

        // extract dps report links
        ArrayList<String> dpsReports = commandUtil.extractDpsReports(dpsArguments);

        if(dpsReports.isEmpty()){
            return new ResponseForm(CommandResponses.ADD_DPS_REPORT_INVALID_ARGUMENTS);
        }

        for(String dpsReportLink : dpsReports){
            DpsReport dpsReport = new DpsReport(dpsReportLink, eventId);

            dpsReportService.saveDpsReport(dpsReport);
        }

        return new ResponseForm(CommandResponses.ADD_DPS_REPORT_SUCCESS);
    }

}
