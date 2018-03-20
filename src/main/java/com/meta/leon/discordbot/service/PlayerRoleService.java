package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.PlayerRole;

/**
 * Interface for PlayerRole service
 *
 * @author Leon, created on 19/03/2018
 */
public interface PlayerRoleService{

    PlayerRole findbyIds(Long playerId, Long roleId);

    PlayerRole savePlayerRole(PlayerRole playerRole);

    Integer removePlayerRole(Long playerId, Long roleId);

}
