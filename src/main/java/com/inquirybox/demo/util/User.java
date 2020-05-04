package com.inquirybox.demo.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Table(name = "user")
@Data
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_active")
    private int userActive;

    @Column(name = "user_box_open")
    private int UserBoxOpen;

    @Column(name = "user_picture")
    private String userPicture;

    @Column(name = "user_ban")
    private int userBan;

    @Column(name = "user_role")
    private int userRole;

    @Column(name = "user_register_date")
    private Date userRegisterDate;

    @Column(name = "user_password")
    private String userPassword;

    public User(String userName, String userEmail, int userActive, int userBoxOpen, String userPicture, int userBan, int userRole, Date userRegisterDate, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userActive = userActive;
        UserBoxOpen = userBoxOpen;
        this.userPicture = userPicture;
        this.userBan = userBan;
        this.userRole = userRole;
        this.userRegisterDate = userRegisterDate;
        this.userPassword = userPassword;
    }
    public void show(){
        System.out.println(userId +userName+userEmail+userBan+userActive+userRole+userRegisterDate+userPassword);
    }

}
