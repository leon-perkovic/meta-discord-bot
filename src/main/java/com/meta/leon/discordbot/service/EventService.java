package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.Event;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Interface for Event service
 *
 * @author Leon, created on 21/03/2018
 */
public interface EventService{

    List<Event> findUpcoming(DateTime currentTime);

    List<Event> findPast(DateTime currentTime);

    Event findById(Long id);

    Event findByName(String name);

    Event saveEvent(Event event);

    Integer removeById(Long id);

    Integer removeByName(String name);

}
