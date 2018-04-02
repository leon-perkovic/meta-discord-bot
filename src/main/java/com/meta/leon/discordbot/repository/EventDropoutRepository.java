package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.EventDropout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * EventDropout repository - used to manage EventDropout entries in a database
 *
 * Created by Leon on 02/04/2018
 */
public interface EventDropoutRepository extends JpaRepository<EventDropout, Long>{

    List<EventDropout> findAllByEventIdOrderByDropoutTime(Long eventId);

}
