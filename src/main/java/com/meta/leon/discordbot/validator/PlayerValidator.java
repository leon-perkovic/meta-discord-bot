package com.meta.leon.discordbot.validator;

import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validator for passed arguments
 * <p>
 * Created by Leon on 18/03/2018
 */
@Component
public class PlayerValidator extends GlobalValidator {

    @Autowired
    PlayerService playerService;


    public boolean validateIfUniquePlayer(String nickname, String accountName, String discordId) {

        if(playerService.findByNickname(nickname) == null && playerService.findByAccountName(accountName) == null) {
            if(discordId == null) {
                return true;

            }else if(playerService.findByDiscordId(discordId) == null) {
                return true;
            }
        }
        return false;
    }

    public boolean validateIfUniquePlayerUpdate(Long id, String nickname, String accountName, String discordId) {
        Player player = playerService.findById(id);

        if(playerService.findByNickname(nickname) != null && !player.getNickname().equalsIgnoreCase(nickname)) {
            return false;
        }
        if(playerService.findByAccountName(accountName) != null && !player.getAccountName().equalsIgnoreCase(accountName)) {
            return false;
        }
        if(discordId != null) {
            if(playerService.findByDiscordId(discordId) != null && !player.getDiscordId().equalsIgnoreCase(discordId)) {
                return false;
            }
        }
        return true;
    }

}
