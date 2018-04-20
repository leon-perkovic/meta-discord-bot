package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.Group;
import com.meta.leon.discordbot.repository.GroupRepository;
import com.meta.leon.discordbot.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Leon on 19/04/2018
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public Group findByName(String name) {
        return groupRepository.findByNameIgnoreCase(name);
    }

    @Override
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Integer removeById(Long id) {
        return groupRepository.deleteById(id);
    }

    @Override
    public Integer removeByName(String name) {
        return groupRepository.deleteByNameIgnoreCase(name);
    }

}
