package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.Player;

import java.util.List;

/**
 * Interface for Player service
 * <p>
 * Created by Leon on 17/03/2018
 */
public interface PlayerService {

    List<Player> findAll();

    Player findById(Long id);

    Player findByNickname(String nickname);

    Player findByAccountName(String accountName);

    Player findByDiscordId(String discordId);

    Player savePlayer(Player player);

    Integer removeById(Long id);

    Integer removeByNickname(String nickname);

    Integer removeByDiscordId(String discordId);

}
