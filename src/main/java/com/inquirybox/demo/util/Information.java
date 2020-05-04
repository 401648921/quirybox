package com.inquirybox.demo.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Table(name = "information")
@Data
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "information_date")
    private Date informationDate;

    @Column(name = "information_class")
    private int informationClass;

}
