package com.meta.leon.discordbot.service;

import com.meta.leon.discordbot.model.DpsReport;

import java.util.List;

/**
 * Interface for DpsReport service
 * <p>
 * Created by Leon on 02/04/2018
 */
public interface DpsReportService {

    DpsReport saveDpsReport(DpsReport dpsReport);

    List<DpsReport> findAllByEventId(Long eventId);

    Integer removeAllByEventId(Long eventId);

}
