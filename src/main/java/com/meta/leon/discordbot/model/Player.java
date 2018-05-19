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
 * <p>
 * Created by Leon on 16/03/2018
 */
@Entity
@Table(name = "player")
public class Player implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "discord_id")
    private String discordId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "player_role",
            joinColumns = {@JoinColumn(name = "player_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id")
            })
    @Fetch(FetchMode.JOIN)
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "player_group",
            joinColumns = {@JoinColumn(name = "player_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "group_id", referencedColumnName = "id")
            })
    @Fetch(FetchMode.JOIN)
    private Set<Group> groups = new HashSet<>();

    public Player() {
        // default constructor
    }

    public Player(String nickname, String accountName, String discordId) {
        this.nickname = nickname;
        this.accountName = accountName;
        this.discordId = discordId;
    }

    public String rolesToString() {
        String rolesInfo = "[";

        int i = 0;
        for(Role role : roles) {
            if(i == roles.size() - 1) {
                rolesInfo += role.getRoleName();
            }else {
                rolesInfo += role.getRoleName() + ", ";
            }
            i++;
        }
        rolesInfo += "]";

        return rolesInfo;
    }

    public String groupsToString() {
        String groupsInfo = "[";

        int i = 0;
        for(Group group : groups) {
            if(i == groups.size() - 1) {
                groupsInfo += group.getName();
            }else {
                groupsInfo += group.getName() + ", ";
            }
            i++;
        }
        groupsInfo += "]";

        return groupsInfo;
    }

    @Override
    public String toString() {
        final ToStringBuilder stringBuilder = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE);

        stringBuilder.append("id", id);
        stringBuilder.append("nickname", nickname);
        stringBuilder.append("accountName", accountName);
        stringBuilder.append("roles", roles.toString());
        stringBuilder.append("groups", groups.toString());

        return stringBuilder.toString();
    }

    // -- getters and setters -------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDiscordId() {
        return discordId;
    }

    public void setDiscordId(String discordId) {
        this.discordId = discordId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

}
