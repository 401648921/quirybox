package com.inquirybox.demo.mapper;

import com.inquirybox.demo.util.Blacklist;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BlacklistMapper {
    @Select("select * from blacklist where pulled_black_user=#{pulledUserId} and pulling_black_user=#{pullingUserId}")
    public Blacklist selectByUserId(int pulledUserId,int pullingUserId);

    @Delete("delete from blacklist where id = #{id}")
    public void deleteById(int id);

    @Insert("insert into blacklist(pulled_black_user,pulling_black_user) values (#{pulledUserId},#{pullingUserId})")
    public void insertBlacklist(int pulledUserId,int pullingUserId);


    @Delete("delete from blacklist where pulled_black_user=#{pulledUserId} and pulling_black_user=#{pullingUserId}")
    public void deleteByUserId(int pulledUserId,int pullingUserId);
}
