package com.inquirybox.demo.service;

import com.inquirybox.demo.util.Question;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface QuestionService {
    public boolean saveQuestion(int askId,int questionerId,String content,int questionState);
    public List<Question> selectQuestion(int questionState, int questionId);
    public void updateQuestion(int id,String response);
    public Question selectById(int id);
    public void updateQuestionState(int id,int questionState);
    public List<Question> selectAsk(int userId);
    public List<Question> selectAsked(int userId);
    public List<Question> selectNotNull(int userId);
}
