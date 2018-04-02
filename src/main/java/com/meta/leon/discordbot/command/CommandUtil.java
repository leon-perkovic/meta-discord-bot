package com.meta.leon.discordbot.command;

import com.meta.leon.discordbot.DiscordBotApp;
import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.EventSignupService;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.GlobalValidator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Utility class for common command operations
 *
 * Created by Leon on 20/03/2018
 */
@Component
public class CommandUtil{

    HashMap<String, Integer> days;

    @Autowired
    PlayerService playerService;

    @Autowired
    EventSignupService eventSignupService;

    @Autowired
    GlobalValidator globalValidator;


    public CommandUtil(){
        days = new HashMap<>();

        days.put("monday", 1);
        days.put("tuesday", 2);
        days.put("wednesday", 3);
        days.put("thursday", 4);
        days.put("friday", 5);
        days.put("saturday", 6);
        days.put("sunday", 7);
    }

    public Player findPlayerByAnyReference(ArrayList<String> arguments){

        Player player;

        // determine which reference was used for a player and get their ID
        if(globalValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));

            player = playerService.findById(id);

            if(player == null){
                return player;
            }

        }else if(globalValidator.validateIfDiscordId(arguments.get(0))){
            player = playerService.findByDiscordId(arguments.get(0).replace("!", ""));

            if(player == null){
                return player;
            }

        }else{
            player = playerService.findByNickname(arguments.get(0));
        }

        return player;
    }

    public DateTime getEventDateTime(String day, String time){

        DateTime now = new DateTime();
        int today = now.getDayOfWeek();
        int nowHours = now.getHourOfDay();
        int nowMinutes = now.getMinuteOfHour();
        int eventDay = days.get(day.toLowerCase());

        int hours = 0;
        int minutes = 0;

        if(time.contains(":")){
            hours = Integer.valueOf(time.split(":")[0]);
            minutes = Integer.valueOf(time.split(":")[1]);
        }else{
            hours = Integer.valueOf(time);
        }

        if(eventDay < today || (nowHours*60+nowMinutes) > (hours*60+minutes)){
            eventDay += 7;
        }
        eventDay = now.getDayOfMonth() + (eventDay-today);

        int eventMonth = now.getMonthOfYear();
        int eventYear = now.getYear();

        int endOfMonth = now.withDayOfMonth(1).plusMonths(1).minusDays(1).getDayOfMonth();
        if(eventDay > endOfMonth){
            eventMonth++;
        }
        if(eventMonth < now.getMonthOfYear()){
            eventYear++;
        }

        DateTime eventTime = new DateTime(eventYear, eventMonth, eventDay, hours, minutes, 0, 0);

        return eventTime;
    }

    public String createEventName(String day, String time){

        DateTime eventTime = getEventDateTime(day, time);

        String eventName = day.toLowerCase()
                + "-" + eventTime.getYearOfCentury()
                + "-" + eventTime.getMonthOfYear()
                + "-" + eventTime.getDayOfMonth()
                + "-" + eventTime.getHourOfDay()
                + "-" + eventTime.getMinuteOfHour();

        return eventName;
    }

    public String createEventBody(Event event){
        DateTimeZone timeZone = event.getEventTime().getZone();
        String zone = timeZone.getShortName(event.getEventTime().getMillis());

        String fieldValue = "*Description:*  **" + event.getDescription() + "**\n"
                + "*Time:*  **" + event.getEventTime().toString("dd/MM/yyyy - HH:mm") + " " + zone + "**\n"
                + "*Total players:*  **"
                + eventSignupService.getNumOfSignups(event.getId(), false)
                + "/" + event.getPlayerLimit() + "**\n";

        fieldValue += "*Members:*  **"
                    + eventSignupService.getNumOfSignupsByRank(event.getId(), DiscordBotApp.getMemberRole(), false)
                    + "/" + event.getMemberLimit() + "**\n";

        fieldValue += "*Trials:*  **"
                    + eventSignupService.getNumOfSignupsByRank(event.getId(), DiscordBotApp.getTrialRole(), false)
                    + "/" + event.getTrialLimit() + "**\n";

        fieldValue += "*Leader:*  **" + event.getEventLeader() + "**";

        return fieldValue;
    }

    public String convertDiscordMentionToId(String mention){
        mention = mention.replace("<", "");
        mention = mention.replace(">", "");
        mention = mention.replace("@", "");

        return mention;
    }

    // -- getters and setters -------------------------------------------------

    public HashMap<String, Integer> getDays(){
        return days;
    }
}
