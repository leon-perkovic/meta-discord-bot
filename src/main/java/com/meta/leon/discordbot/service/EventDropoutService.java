package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.EventDropout;

import java.util.List;

/**
 * Interface for EventDropout service
 *
 * Created by Leon on 02/04/2018
 */
public interface EventDropoutService{

    List<EventDropout> findAllByEventId(Long eventId);

    EventDropout saveEventDropout(EventDropout eventDropout);

}
