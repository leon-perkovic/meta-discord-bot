package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.command.ResponseForm;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.RoleValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !removeRole <id or role_name or short_name>
 * Command for removing role entries from a database
 *
 * Created by Leon on 19/03/2018
 */
@Component
public class RemoveRoleCommand extends AbstractCommand{

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public RemoveRoleCommand(){
        super("removerole",
                "**!removeRole <id or role_name or short_name>**"
                + "\n -> Remove a specific role.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments){
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 1)){
        messageChannel.sendMessage(CommandResponses.REMOVE_ROLE_INVALID_ARGUMENTS).queue();
        return;
        }

        int numOfRemoved;
        if(roleValidator.validateIfNumeric(arguments.get(0))){
            Long id = Long.valueOf(arguments.get(0));
            numOfRemoved = roleService.removeById(id);

        }else{
            numOfRemoved = roleService.removeByRoleName(arguments.get(0));

            if(numOfRemoved == 0){
                numOfRemoved = roleService.removeByShortName(arguments.get(0));
            }
        }

        if(numOfRemoved > 0){
            messageChannel.sendMessage(CommandResponses.REMOVE_ROLE_SUCCESS).queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.ROLE_NOT_FOUND).queue();
    }

}
