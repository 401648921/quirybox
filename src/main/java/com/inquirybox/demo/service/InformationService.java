package com.inquirybox.demo.service;

import com.inquirybox.demo.util.Information;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface InformationService {
    public Information selectById(int Id);
    public void insertInformation(String content, int userId, Date date,int informationClass);
    public void deleteById(int id);
    public void deleteByUserId(int UserId,int informationClass);

    public Information selectByUserId(int userId,int informationClass);
}
