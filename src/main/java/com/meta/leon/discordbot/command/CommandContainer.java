package com.meta.leon.discordbot.command;

import com.meta.leon.discordbot.command.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Container for all autowired commands
 *
 * @author Leon, created on 17/03/2018
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


    // Used to map all autowired commands to their key values
    public void mapCommands(){

        commands = new HashMap<>();

        commands.put("addplayer", addPlayerCommand);
        commands.put("removeplayer", removePlayerCommand);
        commands.put("getplayer", getPlayerCommand);
        commands.put("getplayers", getPlayersCommand);
        commands.put("updateplayer", updatePlayerCommand);
    }

    // -- getters and setters -------------------------------------------------

    public HashMap<String, AbstractCommand> getCommands(){
        return commands;
    }

}
