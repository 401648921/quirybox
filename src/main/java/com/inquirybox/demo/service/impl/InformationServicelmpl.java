package com.inquirybox.demo.service.impl;

import com.inquirybox.demo.mapper.InformationMapper;
import com.inquirybox.demo.service.InformationService;
import com.inquirybox.demo.util.Information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class InformationServicelmpl implements InformationService {

    @Autowired
    InformationMapper informationMapper;

    @Override
    public Information selectById(int id) {
        Information information;
        information = informationMapper.selectById(id);
        return information;
    }

    @Override
    public void insertInformation(String content, int userId, Date date, int informationClass) {
        informationMapper.insertInformation(content, userId, date, informationClass);

    }

    @Override
    public void deleteById(int id) {
        informationMapper.deleteById(id);
    }

    @Override
    public void deleteByUserId(int UserId,int informationClass) {
        informationMapper.deleteByUserId(UserId,informationClass);
    }

    @Override
    public Information selectByUserId(int userId,int informationClass) {
        Information information = informationMapper.selectByUserId(userId,informationClass);
        return information;
    }
}
