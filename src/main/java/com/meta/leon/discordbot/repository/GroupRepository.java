package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Leon on 19/04/2018
 */
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findById(Long id);

    Group findByNameIgnoreCase(String name);

    Integer deleteById(Long id);

    Integer deleteByNameIgnoreCase(String name);

}
