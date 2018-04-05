package com.meta.leon.discordbot;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandContainer;
import com.meta.leon.discordbot.command.CommandResponses;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Listener class - used to handle events
 *
 * Created by Leon on 17/03/2018
 */
@Component
public class BotListener extends ListenerAdapter{

    @Autowired
    private CommandContainer commandContainer;


    @Override
    public void onMessageReceived(MessageReceivedEvent event){

        // get received message and channel
        Message message = event.getMessage();
        MessageChannel messageChannel = event.getChannel();

        String messageContent = message.getContentRaw();

        // check if passed message is a command
        if(!isCommand(messageContent)){
            return;
        }
        messageContent = messageContent.substring(1);

        // map commands in command container
        commandContainer.mapCommands();
        LinkedHashMap<String, AbstractCommand> commands = commandContainer.getCommands();

        // get user roles and set authority level
        User user = event.getAuthor();
        List<String> roleNames = getUserRoles(user);

        CommandAuthority authority = getUserAuthority(roleNames);

        String[] splitContent = messageContent.split(" ");

        // check if command exists
        if(commands.keySet().contains(splitContent[0].toLowerCase())){
            AbstractCommand command = commands.get(splitContent[0].toLowerCase());

            // check if user is authorized to use the command
            if(authority.getLevel() >= command.getAuthority().getLevel()){
                ArrayList<String> arguments = new ArrayList<>();

                // if there are passed arguments - add them to an array
                if(splitContent.length > 1){
                    for(int i = 1; i < splitContent.length; i++){
                        arguments.add(splitContent[i]);
                    }
                }
                // call corresponding command and get its response
                Object response = command.execute(user, arguments).getResponse();

                // send corresponding response
                if(response instanceof  String){
                    messageChannel.sendMessage((String) response).queue();
                }else if(response instanceof MessageEmbed){
                    messageChannel.sendMessage((MessageEmbed) response).queue();
                }else{
                    messageChannel.sendMessage(response.toString()).queue();
                }
            }else{
                // if user isn't authorized - send corresponding response
                messageChannel.sendMessage(CommandResponses.NOT_AUTHORIZED).queue();
            }
        }else{
            // if command doesn't exist - send corresponding response
            messageChannel.sendMessage(CommandResponses.INVALID_COMMAND).queue();
        }
    }

    private boolean isCommand(String messageContent){
        if(messageContent.startsWith("!")){
            return true;
        }
        return false;
    }

    public static List<String> getUserRoles(User user){
        List<Guild> guilds = user.getMutualGuilds();
        Member member = null;

        for(Guild guild : guilds){
            if(guild.getId().equals(DiscordBotApp.getServerId())){
                member = guild.getMember(user);
                break;
            }
        }

        List<String> roleNames = new ArrayList<>();

        if(member != null){
            List<Role> roles = member.getRoles();

            for(Role role : roles){
                if(role.getName().startsWith("@")){
                    roleNames.add(role.getName().substring(1));
                }else{
                    roleNames.add(role.getName());
                }
            }
        }

        return roleNames;
    }

    public static CommandAuthority getUserAuthority(List<String> roleNames){
        System.out.println(DiscordBotApp.getEventLeaderRole());
        if(roleNames.contains(DiscordBotApp.getAdminRole())){
            return CommandAuthority.ADMIN;
        }
        if(roleNames.contains(DiscordBotApp.getEventLeaderRole())){
            return CommandAuthority.EVENT_LEADER;
        }
        if(roleNames.contains(DiscordBotApp.getMemberRole())){
            return CommandAuthority.MEMBER;
        }
        if(roleNames.contains(DiscordBotApp.getTrialRole())){
            return CommandAuthority.TRIAL;
        }
        return CommandAuthority.PUBLIC;
    }

}
