package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.PlayerRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PlayerRole repository - used to manage PlayerRole entries in a database
 *
 * Created by Leon on 19/03/2018
 */
public interface PlayerRoleRepository extends JpaRepository<PlayerRole, Long>{

    PlayerRole findByPlayerIdAndRoleId(Long playerId, Long roleId);

    Integer deleteByPlayerIdAndAndRoleId(Long playerId, Long roleId);

}
