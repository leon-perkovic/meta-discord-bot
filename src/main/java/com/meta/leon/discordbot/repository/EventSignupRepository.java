package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.EventSignup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * EventSignup repository - used to manage EventSignup entries in a database
 * <p>
 * Created by Leon on 01/04/2018
 */
public interface EventSignupRepository extends JpaRepository<EventSignup, Long> {

    EventSignup findByEventIdAndPlayerId(Long eventId, Long playerId);

    List<EventSignup> findAllByEventIdOrderBySignupTime(Long eventId);

    List<EventSignup> findAllByPlayerIdOrderBySignupTime(Long playerId);

    EventSignup findFirstByEventIdAndDiscordRankAndIsBackupOrderBySignupTime(Long eventId, String discordRank, boolean isBackup);

    Integer countAllByEventIdAndIsBackup(Long eventId, boolean isBackup);

    Integer countAllByEventIdAndDiscordRankAndIsBackup(Long eventId, String discordRank, boolean isBackup);

    Integer deleteByEventIdAndPlayerId(Long eventId, Long playerId);

    @Modifying
    @Query("UPDATE EventSignup es SET es.isBackup = :isBackup WHERE es.eventId = :eventId AND es.playerId = :playerId")
    void updateBackup(@Param("eventId") Long eventId, @Param("playerId") Long playerId, @Param("isBackup") boolean isBackup);

}
