package com.inquirybox.demo.service.impl;

import com.inquirybox.demo.mapper.SessionIdMapper;
import com.inquirybox.demo.service.SessionIdService;
import com.inquirybox.demo.util.SessionID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionIdServicelmpl implements SessionIdService {

    @Autowired
    SessionIdMapper sessionIdMapper;

    @Override
    public SessionID selecetSessionByUserId(int userId) {
        return sessionIdMapper.selecetSessionByUserId(userId);
    }

    @Override
    public void insertSession(int userId, String sessionId) {
        sessionIdMapper.insertSession(userId,sessionId);
    }

    @Override
    public void deleteSession(int userId) {
        sessionIdMapper.deleteSession(userId);
    }
}
