package com.meta.leon.discordbot.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class for database table "player"
 *
 * @author Leon, created on 16/03/2018
 */
@Entity
@Table(name = "player")
public class Player implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discord_name")
    private String discordName;

    @Column(name = "account_name")
    private String accountName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "player_role",
            joinColumns = { @JoinColumn(name = "player_id", referencedColumnName = "id") },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id")
            })
    @Fetch(FetchMode.JOIN)
    private Set<Role> roles = new HashSet<>();


    public Player(){
        // default constructor
    }

    public Player(String discordName, String accountName){
        this.discordName=discordName;
        this.accountName=accountName;
    }

    @Override
    public String toString(){

        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("id", id);
        stringBuilder.append("discordName", discordName);
        stringBuilder.append("accountName", accountName);
        stringBuilder.append("roles", roles.toString());

        return stringBuilder.toString();
    }

    // -- getters and setters -------------------------------------------------

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getDiscordName(){
        return discordName;
    }

    public void setDiscordName(String discordName){
        this.discordName=discordName;
    }

    public String getAccountName(){
        return accountName;
    }

    public void setAccountName(String accountName){
        this.accountName=accountName;
    }

    public Set<Role> getRoles(){
        return roles;
    }

    public void setRoles(Set<Role> roles){
        this.roles=roles;
    }

}
