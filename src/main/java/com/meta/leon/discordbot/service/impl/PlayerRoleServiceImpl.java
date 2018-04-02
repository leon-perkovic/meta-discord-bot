package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.PlayerRole;
import com.meta.leon.discordbot.repository.PlayerRoleRepository;
import com.meta.leon.discordbot.service.PlayerRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PlayerRole service - uses PlayerRole repository to manage PlayerRole entries in a database
 *
 * Created by Leon on 19/03/2018
 */
@Service
public class PlayerRoleServiceImpl implements PlayerRoleService{

    @Autowired
    PlayerRoleRepository playerRoleRepository;


    @Override
    public PlayerRole findbyIds(Long playerId, Long roleId){

        return playerRoleRepository.findByPlayerIdAndRoleId(playerId, roleId);
    }

    @Override
    public PlayerRole savePlayerRole(PlayerRole playerRole){

        return playerRoleRepository.save(playerRole);
    }

    @Override
    public Integer removePlayerRole(Long playerId, Long roleId){

        return playerRoleRepository.deleteByPlayerIdAndAndRoleId(playerId, roleId);
    }
}
