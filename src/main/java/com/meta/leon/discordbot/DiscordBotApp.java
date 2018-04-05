package com.meta.leon.discordbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Leon on 12/03/2018
 */
@SpringBootApplication
public class DiscordBotApp implements CommandLineRunner{

    @Autowired
    private BotListener botListener;

    private static JDA jdaBot;

    private static String token;
    private static String serverId;
    private static String announcementChannel;

    private static String adminRole;
    private static String eventLeaderRole;
    private static String memberRole;
    private static String trialRole;
    private static String publicRole;


    public static void main(String[] args){

        // run with spring-boot
        SpringApplication.run(DiscordBotApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception{

        // set default time zone
        DateTimeZone.setDefault(DateTimeZone.forID("Europe/Amsterdam"));

        // load application.properties file
        Properties prop = new Properties();
        InputStream input = DiscordBotApp.class.getClassLoader().getResourceAsStream("application.properties");
        prop.load(input);

        // read and store properties
        token = prop.getProperty("discordApp.token");
        serverId = prop.getProperty("discordApp.serverId");
        announcementChannel = prop.getProperty("discordApp.announcementChannel");

        adminRole = prop.getProperty("discord.adminRole");
        eventLeaderRole = prop.getProperty("discord.eventLeaderRole");
        memberRole = prop.getProperty("discord.memberRole");
        trialRole = prop.getProperty("discord.trialRole");
        publicRole = prop.getProperty("discord.publicRole");

        // initialize Bot and set token
        jdaBot = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();

        // add autowired BotListener bean to event listener
        jdaBot.addEventListener(botListener);

        // set !help as currently played "game"
        jdaBot.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "!help"));
    }

    // -- getters and setters -------------------------------------------------

    public static JDA getJdaBot(){
        return jdaBot;
    }

    public static String getServerId(){
        return serverId;
    }

    public static String getAnnouncementChannel(){
        return announcementChannel;
    }

    public static String getAdminRole(){
        return adminRole;
    }

    public static String getEventLeaderRole(){
        return eventLeaderRole;
    }

    public static String getMemberRole(){
        return memberRole;
    }

    public static String getTrialRole(){
        return trialRole;
    }

    public static String getPublicRole(){
        return publicRole;
    }

}
