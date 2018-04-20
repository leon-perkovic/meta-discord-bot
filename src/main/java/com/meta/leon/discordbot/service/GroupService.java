package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.Group;

import java.util.List;

/**
 * Created by Leon on 19/04/2018
 */
public interface GroupService {

    List<Group> findAll();

    Group findById(Long id);

    Group findByName(String name);

    Group saveGroup(Group group);

    Integer removeById(Long id);

    Integer removeByName(String name);

}
