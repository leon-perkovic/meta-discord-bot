package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.PlayerGroup;

import java.util.List;

/**
 * Created by Leon on 19/04/2018
 */
public interface PlayerGroupService {

    List<PlayerGroup> findAllByPlayerId(Long playerId);

    List<PlayerGroup> findAllByGroupId(Long groupId);

    PlayerGroup findByIds(Long playerId, Long groupId);

    PlayerGroup savePlayerGroup(PlayerGroup playerGroup);

    Integer removePlayerGroup(Long playerId, Long groupId);

}
