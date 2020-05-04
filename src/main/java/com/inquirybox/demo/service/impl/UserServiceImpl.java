package com.inquirybox.demo.service.impl;

import com.inquirybox.demo.content.UserContent;
import com.inquirybox.demo.mapper.UserMapper;
import com.inquirybox.demo.service.UserService;
import com.inquirybox.demo.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService , UserContent {

    @Autowired
    UserMapper userMapper;
    /*
    注册用户
     */
    @Override
    public void regiset(User user) {

        //设置用户基础属性

        //初始用户没有被封禁
        user.setUserBoxOpen(USER_NO_BAN);
        //初始用户为普通用户
        user.setUserRole(ORDINARY_USERS);
        //初始化头像
        user.setUserPicture(PICTURE_PATH);
        //初始用户没有被激活
        user.setUserActive(ACTIVATION);
        //初始化注册时间
        user.setUserRegisterDate(new Date());
        //初始用户没有开启提问箱
        user.setUserBoxOpen(CLOSE_BOX);
        userMapper.saveUser(user);

    }

    /*
    通过用户邮箱得到用户
     */
    @Override
    public String emailTest(String Email) {
        User user = userMapper.selectByEmail(Email);
        if(user == null){
            return null;
        }else{
            return user.getUserEmail();
        }

    }

    /*
        通过用户名得到用户信息
     */
    @Override
    public String userNameTest(String userName) {
        User user = userMapper.selectByUserName(userName);
        if(user == null){
            return null;
        }else{
            return user.getUserName();
        }
    }
    /*
           用账号密码来查找并验证账号
     */

    @Override
    public User userLoginTest(String userLogintext, String userPassword) {
        User user;
        if(userLogintext.contains("@")){
            user = userMapper.selectByEmail(userLogintext);
        }else{
            user = userMapper.selectByUserName(userLogintext);
        }
        //找不到用户
        if(user==null)
            return user;


        if(user.getUserPassword().equals(userPassword)){
            return user;
        }else{
            return null;
        }
    }
    /*
    通过用户名得到用户
     */

    @Override
    public User getUserByUserName(String userName) {
        User user = userMapper.selectByUserName(userName);
        if(user == null){
            return null;
        }else{
            return user;
        }
    }

    @Override
    public User selectByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public void updateByUserId(int userId, String password) {
        userMapper.updateByUserId(userId,password);
    }

    @Override
    public User selectByUserId(int userId) {
        return userMapper.selectByuserId(userId);
    }

    @Override
    public void updateActive(int userId, int state) {
        userMapper.updateActive(userId,state);
    }

    @Override
    public void updateUserPic(String picName, int userId) {
        userMapper.updateUserPic(picName,userId);
    }

    @Override
    public void updateUserName(String userName, int userId) {
        userMapper.updateUserName(userName,userId);
    }

    @Override
    public void updateEmail(String email, int userId) {
        userMapper.updateEmail(email,userId);
    }

    @Override
    public User selectById(int id) {
        return userMapper.selectByid(id);
    }

    @Override
    public void updateUsrrBan(int userId, int userBan) {
        userMapper.updateBan(userId,userBan);
    }

    @Override
    public void updateBoxOpen(int userId, int boxState) {
        userMapper.updateBoxOpen(userId,boxState);
    }
}
