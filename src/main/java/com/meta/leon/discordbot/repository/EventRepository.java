package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.Event;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Event repository - used to manage Event entries in a database
 *
 * Created by Leon on 21/03/2018
 */
public interface EventRepository extends JpaRepository<Event, Long>{

    @Query("SELECT e FROM Event e WHERE e.eventTime > :currentTime ORDER BY e.eventTime")
    List<Event> findUpcomingEvents(@Param("currentTime") DateTime currentTime);

    @Query("SELECT e FROM Event e WHERE e.eventTime < :currentTime ORDER BY e.eventTime DESC")
    List<Event> findPastEvents(@Param("currentTime") DateTime currentTime);

    Event findById(Long id);

    Event findByNameIgnoreCase(String name);

    Integer deleteById(Long id);

    Integer deleteByName(String name);

}
