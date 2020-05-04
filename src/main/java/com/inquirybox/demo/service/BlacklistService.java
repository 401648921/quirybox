package com.inquirybox.demo.service;

import com.inquirybox.demo.util.Blacklist;
import org.springframework.stereotype.Service;

@Service
public interface BlacklistService {
    public Blacklist selectByUserId(int pulledUserId, int pullingUserId);

    public void deleteByUserId(int pulledUserId,int pullingUserId);

    public void insertByUserId(int pulledUserId,int pullingUserId);

    public void deleteById(int id);
}
