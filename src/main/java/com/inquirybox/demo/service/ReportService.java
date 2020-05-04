package com.inquirybox.demo.service;

import com.inquirybox.demo.util.Report;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ReportService {

    public Report selectByUserId(int reportedId, int reportId);

    public void deleteById(int id);

    public void insertBlacklist(String content, int reportedId, int reportId, Date date);

    public void deleteByUserId(int reportedId, int reportId);

    public List<Report> selectAll();
}
