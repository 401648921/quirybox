package com.inquirybox.demo.mapper;

import com.inquirybox.demo.util.User;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Mapper
@Repository
public interface UserMapper {

    @Insert("insert into user(user_name,user_email,user_active,user_box_open,user_picture,user_ban,user_role,user_register_date,user_password) values (#{userName},#{userEmail},#{userActive},#{userBoxOpen},#{userPicture},#{userBan},#{userRole},#{userRegisterDate},#{userPassword})")
    void saveUser(User user);

    //void savaUser(@Param("user_name") String userName, @Param("user_email")String userEmail, @Param("user_active")int userActive, @Param("user_box_open")int userBoxOpen, @Param("user_picture")String userPicture, @Param("user_ban")int userBan, @Param("user_role")int userRole, @Param("user_register_date")Date userRegisterDate);

    @Select("select * from user where user_email=#{userEmail}")
    public User selectByEmail(@Param("userEmail") String userEmail);

    @Select("select * from user where user_name=#{userName}")
    public User selectByUserName(@Param("userName") String userName);

    @Update("UPDATE user SET user_password=#{password} WHERE user_id=#{userId}")
    public void updateByUserId(int userId,String password);

    @Select("select * from user where user_id=#{userId}")
    public User selectByuserId(int userId);

    @Update("UPDATE user SET user_active=#{state} WHERE user_id=#{userId}")
    public void updateActive(int userId,int state);

    @Update("UPDATE user SET user_picture=#{picName} WHERE user_id=#{userId}")
    public void updateUserPic(String picName,int userId);

    @Update("UPDATE user SET user_name=#{userName} WHERE user_id=#{userId}")
    public void updateUserName(String userName,int userId);

    @Update("UPDATE user SET user_email=#{email} WHERE user_id=#{userId}")
    public void updateEmail(String email,int userId);

    @Select("select * from user where user_id=#{id}")
    public User selectByid(int id);

    @Update("UPDATE user SET user_ban=#{userBan} WHERE user_id=#{userId}")
    public void updateBan(int userId,int userBan);

    @Update("UPDATE user SET user_box_open=#{BoxState} WHERE user_id=#{userId}")
    public void updateBoxOpen(int userId,int BoxState);
}

