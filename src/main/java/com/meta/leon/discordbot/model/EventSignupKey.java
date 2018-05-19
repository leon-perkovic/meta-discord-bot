package com.meta.leon.discordbot.model;

import java.io.Serializable;

/**
 * Container class for EventSignup's composite primary key
 * <p>
 * Created by Leon on 16/03/2018
 */
public class EventSignupKey implements Serializable {

    private Long eventId;

    private Long playerId;

    // -- getters and setters -------------------------------------------------

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

}
