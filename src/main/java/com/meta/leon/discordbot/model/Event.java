package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class for database table "event"
 *
 * Created by Leon on 16/03/2018
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

    @Column(name = "member_limit")
    private Integer memberLimit;

    @Column(name = "trial_limit")
    private Integer trialLimit;

    @Column(name = "event_leader")
    private String eventLeader;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_signup",
            joinColumns = { @JoinColumn(name = "event_id", referencedColumnName = "id") },
            inverseJoinColumns = {
                    @JoinColumn(name = "player_id", referencedColumnName = "id")
            })
    @Fetch(FetchMode.JOIN)
    private Set<Player> players = new HashSet<>();


    public Event(){
        // default constructor
    }

    public Event(String name, DateTime eventTime, String description, Integer playerLimit, Integer memberLimit, Integer trialLimit, String eventLeader){
        this.name = name;
        this.eventTime = eventTime;
        this.description = description;
        this.playerLimit = playerLimit;
        this.memberLimit = memberLimit;
        this.trialLimit = trialLimit;
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
        stringBuilder.append("memberLimit", memberLimit);
        stringBuilder.append("trialLimit", trialLimit);
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

    public Integer getMemberLimit(){
        return memberLimit;
    }

    public void setMemberLimit(Integer memberLimit){
        this.memberLimit = memberLimit;
    }

    public Integer getTrialLimit(){
        return trialLimit;
    }

    public void setTrialLimit(Integer trialLimit){
        this.trialLimit = trialLimit;
    }

    public String getEventLeader(){
        return eventLeader;
    }

    public void setEventLeader(String eventLeader){
        this.eventLeader = eventLeader;
    }

    public Set<Player> getPlayers(){
        return players;
    }

    public void setPlayers(Set<Player> players){
        this.players = players;
    }
}
