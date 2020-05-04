package com.inquirybox.demo.controller;


import com.inquirybox.demo.content.UserContent;
import com.inquirybox.demo.service.EmailService;
import com.inquirybox.demo.service.InformationService;
import com.inquirybox.demo.service.UserService;
import com.inquirybox.demo.tool.CreateRand;
import com.inquirybox.demo.tool.TestUser;
import com.inquirybox.demo.util.Information;
import com.inquirybox.demo.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
public class FindPasswordController implements UserContent {

    @Autowired
    UserService userService;

    @Autowired
    InformationService inforamtionService;

    @Autowired
    private EmailService emailService;

    TestUser testUser = new TestUser();

    private String path = "localhost:8080/resetPassword/";

    @RequestMapping(value="findPassword")
    public String findPassword(){
        return "findUserPath1";
    }


    //通过向数据库里面存储密码，来验证是否本人修改密码
    @RequestMapping(value = "createFindPassword")
    public String createFindPasswordOn(String userEmail){
        TestUser testUser = new TestUser();
        if(testUser.testEmail(userEmail)==false)
            return "404";
        String password = CreateRand.getGUID();
        User user = userService.selectByEmail(userEmail);
        //如果数据库里面有该挑信息，删除这条信息
        if(inforamtionService.selectByUserId(user.getUserId(),CHANGE_PASSWOED)!=null){
            inforamtionService.deleteByUserId(user.getUserId(),CHANGE_PASSWOED);
        }
        inforamtionService.insertInformation(password,user.getUserId(),new Date(),CHANGE_PASSWOED);
        //发送邮件
        emailService.sendSimpleMail(user.getUserEmail(),"quiryBox，修改您的密码","您是否忘记了您的密码，打开http://"+path+password+"/"+user.getUserId()+"\n\t\t\t\t修改密码");
        return "succeedCreatePasswordPath";
    }

    //修改密码的页面生成
    @RequestMapping(value="resetPassword/{text}/{userId}")
    public String resetPassword(@PathVariable String text, @PathVariable String userId, Model model){
        int id = Integer.parseInt(userId);
        String text1= text;
        //获取问题对象
        Information information = inforamtionService.selectByUserId(id,CHANGE_PASSWOED);
        //验证是否密文是否正确
        if(!information.getContent().equals(text1)){
            return "404";
        }
        model.addAttribute("userId1",id);
        model.addAttribute("text1", text1);
        return "findUserPath2";
    }

    @RequestMapping(value="createFindPassword",method = RequestMethod.POST)
    public String reset(String ciphertext,String userPassword,int userId){
        //检查密码是否合法
        if(!testUser.testUserPassword(userPassword)){
            return "404";
        }
        Information information = inforamtionService.selectByUserId(userId,CHANGE_PASSWOED);
        //检查密文是否正确
        if(!information.getContent().equals(ciphertext)){
            return "404";
        }

        //二次加密
        userPassword=TestUser.resetPasswrd(userPassword);


        userService.updateByUserId(userId,userPassword);
        //删除记录
        inforamtionService.deleteByUserId(userId,CHANGE_PASSWOED);
        return "findUserPath3";
    }
}
