package com.inquirybox.demo.service;

import com.inquirybox.demo.util.SessionID;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public interface SessionIdService {
    public SessionID selecetSessionByUserId(int userId);


    public void insertSession(int userId,String sessionId);


    public void deleteSession(int userId);
}
