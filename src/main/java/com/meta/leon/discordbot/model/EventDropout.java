package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "event_dropout"
 * <p>
 * Created by Leon on 02/04/2018
 */
@Entity
@IdClass(EventDropoutKey.class)
@Table(name = "event_dropout")
public class EventDropout implements Serializable {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "discord_rank")
    private String discordRank;

    @Column(name = "is_backup")
    private boolean isBackup;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "signup_time")
    private DateTime signupTime;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "dropout_time")
    private DateTime dropoutTime;


    public EventDropout() {
        // default constructor
    }

    public EventDropout(Long eventId, Long playerId, String nickname, String discordRank, boolean isBackup, DateTime signupTime, DateTime dropoutTime) {
        this.eventId = eventId;
        this.playerId = playerId;
        this.nickname = nickname;
        this.discordRank = discordRank;
        this.isBackup = isBackup;
        this.signupTime = signupTime;
        this.dropoutTime = dropoutTime;
    }

    @Override
    public String toString() {

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("eventId", eventId);
        stringBuilder.append("playerId", playerId);
        stringBuilder.append("nickname", nickname);
        stringBuilder.append("discordRank", discordRank);
        stringBuilder.append("isBackup", isBackup);
        stringBuilder.append("signupTime", signupTime);
        stringBuilder.append("dropoutTime", dropoutTime);

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public DateTime getDropoutTime() {
        return dropoutTime;
    }

    public void setDropoutTime(DateTime dropoutTime) {
        this.dropoutTime = dropoutTime;
    }
}
