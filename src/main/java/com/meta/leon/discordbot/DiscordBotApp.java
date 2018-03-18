package com.meta.leon.discordbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author Leon, created on 12/03/2018
 */
@SpringBootApplication
public class DiscordBotApp implements CommandLineRunner{

    @Autowired
    private BotListener botListener;

    private static String token;
    private static String adminRole;
    private static String memberRole;
    private static String publicRole;


    public static void main(String[] args){

        // run with spring-boot
        SpringApplication.run(DiscordBotApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception{

        // load application.properties file
        Properties prop = new Properties();
        InputStream input = DiscordBotApp.class.getClassLoader().getResourceAsStream("application.properties");
        prop.load(input);

        // read and store properties
        token = prop.getProperty("discordApp.token");
        adminRole = prop.getProperty("discord.adminRole");
        memberRole = prop.getProperty("discord.memberRole");
        publicRole = prop.getProperty("discord.publicRole");

        // initialize Bot and set token
        JDA jdaBot = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();

        // add autowired BotListener bean to event listener
        jdaBot.addEventListener(botListener);
    }

    // -- getters and setters -------------------------------------------------

    public static String getAdminRole(){
        return adminRole;
    }

    public static String getMemberRole(){
        return memberRole;
    }

    public static String getPublicRole(){
        return publicRole;
    }

}
