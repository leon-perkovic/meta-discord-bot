package com.meta.leon.discordbot.repository;

import com.meta.leon.discordbot.model.DpsReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * DpsReport repository - used to manage DpsReport entries in a database
 *
 * Created by Leon on 02/04/2018
 */
public interface DpsReportRepository extends JpaRepository<DpsReport, Long>{

    List<DpsReport> findAllByEventId(Long eventId);

    Integer deleteAllByEventId(Long eventId);

}
