package com.inquirybox.demo.service.impl;

import com.inquirybox.demo.mapper.BlacklistMapper;
import com.inquirybox.demo.service.BlacklistService;
import com.inquirybox.demo.util.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistSercivelmpl implements BlacklistService {

    @Autowired
    BlacklistMapper blacklistMapper;
    @Override
    public Blacklist selectByUserId(int pulledUserId, int pullingUserId) {
        Blacklist blacklist = new Blacklist();
        blacklist = blacklistMapper.selectByUserId(pulledUserId,pullingUserId);
        return blacklist;
    }

    @Override
    public void deleteByUserId(int pulledUserId, int pullingUserId) {
        blacklistMapper.deleteByUserId(pulledUserId,pullingUserId);
    }

    @Override
    public void insertByUserId(int pulledUserId, int pullingUserId) {
        blacklistMapper.insertBlacklist(pulledUserId,pullingUserId);
    }

    @Override
    public void deleteById(int id) {
        blacklistMapper.deleteById(id);
    }
}
