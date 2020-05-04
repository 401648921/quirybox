package com.inquirybox.demo.mapper;

import com.inquirybox.demo.util.Blacklist;
import com.inquirybox.demo.util.Report;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface ReportMapper {
    @Select("select * from report where reported_id=#{reportedId} and report_id=#{reportId}")
    public Report selectByUserId(int reportedId, int reportId);

    @Delete("delete from report where id=#{id}")
    public void deleteById(int id);

    @Insert("insert into report(content,reported_id,report_id,report_date) values (#{content},#{reportedId},#{reportId},#{date})")
    public void insertBlacklist(String content, int reportedId, int reportId, Date date);


    @Delete("delete from report where reported_id=#{reportedId} and report_id=#{reportId}")
    public void deleteByUserId(int reportedId, int reportId);

    @Select("select * from report")
    public List<Report> selectAll();
}
