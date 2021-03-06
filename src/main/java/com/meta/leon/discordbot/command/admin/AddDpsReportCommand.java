package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.DpsReport;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.DpsReportService;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.util.CommandUtil;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !addDpsReport <id or event_name> <dps_report_links...>
 * Command for adding new dps report entries to a database
 * <p>
 * Created by Leon on 02/04/2018
 */
@Component
public class AddDpsReportCommand extends AbstractCommand {

    @Autowired
    DpsReportService dpsReportService;

    @Autowired
    EventService eventService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;

    public AddDpsReportCommand() {
        super("adddpsreport",
                "**!addDpsReport <id or event_name> <dps_report_links...>**"
                        + "\n -> Add one or multiple dps reports for a specific event.",
                "N/A",
                CommandAuthority.EVENT_LEADER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!globalValidator.validateMinNumberOfArguments(arguments, 2)) {
            messageChannel.sendMessage(CommandResponses.ADD_DPS_REPORT_INVALID_ARGUMENTS).queue();
            return;
        }

        Event event;
        Long eventId;
        // check if event exists
        if(globalValidator.validateIfNumeric(arguments.get(0))) {
            eventId = Long.valueOf(arguments.get(0));
            event = eventService.findById(eventId);
            if(event == null) {
                messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
                return;
            }
        }else {
            event = eventService.findByName(arguments.get(0));
            if(event == null) {
                messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
                return;
            }
            eventId = event.getId();
        }

        // format arguments
        StringBuilder dpsArguments = new StringBuilder();
        for(int i = 1; i < arguments.size(); i++) {
            dpsArguments.append(arguments.get(i)).append(" ");
        }
        String formattedArgs = dpsArguments.toString().replaceAll("\n", " ");

        // extract dps report links
        ArrayList<String> dpsReports = commandUtil.extractDpsReports(formattedArgs);
        if(dpsReports.isEmpty()) {
            messageChannel.sendMessage(CommandResponses.ADD_DPS_REPORT_INVALID_ARGUMENTS).queue();
            return;
        }

        for(String dpsReportLink : dpsReports) {
            DpsReport dpsReport = new DpsReport(dpsReportLink, eventId);

            dpsReportService.saveDpsReport(dpsReport);
        }
        messageChannel.sendMessage(CommandResponses.ADD_DPS_REPORT_SUCCESS).queue();
    }

}
