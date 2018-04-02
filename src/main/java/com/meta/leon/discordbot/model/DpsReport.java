package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "dps_report"
 *
 * Created by Leon on 16/03/2018
 */
@Entity
@Table(name = "dps_report")
public class DpsReport implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "link")
    private String link;

    @JoinColumn(name = "event_id")
    private Long eventId;


    public DpsReport(){
        // default constructor
    }

    public DpsReport(String link, Long eventId){
        this.link = link;
        this.eventId = eventId;
    }

    @Override
    public String toString(){

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("id", id);
        stringBuilder.append("link", link);
        stringBuilder.append("eventId", eventId);

        return stringBuilder.toString();
    }

    // -- getters and setters -------------------------------------------------

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getLink(){
        return link;
    }

    public void setLink(String link){
        this.link=link;
    }

    public Long getEventId(){
        return eventId;
    }

    public void setEventId(Long eventId){
        this.eventId=eventId;
    }

}
