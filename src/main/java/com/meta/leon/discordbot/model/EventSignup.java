package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "event_signup"
 *
 * @author Leon, created on 16/03/2018
 */
@Entity
@IdClass(EventSignupKey.class)
@Table(name = "event_signup")
public class EventSignup implements Serializable{

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "discord_rank")
    private String discordRank;

    @Column(name = "signup_time")
    private DateTime signupTime;


    public EventSignup(){
        // default constructor
    }

    public EventSignup(Long playerId, Long eventId, String discordRank, DateTime signupTime){
        this.playerId = playerId;
        this.eventId = eventId;
        this.discordRank = discordRank;
        this.signupTime = signupTime;
    }

    @Override
    public String toString(){

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("playerId", playerId);
        stringBuilder.append("eventId", eventId);
        stringBuilder.append("discordRank", discordRank);
        stringBuilder.append("signupTime", signupTime);

        return stringBuilder.toString();
    }

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

    public String getDiscordRank(){
        return discordRank;
    }

    public void setDiscordRank(String discordRank){
        this.discordRank=discordRank;
    }

    public DateTime getSignupTime(){
        return signupTime;
    }

    public void setSignupTime(DateTime signupTime){
        this.signupTime=signupTime;
    }

}
