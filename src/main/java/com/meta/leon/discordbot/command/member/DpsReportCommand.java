package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.command.*;
import com.meta.leon.discordbot.model.DpsReport;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.service.DpsReportService;
import com.meta.leon.discordbot.service.EventService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * !dpsReport <id or event_name>
 * Command for getting dps report entries from a database
 *
 * Created by Leon on 02/04/2018
 */
@Component
public class DpsReportCommand extends AbstractCommand{

    private Long eventId;

    @Autowired
    DpsReportService dpsReportService;

    @Autowired
    EventService eventService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;


    public DpsReportCommand(){
        super("dpsreport",
                "**!dpsReport <id or event_name>**"
                + "\n -> Get dps reports for a specific event.",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!globalValidator.validateNumberOfArguments(arguments, 1)){
            messageChannel.sendMessage(CommandResponses.DPS_REPORT_INVALID_ARGUMENTS).queue();
            return;
        }

        Event event;
        // check if event exists
        if(globalValidator.validateIfNumeric(arguments.get(0))){
            this.eventId = Long.valueOf(arguments.get(0));

            event = eventService.findById(eventId);
            if(event == null){
                messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
                return;
            }

        }else{
            event = eventService.findByName(arguments.get(0));
            if(event == null){
                messageChannel.sendMessage(CommandResponses.EVENT_NOT_FOUND).queue();
                return;
            }
            this.eventId = event.getId();
        }

        List<DpsReport> dpsReports = dpsReportService.findAllByEventId(eventId);

        if(dpsReports.isEmpty()){
            messageChannel.sendMessage(CommandResponses.DPS_REPORTS_NONE_FOUND).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("__DPS reports:__");
        embedBuilder.setColor(Color.decode("#D02F00"));

        String fieldValue = commandUtil.createEventBody(event);
        fieldValue += "\n------------------------------";

        StringBuilder reports = new StringBuilder("");
        for(DpsReport dpsReport: dpsReports){
            reports.append("\n")
                    .append(dpsReport.getLink());
        }

        embedBuilder.addField(event.getName() + " (id: " + event.getId() + ")", fieldValue + reports, false);

        messageChannel.sendMessage(embedBuilder.build()).queue();
    }

}
