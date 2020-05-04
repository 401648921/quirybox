package com.inquirybox.demo.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Table(name = "question")
@Data
public class Question {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ask_id")
    private int askId;

    @Column(name = "questioner_id")
    private int questionerId;

    @Column(name = "content")
    private String content;


    @Column(name = "question_date")
    private Date questionDate;

    @Column(name = "response")
    private String response;

    @Column(name="question_state")
    private int questionState;

    public Question(String content, String response) {
        this.content = content;
        this.response = response;
    }
}
