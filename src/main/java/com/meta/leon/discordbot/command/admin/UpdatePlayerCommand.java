package com.meta.leon.discordbot.command.admin;

import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.validator.PlayerValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * !updatePlayer <id> <nickname> <account_name> [@username]
 * [@username] is optional
 * Command for updating player entries in a database
 * <p>
 * Created by Leon on 18/03/2018
 */
@Component
public class UpdatePlayerCommand extends AbstractCommand {

    private Long id;
    private String nickname;
    private String accountName;
    private String discordId;

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerValidator playerValidator;


    public UpdatePlayerCommand() {
        super("updateplayer",
                "**!updatePlayer <id> <nickname> <account_name> [@username]**"
                        + "\n -> Update information about a specific player.",
                "N/A",
                CommandAuthority.ADMIN);
    }

    @Override
    @Transactional
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();

        // if @username wasn't specified - add it as null
        if(arguments.size() == 3) {
            arguments.add(null);
        }

        // validate passed arguments
        if(!playerValidator.validateNumberOfArguments(arguments, 4)) {
            messageChannel.sendMessage(CommandResponses.UPDATE_PLAYER_INVALID_ARGUMENTS).queue();
            return;
        }
        if(arguments.get(3) != null) {
            if(!playerValidator.validateIfDiscordId(arguments.get(3))) {
                messageChannel.sendMessage(CommandResponses.UPDATE_PLAYER_INVALID_DISCORD_ID).queue();
                return;
            }
        }

        if(playerValidator.validateIfNumeric(arguments.get(0))) {
            this.id = Long.valueOf(arguments.get(0));
            this.nickname = arguments.get(1);
            this.accountName = arguments.get(2);
            this.discordId = arguments.get(3);
            if(discordId != null) {
                discordId = discordId.replace("!", "");
            }

            Player player = playerService.findById(id);
            if(player == null) {
                messageChannel.sendMessage(CommandResponses.PLAYER_NOT_FOUND).queue();
                return;
            }

            if(!playerValidator.validateIfUniquePlayerUpdate(id, nickname, accountName, discordId)) {
                messageChannel.sendMessage(CommandResponses.UPDATE_PLAYER_ALREADY_TAKEN).queue();
                return;
            }

            player.setNickname(nickname);
            player.setAccountName(accountName);
            player.setDiscordId(discordId);

            playerService.savePlayer(player);

            messageChannel.sendMessage("Successfully updated player with *id:* **" + id + "** :white_check_mark:").queue();
            return;
        }
        messageChannel.sendMessage(CommandResponses.UPDATE_PLAYER_INVALID_ID).queue();
    }
}
