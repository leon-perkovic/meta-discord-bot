package com.meta.leon.discordbot.command;

import com.meta.leon.discordbot.command.admin.*;
import com.meta.leon.discordbot.command.member.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Container for all autowired commands
 *
 * Created by Leon on 17/03/2018
 */
@Component
public class CommandContainer{

    private HashMap<String, AbstractCommand> commands;

    @Autowired
    private AddPlayerCommand addPlayerCommand;

    @Autowired
    private RemovePlayerCommand removePlayerCommand;

    @Autowired
    private GetPlayerCommand getPlayerCommand;

    @Autowired
    private GetPlayersCommand getPlayersCommand;

    @Autowired
    private UpdatePlayerCommand updatePlayerCommand;

    @Autowired
    private AddRoleCommand addRoleCommand;

    @Autowired
    private RemoveRoleCommand removeRoleCommand;

    @Autowired
    private GetRoleCommand getRoleCommand;

    @Autowired
    private GetRolesCommand getRolesCommand;

    @Autowired
    private UpdateRoleCommand updateRoleCommand;

    @Autowired
    private AddPrCommand addPrCommand;

    @Autowired
    private RemovePrCommand removePrCommand;

    @Autowired
    private AddEventCommand addEventCommand;

    @Autowired
    private RemoveEventCommand removeEventCommand;

    @Autowired
    private GetEventCommand getEventCommand;

    @Autowired
    private GetEventsCommand getEventsCommand;

    @Autowired
    private GetPastEventsCommand getPastEventsCommand;

    @Autowired
    private EventDetailCommand eventDetailCommand;

    @Autowired
    private SignupCommand signupCommand;

    @Autowired
    private DropoutCommand dropoutCommand;


    // Used to map all autowired commands to their key values
    public void mapCommands(){

        commands = new HashMap<>();

        commands.put("addplayer", addPlayerCommand);
        commands.put("removeplayer", removePlayerCommand);
        commands.put("getplayer", getPlayerCommand);
        commands.put("getplayers", getPlayersCommand);
        commands.put("updateplayer", updatePlayerCommand);
        commands.put("addrole", addRoleCommand);
        commands.put("removerole", removeRoleCommand);
        commands.put("getrole", getRoleCommand);
        commands.put("getroles", getRolesCommand);
        commands.put("updaterole", updateRoleCommand);
        commands.put("addpr", addPrCommand);
        commands.put("removepr", removePrCommand);
        commands.put("addevent", addEventCommand);
        commands.put("removeevent", removeEventCommand);
        commands.put("getevent", getEventCommand);
        commands.put("getevents", getEventsCommand);
        commands.put("getpastevents", getPastEventsCommand);
        commands.put("eventdetail", eventDetailCommand);
        commands.put("signup", signupCommand);
        commands.put("dropout", dropoutCommand);
    }

    // -- getters and setters -------------------------------------------------

    public HashMap<String, AbstractCommand> getCommands(){
        return commands;
    }

}
