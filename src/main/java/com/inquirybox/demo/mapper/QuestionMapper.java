package com.inquirybox.demo.mapper;

import com.inquirybox.demo.util.Question;
import com.inquirybox.demo.util.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {

    @Insert("insert into question(ask_id,questioner_id,content,question_date,question_state) values (#{askId},#{questionerId},#{content},#{questionDate},#{questionState})")
    public void saveQuestion(int askId , int questionerId,String content , Date questionDate,int questionState);

    @Delete("delete from question where id = #{id}")
    public void deleteQuestionById(int id);

    @Select("select * from question where id = #{id}")
    public Question selectById(int id);

    @Select("select id,content,response from question where questioner_id=#{questionId} and question_state=#{questionState}")
    public List<Question> selectQuestion(int questionState, int questionId);

    @Update("UPDATE question SET response=#{response} WHERE id=#{id}")
    public void updateResponse(int id,String response);

    @Update("UPDATE question SET question_state=#{questionState} WHERE id=#{questionId}")
    public void updateQuestionState(int questionId,int questionState);

    @Select("select * from question where ask_id=#{userId}")
    public List<Question> selectAsk(int userId);

    @Select("select * from question where questioner_id=#{userId}")
    public List<Question> selectAsked(int userId);

    @Select("select * from question where questioner_id=#{userId} and content is not null")
    public List<Question> selectNotNull(int userId);
}
