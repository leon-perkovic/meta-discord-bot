package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "player_group"
 * <p>
 * Created by Leon on 19/03/2018
 */
@Entity
@IdClass(PlayerGroupKey.class)
@Table(name = "player_group")
public class PlayerGroup implements Serializable {

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Id
    @Column(name = "group_id")
    private Long groupId;


    public PlayerGroup() {
        // default constructor
    }

    public PlayerGroup(Long playerId, Long groupId) {
        this.playerId = playerId;
        this.groupId = groupId;

    }

    @Override
    public String toString() {

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("playerId", playerId);
        stringBuilder.append("groupId", groupId);

        return stringBuilder.toString();
    }

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
