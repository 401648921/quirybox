package com.inquirybox.demo.service.impl;

import com.inquirybox.demo.mapper.QuestionMapper;
import com.inquirybox.demo.service.QuestionService;
import com.inquirybox.demo.util.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QuestionServicelmpl implements QuestionService {

    @Autowired
    QuestionMapper questionMapper;
    @Override
    public boolean saveQuestion(int askId,int questionerId,String content,int questionState) {
        Date date = new Date();
        questionMapper.saveQuestion(askId,questionerId,content,date,questionState);

        return true;
    }
    /*\
    有限查找问题的方法
     */

    @Override
    public List<Question> selectQuestion(int questionState, int questionId) {
        List<Question> list = questionMapper.selectQuestion(questionState,questionId);
        return list;
    }
    /*
    更新回答的方法
     */
    @Override
    public void updateQuestion(int id, String response) {
        questionMapper.updateResponse(id,response);
    }

    @Override
    public Question selectById(int id) {
        return questionMapper.selectById(id);
    }

    @Override
    public void updateQuestionState(int id, int questionState) {
        questionMapper.updateQuestionState(id,questionState);
    }

    @Override
    public List<Question> selectAsk(int userId) {
        return questionMapper.selectAsk(userId);
    }

    @Override
    public List<Question> selectAsked(int userId) {
        return questionMapper.selectAsked(userId);
    }

    @Override
    public List<Question> selectNotNull(int userId) {
        return questionMapper.selectNotNull(userId);
    }
}
