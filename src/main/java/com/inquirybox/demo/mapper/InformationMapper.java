package com.inquirybox.demo.mapper;

import com.inquirybox.demo.content.UserContent;
import com.inquirybox.demo.util.Information;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Mapper
@Repository
public interface InformationMapper {
    @Select("select * from information where id = #{id}")
    public Information selectById(int id);

    @Select("select * from information where user_id = #{userId} and information_class=#{informationClass}")
    public Information selectByUserId(int userId,int informationClass);

    @Insert("insert into information (content,user_id,information_date,information_class) values (#{content},#{userId},#{date},#{informationClass})")
    public void insertInformation(String content, int userId, Date date,int informationClass);

    @Delete("delete from information where id = #{id}")
    public void deleteById(int id);

    @Delete("delete from information where user_id = #{userId} and information_class=#{informationClass}")
    public void deleteByUserId(int userId,int informationClass);

    @Update("UPDATE information SET content=#{content} WHERE user_id=${userId} and information_class=#{informationClass}")
    public void updateById(int userId,String content,int informationClass);
}
