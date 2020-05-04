package com.inquirybox.demo.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Table(name = "sesssionid")
@Data
public class SessionID {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "sessionID")
    private String sessionID;

    @Column(name="user_id")
    private int userId;

    public SessionID(String sessionID, int userId) {
        this.sessionID = sessionID;
        this.userId = userId;
    }
}
