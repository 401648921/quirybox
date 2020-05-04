package com.inquirybox.demo.mapper;

import com.inquirybox.demo.util.SessionID;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SessionIdMapper {
    @Select("select * from sessionid where user_id=#{userId}")
    public SessionID selecetSessionByUserId(int userId);


    @Insert("insert into sessionid(sessionID,user_id) values (#{sessionId},#{userId})")
    public void insertSession(int userId,String sessionId);


    @Delete("delete from sessionid where user_id=#{userId}")
    public void deleteSession(int userId);
}
