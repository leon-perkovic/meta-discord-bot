package com.meta.leon.discordbot.command;

import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

/**
 * Abstract class for Commands - contains all basic command properties and methods
 *
 * Created by Leon on 17/03/2018
 */
public abstract class AbstractCommand{

    private String name;

    private String description;

    private String help;

    private CommandAuthority authority;


    public AbstractCommand(){
        // default constructor
    }

    public AbstractCommand(String name, String description, String help, CommandAuthority authority){

        this.name = name;
        this.description = description;
        this.help = help;
        this.authority = authority;
    }

    public ResponseForm execute(User user, ArrayList<String> arguments){
        return null;
    }

    // -- getters and setters -------------------------------------------------

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getHelp(){
        return help;
    }

    public void setHelp(String help){
        this.help = help;
    }

    public CommandAuthority getAuthority(){
        return authority;
    }

    public void setAuthority(CommandAuthority authority){
        this.authority = authority;
    }

}
