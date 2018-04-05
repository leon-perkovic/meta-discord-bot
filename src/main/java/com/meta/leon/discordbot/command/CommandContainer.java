package com.meta.leon.discordbot.command;

import com.meta.leon.discordbot.command.admin.*;
import com.meta.leon.discordbot.command.everyone.HelpCommand;
import com.meta.leon.discordbot.command.everyone.RollCommand;
import com.meta.leon.discordbot.command.member.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

/**
 * Container for all autowired commands
 *
 * Created by Leon on 17/03/2018
 */
@Component
public class CommandContainer{

    private TreeMap<String, AbstractCommand> commands;

    @Autowired
    private AddPlayerCommand addPlayerCommand;
    @Autowired
    private RemovePlayerCommand removePlayerCommand;
    @Autowired
    private PlayerCommand playerCommand;
    @Autowired
    private PlayersCommand playersCommand;
    @Autowired
    private UpdatePlayerCommand updatePlayerCommand;
    @Autowired
    private AddRoleCommand addRoleCommand;
    @Autowired
    private RemoveRoleCommand removeRoleCommand;
    @Autowired
    private RoleCommand roleCommand;
    @Autowired
    private RolesCommand rolesCommand;
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
    private EventCommand eventCommand;
    @Autowired
    private EventsCommand eventsCommand;
    @Autowired
    private PastEventsCommand pastEventsCommand;
    @Autowired
    private EventDetailCommand eventDetailCommand;
    @Autowired
    private SignupCommand signupCommand;
    @Autowired
    private DropoutCommand dropoutCommand;
    @Autowired
    private AddDpsReportCommand addDpsReportCommand;
    @Autowired
    private RemoveDpsReportCommand removeDpsReportCommand;
    @Autowired
    private DpsReportCommand dpsReportCommand;
    @Autowired
    private AnnounceCommand announceCommand;
    @Autowired
    private HelpCommand helpCommand;
    @Autowired
    private RollCommand rollCommand;
    @Autowired
    private KataCommand kataCommand;


    // Used to map all autowired commands to their key values
    public void mapCommands(){

        commands = new TreeMap<>();

        commands.put("addplayer", addPlayerCommand);
        commands.put("removeplayer", removePlayerCommand);
        commands.put("player", playerCommand);
        commands.put("players", playersCommand);
        commands.put("updateplayer", updatePlayerCommand);
        commands.put("addrole", addRoleCommand);
        commands.put("removerole", removeRoleCommand);
        commands.put("role", roleCommand);
        commands.put("roles", rolesCommand);
        commands.put("updaterole", updateRoleCommand);
        commands.put("addpr", addPrCommand);
        commands.put("removepr", removePrCommand);
        commands.put("addevent", addEventCommand);
        commands.put("removeevent", removeEventCommand);
        commands.put("event", eventCommand);
        commands.put("events", eventsCommand);
        commands.put("pastevents", pastEventsCommand);
        commands.put("eventdetail", eventDetailCommand);
        commands.put("signup", signupCommand);
        commands.put("dropout", dropoutCommand);
        commands.put("adddpsreport", addDpsReportCommand);
        commands.put("removedpsreport", removeDpsReportCommand);
        commands.put("dpsreport", dpsReportCommand);
        commands.put("announce", announceCommand);
        commands.put("help", helpCommand);
        commands.put("roll", rollCommand);
        commands.put("kata", kataCommand);
    }

    // -- getters and setters -------------------------------------------------

    public TreeMap<String, AbstractCommand> getCommands(){
        return commands;
    }

}
