package com.meta.leon.discordbot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Leon, created on 12/03/2018
 */
@SpringBootApplication
public class DiscordBotApp extends ListenerAdapter{

    public static void main(String[] args) throws LoginException, InterruptedException, IOException{

        // run with spring-boot
        SpringApplication.run(DiscordBotApp.class, args);

        // load application.properties file
        Properties prop = new Properties();
        InputStream input = DiscordBotApp.class.getClassLoader().getResourceAsStream("application.properties");
        prop.load(input);

        // initialize Bot and set token using property value
        JDA jdaBot = new JDABuilder(AccountType.BOT).setToken(prop.getProperty("discordApp.token")).buildBlocking();
        jdaBot.addEventListener(new DiscordBotApp());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        Message message = event.getMessage();
        MessageChannel messageChannel = event.getChannel();
        User user = event.getAuthor();

        if(message.getContentRaw().startsWith("!")){
            messageChannel.sendMessage("Hello " + user.getName() + ", that's a command :)").queue();
        }
    }

}
