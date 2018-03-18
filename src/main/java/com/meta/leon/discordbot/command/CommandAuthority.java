package com.meta.leon.discordbot.command;

/**
 * @author Leon, created on 17/03/2018
 */
public enum CommandAuthority{

    PUBLIC(1),
    MEMBER(2),
    ADMIN(3);

    private final int level;


    CommandAuthority(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

}
