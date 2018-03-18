package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Player repository - used to manage Player entries in a database
 *
 * @author Leon, created on 17/03/2018
 */
public interface PlayerRepository extends JpaRepository<Player, Long>{

    Player findById(Long id);

    Player findByNicknameIgnoreCase(String nickname);

    Player findByAccountNameIgnoreCase(String accountName);

    Integer deletePlayerById(Long id);

    Integer deletePlayerByNicknameIgnoreCase(String nickname);

}
