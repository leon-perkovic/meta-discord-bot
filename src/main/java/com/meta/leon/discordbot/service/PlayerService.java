package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.Player;

import java.util.List;

/**
 * Interface for Player service
 *
 * @author Leon, created on 17/03/2018
 */
public interface PlayerService{

    List<Player> findAll();

    Player findById(Long id);

    Player findByNickname(String nickname);

    Player findByAccountName(String accountName);

    Player savePlayer(Player player);

    Integer removePlayerById(Long id);

    Integer removePlayerByNickname(String nickname);

}
