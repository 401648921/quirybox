package com.inquirybox.demo.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Table(name = "report")
@Data
public class Report {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "content")
    private String content;


    @Column(name = "reported_id")
    private int reportedId;

    @Column(name = "report_id")
    private int reportId;


    @Column(name = "report_date")
    private Date date;

    public Report(String content, int reportedId, int reportId, Date date) {
        this.content = content;
        this.reportedId = reportedId;
        this.reportId = reportId;
        this.date = date;
    }
}
