package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.Player;
import com.meta.leon.discordbot.repository.PlayerRepository;
import com.meta.leon.discordbot.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Player service - uses Player repository to manage Player entries in a database
 * <p>
 * Created by Leon on 17/03/2018
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;


    @Override
    public List<Player> findAll() {
        return playerRepository.findAllByOrderByNickname();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public Player findByNickname(String nickname) {
        return playerRepository.findByNicknameIgnoreCase(nickname);
    }

    @Override
    public Player findByAccountName(String accountName) {
        return playerRepository.findByAccountNameIgnoreCase(accountName);
    }

    @Override
    public Player findByDiscordId(String discordId) {
        return playerRepository.findByDiscordId(discordId);
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Integer removeById(Long id) {
        return playerRepository.deletePlayerById(id);
    }

    @Override
    public Integer removeByNickname(String nickname) {
        return playerRepository.deletePlayerByNicknameIgnoreCase(nickname);
    }

    @Override
    public Integer removeByDiscordId(String discordId) {
        return playerRepository.deletePlayerByDiscordId(discordId);
    }

}
