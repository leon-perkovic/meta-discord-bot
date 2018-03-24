package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "event"
 *
 * @author Leon, created on 16/03/2018
 */
@Entity
@Table(name = "event")
public class Event implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "event_time")
    private DateTime eventTime;

    @Column(name = "description")
    private String description;

    @Column(name = "player_limit")
    private Integer playerLimit;

    @Column(name = "event_leader")
    private String eventLeader;


    public Event(){
        // default constructor
    }

    public Event(String name, DateTime eventTime, String description, Integer playerLimit, String eventLeader){
        this.name = name;
        this.eventTime = eventTime;
        this.description = description;
        this.playerLimit = playerLimit;
        this.eventLeader = eventLeader;
    }

    @Override
    public String toString(){

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("id", id);
        stringBuilder.append("name", name);
        stringBuilder.append("eventTime", eventTime);
        stringBuilder.append("description", description);
        stringBuilder.append("playerLimit", playerLimit);
        stringBuilder.append("eventLeader", eventLeader);

        return stringBuilder.toString();
    }

    // -- getters and setters -------------------------------------------------

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public DateTime getEventTime(){
        return eventTime;
    }

    public void setEventTime(DateTime eventTime){
        this.eventTime=eventTime;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public Integer getPlayerLimit(){
        return playerLimit;
    }

    public void setPlayerLimit(Integer playerLimit){
        this.playerLimit = playerLimit;
    }

    public String getEventLeader(){
        return eventLeader;
    }

    public void setEventLeader(String eventLeader){
        this.eventLeader = eventLeader;
    }

}
