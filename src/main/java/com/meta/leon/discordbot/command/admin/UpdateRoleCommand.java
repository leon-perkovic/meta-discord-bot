package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Role;
import com.meta.leon.discordbot.service.RoleService;
import com.meta.leon.discordbot.validator.RoleValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !updateRole <id> <role_name> <short_name>
 * Command for updating role entries in a database
 * <p>
 * Created by Leon on 19/03/2018
 */
@Component
public class UpdateRoleCommand extends AbstractCommand {

    private Long id;
    private String roleName;
    private String shortName;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleValidator roleValidator;


    public UpdateRoleCommand() {
        super("updaterole",
                "**!updateRole <id> <role_name> <short_name>**"
                        + "\n -> Update information about a specific role.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // validate passed arguments
        if(!roleValidator.validateNumberOfArguments(arguments, 3)) {
            messageChannel.sendMessage(CommandResponses.UPDATE_ROLE_INVALID_ARGUMENTS).queue();
            return;
        }
        if(roleValidator.validateIfNumeric(arguments.get(0))) {
            this.id = Long.valueOf(arguments.get(0));
            this.roleName = arguments.get(1);
            this.shortName = arguments.get(2);

            Role role = roleService.findById(id);
            if(role == null) {
                messageChannel.sendMessage(CommandResponses.ROLE_NOT_FOUND).queue();
                return;
            }

            if(!roleValidator.validateIfUniqueRoleUpdate(id, roleName, shortName)) {
                messageChannel.sendMessage(CommandResponses.UPDATE_ROLE_ALREADY_TAKEN).queue();
                return;
            }

            role.setRoleName(roleName);
            role.setShortName(shortName);

            roleService.saveRole(role);

            messageChannel.sendMessage("Successfully updated role with *id:* **" + id + "** :white_check_mark:").queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.UPDATE_ROLE_INVALID_ID).queue();
    }
}
