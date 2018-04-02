package com.meta.leon.discordbot.model;

import java.io.Serializable;

/**
 * Container class for PlayerRole's composite primary key
 *
 * Created by Leon on 19/03/2018
 */
public class PlayerRoleKey implements Serializable{

    private Long playerId;

    private Long roleId;


    // -- getters and setters -------------------------------------------------

    public Long getPlayerId(){
        return playerId;
    }

    public void setPlayerId(Long playerId){
        this.playerId = playerId;
    }

    public Long getRoleId(){
        return roleId;
    }

    public void setRoleId(Long roleId){
        this.roleId = roleId;
    }

}
