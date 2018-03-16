package com.meta.leon.discordbot.model;

import java.io.Serializable;

/**
 * Container class for EventSignup's composite primary key
 *
 * @author Leon, created on 16/03/2018
 */
public class EventSignupKey implements Serializable{

    private Long playerId;

    private Long eventId;


    // -- getters and setters -------------------------------------------------

    public Long getPlayerId(){
        return playerId;
    }

    public void setPlayerId(Long playerId){
        this.playerId=playerId;
    }

    public Long getEventId(){
        return eventId;
    }

    public void setEventId(Long eventId){
        this.eventId=eventId;
    }

}
