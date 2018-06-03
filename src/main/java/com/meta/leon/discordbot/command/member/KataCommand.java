package com.meta.leon.discordbot.command.member;

import com.meta.leon.discordbot.BotListener;
import com.meta.leon.discordbot.command.AbstractCommand;
import com.meta.leon.discordbot.command.CommandAuthority;
import com.meta.leon.discordbot.command.CommandResponses;
import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import com.meta.leon.discordbot.util.CommandUtil;
import com.meta.leon.discordbot.validator.GlobalValidator;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * !kata
 * Command that likes to spam chat :)
 * <p>
 * Created by Leon on 02/04/2018
 */
@Component
public class KataCommand extends AbstractCommand {

    @Autowired
    PlayerService playerService;

    @Autowired
    GlobalValidator globalValidator;

    @Autowired
    CommandUtil commandUtil;

    public KataCommand() {
        super("kata",
                "**!kata**"
                        + "\n -> Hmm, what could it do?",
                "N/A",
                CommandAuthority.MEMBER);
    }

    @Override
    public void execute(MessageReceivedEvent discordEvent, ArrayList<String> arguments) {
        MessageChannel messageChannel = discordEvent.getChannel();
        User user = discordEvent.getAuthor();

        // get user roles and set authority level
        List<String> roleNames = BotListener.getUserRoles(user);
        CommandAuthority authority = BotListener.getUserAuthority(roleNames);

        // validate passed arguments
        if(!globalValidator.validateNumberOfArguments(arguments, 0)) {
            messageChannel.sendMessage(CommandResponses.ROLL_INVALID_ARGUMENTS).queue();
            return;
        }

        Random rng = new Random();
        int range = rng.nextInt(16) + 5;
        StringBuilder spam = new StringBuilder("");

        Player player = playerService.findByDiscordId(user.getAsMention());
        if(player != null) {
            if(player.getNickname().toLowerCase().equals("kata")) {
                for(int i = 0; i < range; i++) {
                    spam.append("**[Sun Spirit]**\n");
                }
                messageChannel.sendMessage(spam.toString()).queue();
                return;
            }
        }

        player = playerService.findByNickname("kata");
        if(player != null) {
            if(authority.getLevel() >= CommandAuthority.EVENT_LEADER.getLevel()) {
                for(int i = 0; i < range; i++) {
                    spam.append(player.getDiscordId())
                            .append("\n");
                }
                messageChannel.sendMessage(spam.toString()).queue();
                return;
            }
        }
        messageChannel.sendMessage("Sorry, you're not the spam-queen :wink:").queue();
    }

}
