package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "player_role"
 * <p>
 * Created by Leon on 19/03/2018
 */
@Entity
@IdClass(PlayerRoleKey.class)
@Table(name = "player_role")
public class PlayerRole implements Serializable {

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Id
    @Column(name = "role_id")
    private Long roleId;


    public PlayerRole() {
        // default constructor
    }

    public PlayerRole(Long playerId, Long roleId) {
        this.playerId = playerId;
        this.roleId = roleId;

    }

    @Override
    public String toString() {

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("playerId", playerId);
        stringBuilder.append("roleId", roleId);

        return stringBuilder.toString();
    }

    // -- getters and setters -------------------------------------------------

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
