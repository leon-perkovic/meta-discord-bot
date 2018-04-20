package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.PlayerGroup;
import com.meta.leon.discordbot.repository.PlayerGroupRepository;
import com.meta.leon.discordbot.service.PlayerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Leon on 19/04/2018
 */
@Service
public class PlayerGroupServiceImpl implements PlayerGroupService {

    @Autowired
    PlayerGroupRepository playerGroupRepository;

    @Override
    public List<PlayerGroup> findAllByPlayerId(Long playerId) {
        return playerGroupRepository.findAllByPlayerId(playerId);
    }

    @Override
    public List<PlayerGroup> findAllByGroupId(Long groupId) {
        return playerGroupRepository.findAllByGroupId(groupId);
    }

    @Override
    public PlayerGroup findByIds(Long playerId, Long groupId) {
        return playerGroupRepository.findByPlayerIdAndGroupId(playerId, groupId);
    }

    @Override
    public PlayerGroup savePlayerGroup(PlayerGroup playerGroup) {
        return playerGroupRepository.save(playerGroup);
    }

    @Override
    public Integer removePlayerGroup(Long playerId, Long groupId) {
        return playerGroupRepository.deleteByPlayerIdAndGroupId(playerId, groupId);
    }
}
