package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.Event;
import com.meta.leon.discordbot.repository.EventRepository;
import com.meta.leon.discordbot.service.EventService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Event service - uses Event repository to manage Event entries in a database
 *
 * @author Leon, created on 21/03/2018
 */
@Service
public class EventServiceImpl implements EventService{

    @Autowired
    EventRepository eventRepository;


    @Override
    public List<Event> findUpcoming(DateTime currentTime){

        return eventRepository.findUpcomingEvents(currentTime);
    }

    @Override
    public List<Event> findPast(DateTime currentTime){

        return eventRepository.findPastEvents(currentTime);
    }

    @Override
    public Event findById(Long id){

        return eventRepository.findById(id);
    }

    @Override
    public Event findByName(String name){

        return eventRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Event saveEvent(Event event){

        return eventRepository.save(event);
    }

    @Override
    public Integer removeById(Long id){

        return eventRepository.deleteById(id);
    }

    @Override
    public Integer removeByName(String name){

        return eventRepository.deleteByName(name);
    }
}
