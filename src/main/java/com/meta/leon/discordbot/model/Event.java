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

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "event_date")
    private DateTime eventDate;

    @Column(name = "description")
    private String description;


    public Event(){
        // default constructor
    }

    public Event(DateTime eventDate, String description){
        this.eventDate = eventDate;
        this.description = description;
    }

    @Override
    public String toString(){

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("id", id);
        stringBuilder.append("eventDate", eventDate);
        stringBuilder.append("description", description);

        return stringBuilder.toString();
    }

    // -- getters and setters -------------------------------------------------

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public DateTime getEventDate(){
        return eventDate;
    }

    public void setEventDate(DateTime eventDate){
        this.eventDate=eventDate;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;
    }

}
