package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.EventSignup;
import com.meta.leon.discordbot.repository.EventSignupRepository;
import com.meta.leon.discordbot.service.EventSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * EventSignup service - uses EventSignup repository to manage EventSignup entries in a database
 * <p>
 * Created by Leon on 01/04/2018
 */
@Service
public class EventSignupServiceImpl implements EventSignupService {

    @Autowired
    EventSignupRepository eventSignupRepository;


    @Override
    public EventSignup findEventSignup(Long eventId, Long playerId) {

        return eventSignupRepository.findByEventIdAndPlayerId(eventId, playerId);
    }

    @Override
    public List<EventSignup> findAllByEventId(Long eventId) {

        return eventSignupRepository.findAllByEventIdOrderBySignupTime(eventId);
    }

    @Override
    public List<EventSignup> findAllByPlayerId(Long playerId) {

        return eventSignupRepository.findAllByPlayerIdOrderBySignupTime(playerId);
    }

    @Override
    public EventSignup findFirstByRankAndBackup(Long eventId, String discordRank, boolean isBackup) {

        return eventSignupRepository.findFirstByEventIdAndDiscordRankAndIsBackupOrderBySignupTime(eventId, discordRank, isBackup);
    }

    @Override
    public Integer getNumOfSignups(Long eventId, boolean isBackup) {

        return eventSignupRepository.countAllByEventIdAndIsBackup(eventId, isBackup);
    }

    @Override
    public Integer getNumOfSignupsByRank(Long eventId, String discordRank, boolean isBackup) {

        return eventSignupRepository.countAllByEventIdAndDiscordRankAndIsBackup(eventId, discordRank, isBackup);
    }

    @Override
    public EventSignup saveEventSignup(EventSignup eventSignup) {

        return eventSignupRepository.save(eventSignup);
    }

    @Override
    public Integer removeEventSignup(Long eventId, Long playerId) {

        return eventSignupRepository.deleteByEventIdAndPlayerId(eventId, playerId);
    }

    @Override
    public void updateBackup(Long eventId, Long playerId, boolean isBackup) {

        eventSignupRepository.updateBackup(eventId, playerId, isBackup);
    }

}
