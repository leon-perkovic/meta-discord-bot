package com.meta.leon.discordbot.command;

/**
 * Created by Leon on 17/03/2018
 */
public enum CommandAuthority {

    PUBLIC(1),
    TRIAL(2),
    MEMBER(2),
    EVENT_LEADER(3),
    ADMIN(4);

    private final int level;


    CommandAuthority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
