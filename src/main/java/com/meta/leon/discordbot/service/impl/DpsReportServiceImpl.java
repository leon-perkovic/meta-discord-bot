package com.meta.leon.discordbot.service.impl;

import com.meta.leon.discordbot.model.DpsReport;
import com.meta.leon.discordbot.repository.DpsReportRepository;
import com.meta.leon.discordbot.service.DpsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DpsReport service - uses DpsReport repository to manage DpsReport entries in a database
 *
 * Created by Leon on 02/04/2018
 */
@Service
public class DpsReportServiceImpl implements DpsReportService{

    @Autowired
    DpsReportRepository dpsReportRepository;


    @Override
    public DpsReport saveDpsReport(DpsReport dpsReport){

        return dpsReportRepository.save(dpsReport);
    }

    @Override
    public List<DpsReport> findAllByEventId(Long eventId){

        return dpsReportRepository.findAllByEventId(eventId);
    }

    @Override
    public Integer removeAllByEventId(Long eventId){

        return dpsReportRepository.deleteAllByEventId(eventId);
    }

}
