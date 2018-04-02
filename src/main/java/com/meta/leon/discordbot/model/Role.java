package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class for database table "role"
 *
 * Created by Leon on 16/03/2018
 */
@Entity
@Table(name = "role")
public class Role implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "short_name")
    private String shortName;


    public Role(){
        // default constructor
    }

    public Role(String roleName, String shortName){
        this.roleName = roleName;
        this.shortName = shortName;
    }

    @Override
    public String toString(){

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("id", id);
        stringBuilder.append("roleName", roleName);
        stringBuilder.append("shortName", shortName);

        return stringBuilder.toString();
    }

    // -- getters and setters -------------------------------------------------

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getRoleName(){
        return roleName;
    }

    public void setRoleName(String roleName){
        this.roleName=roleName;
    }

    public String getShortName(){
        return shortName;
    }

    public void setShortName(String shortName){
        this.shortName=shortName;
    }

}
