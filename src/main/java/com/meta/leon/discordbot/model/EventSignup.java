package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "event_signup"
 * <p>
 * Created by Leon on 16/03/2018
 */
@Entity
@IdClass(EventSignupKey.class)
@Table(name = "event_signup")
public class EventSignup implements Serializable {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "discord_rank")
    private String discordRank;

    @Column(name = "is_backup")
    private boolean isBackup;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "signup_time")
    private DateTime signupTime;


    public EventSignup() {
        // default constructor
    }

    public EventSignup(Long eventId, Long playerId, String discordRank, boolean isBackup, DateTime signupTime) {
        this.eventId = eventId;
        this.playerId = playerId;
        this.discordRank = discordRank;
        this.isBackup = isBackup;
        this.signupTime = signupTime;
    }

    @Override
    public String toString() {

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("eventId", eventId);
        stringBuilder.append("playerId", playerId);
        stringBuilder.append("discordRank", discordRank);
        stringBuilder.append("isBackup", isBackup);
        stringBuilder.append("signupTime", signupTime);

        return stringBuilder.toString();
    }

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

    public String getDiscordRank() {
        return discordRank;
    }

    public void setDiscordRank(String discordRank) {
        this.discordRank = discordRank;
    }

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }

    public DateTime getSignupTime() {
        return signupTime;
    }

    public void setSignupTime(DateTime signupTime) {
        this.signupTime = signupTime;
    }

}
