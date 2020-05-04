package com.inquirybox.demo.content;

public interface UserContent {
    //用户没有被封禁
    int USER_NO_BAN = 0;
    //用户被封禁
    int USER_BAN = 1;
    //游客
    int visitor = 3;
    //用户为管理员
    int ADMINISTRATOR = 1;
    //用户为普通用户
    int ORDINARY_USERS = 0;
    //用户为激活状态
    int ACTIVATION = 1;
    //用户为非激活状态
    int NO_ACTIVATION = 0;
    //提问箱开启
    int OPEN_BOX = 1;
    //提问箱关闭
    int CLOSE_BOX = 0;
    //头像默认地址
    String PICTURE_PATH = null;
    //修改密码类型的信息
    int CHANGE_PASSWOED = 0;
    //激活用户信息类型
    int ACTIVE_USER = 1;
}
