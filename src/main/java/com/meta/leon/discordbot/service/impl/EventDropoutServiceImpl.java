package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.EventDropout;
import com.meta.leon.discordbot.repository.EventDropoutRepository;
import com.meta.leon.discordbot.service.EventDropoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * EventDropout service - uses EventDropout repository to manage EventDropout entries in a database
 *
 * Created by Leon on 02/04/2018
 */
@Service
public class EventDropoutServiceImpl implements EventDropoutService{

    @Autowired
    EventDropoutRepository eventDropoutRepository;


    @Override
    public List<EventDropout> findAllByEventId(Long eventId){

        return eventDropoutRepository.findAllByEventIdOrderByDropoutTime(eventId);
    }

    @Override
    public EventDropout saveEventDropout(EventDropout eventDropout){

        return eventDropoutRepository.save(eventDropout);
    }

}
