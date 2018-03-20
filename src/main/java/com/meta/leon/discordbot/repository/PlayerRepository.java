package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Player repository - used to manage Player entries in a database
 *
 * @author Leon, created on 17/03/2018
 */
public interface PlayerRepository extends JpaRepository<Player, Long>{

    List<Player> findAllByOrderByNickname();

    Player findById(Long id);

    Player findByNicknameIgnoreCase(String nickname);

    Player findByAccountNameIgnoreCase(String accountName);

    Player findByDiscordId(String discordId);

    Integer deletePlayerById(Long id);

    Integer deletePlayerByNicknameIgnoreCase(String nickname);

    Integer deletePlayerByDiscordId(String discordId);

}
