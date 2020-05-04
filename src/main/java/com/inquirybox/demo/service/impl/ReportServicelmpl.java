package com.inquirybox.demo.service.impl;

import com.inquirybox.demo.mapper.ReportMapper;
import com.inquirybox.demo.service.ReportService;
import com.inquirybox.demo.util.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class ReportServicelmpl implements ReportService {
    @Autowired
    ReportMapper reportMapper;

    @Override
    public Report selectByUserId(int reportedId, int reportId) {
        return reportMapper.selectByUserId(reportedId,reportId);
    }

    @Override
    public void deleteById(int id) {
        reportMapper.deleteById(id);
    }

    @Override
    public void insertBlacklist(String content, int reportedId, int reportId, Date date) {
        reportMapper.insertBlacklist(content,reportedId,reportId,date);
    }

    @Override
    public void deleteByUserId(int reportedId, int reportId) {
        reportMapper.deleteByUserId(reportedId,reportId);
    }

    @Override
    public List<Report> selectAll() {
        return reportMapper.selectAll();
    }
}
