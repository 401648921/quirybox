package com.inquirybox.demo.service;

import com.inquirybox.demo.util.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void regiset(User user);

    String emailTest(String Email);

    String userNameTest(String userName);

    User userLoginTest(String userLogintext,String userPassword);


    User getUserByUserName(String userName);

    User selectByEmail(String email);

    void updateByUserId(int userId,String password);

    User selectByUserId(int userId);

    void updateActive(int userId,int state);

    void updateUserPic(String picName,int userId);

    void updateUserName(String userName,int userId);

    void updateEmail(String email,int userId);

    User selectById(int id);

    void updateUsrrBan(int userId,int userBan);

    void updateBoxOpen(int userId,int boxState);
}
