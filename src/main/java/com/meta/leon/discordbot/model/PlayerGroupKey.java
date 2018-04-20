package com.meta.leon.discordbot.model;

import java.io.Serializable;

/**
 * Container class for PlayerGroup's composite primary key
 * <p>
 * Created by Leon on 19/03/2018
 */
public class PlayerGroupKey implements Serializable {

    private Long playerId;

    private Long groupId;


    // -- getters and setters -------------------------------------------------

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
