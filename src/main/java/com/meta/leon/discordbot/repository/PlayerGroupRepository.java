package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.PlayerGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Leon on 19/04/2018
 */
public interface PlayerGroupRepository extends JpaRepository<PlayerGroup, Long> {

    List<PlayerGroup> findAllByPlayerId(Long playerId);

    List<PlayerGroup> findAllByGroupId(Long groupId);

    PlayerGroup findByPlayerIdAndGroupId(Long playerId, Long groupId);

    Integer deleteByPlayerIdAndGroupId(Long playerId, Long groupId);

}
