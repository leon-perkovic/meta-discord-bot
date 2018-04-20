package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.EventSignup;

import java.util.List;

/**
 * Interface for EventSignup service
 * <p>
 * Created by Leon on 01/04/2018
 */
public interface EventSignupService {

    EventSignup findEventSignup(Long eventId, Long playerId);

    List<EventSignup> findAllByEventId(Long eventId);

    List<EventSignup> findAllByPlayerId(Long playerId);

    EventSignup findFirstByRankAndBackup(Long eventId, String discordRank, boolean isBackup);

    Integer getNumOfSignups(Long eventId, boolean isBackup);

    Integer getNumOfSignupsByRank(Long eventId, String discordRank, boolean isBackup);

    EventSignup saveEventSignup(EventSignup eventSignup);

    Integer removeEventSignup(Long eventId, Long playerId);

    void updateBackup(Long eventId, Long playerId, boolean isBackup);

}
