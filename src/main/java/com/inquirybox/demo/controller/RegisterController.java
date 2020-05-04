package com.inquirybox.demo.controller;

import com.alibaba.fastjson.JSONObject;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
public class RegisterController implements UserContent {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InformationService informationService;

    private String path = "39.97.168.17:8080/resetAction/";



    @RequestMapping("register")
    public String register(){
        return "register";
    }

    @RequestMapping(value="register",method = RequestMethod.POST)
    public String registerUser(User user, Model model){
        //二次加密
        user.setUserPassword(TestUser.resetPasswrd(user.getUserPassword()));
        if(userService.selectByEmail(user.getUserEmail())!=null){
            return "404";
        }

        //检查输入是否合法
        TestUser testUser = new TestUser();
        if(testUser.testEmail(user.getUserEmail())==false||testUser.testUserName(user.getUserName())==false||testUser.testUserPassword(user.getUserPassword())==false){
            return "404";
        }
        userService.regiset(user);
        User user1 = userService.selectByEmail(user.getUserEmail());
        //需要发送邮件让用户激活
        //生成密文
        String ciphertext = CreateRand.getGUID();
        //如果有信息就删除
        informationService.deleteByUserId(user1.getUserId(),ACTIVE_USER);
        //存入信息
        informationService.insertInformation(ciphertext,user1.getUserId(),new Date(),NO_ACTIVATION);
        emailService.sendSimpleMail(user.getUserEmail(),"quiryBox:激活你的账号","您需要激活你的账户，打开http://"+path+ciphertext+"/"+user1.getUserId()+"\n\t\t\t\t激活账号");
        model.addAttribute("text","注册成功请到邮箱激活账号");
        return "activeUserPath1";
    }
    //激活账号
    @RequestMapping(value = "resetAction/{ciphertext}/{userId}")
    public String toActivation(@PathVariable String ciphertext, @PathVariable String userId, Model model){
        int id = Integer.parseInt(userId);
        Information information = informationService.selectByUserId(id,ACTIVE_USER);
        if(information==null||!information.getContent().equals(ciphertext)){
            return "404";
        }
        userService.updateActive(id,ACTIVE_USER);
        model.addAttribute("text","成功激活账号");
        informationService.deleteByUserId(id,ACTIVE_USER);
        return "activeUserPath1";
    }

    //查看注册时邮箱是否重复
    @ResponseBody
    @RequestMapping(value="testEmail",method = RequestMethod.POST)
    public JSONObject testEmail(@RequestParam("userEmail") String email){
        JSONObject json=new JSONObject();
        if(userService.emailTest(email)==null){
            json.put("result","succeed");
        }else{
            json.put("result","fail");
        }
        return json;
    }
    //查看注册时用户名是否重复
    @ResponseBody
    @RequestMapping(value="testUserName",method = RequestMethod.POST)
    public JSONObject testUserName(@RequestParam("userName")String userName){
        JSONObject json = new JSONObject();
        if(userService.userNameTest(userName)==null){
            json.put("result","succeed");
        }else{
            json.put("result","fail");
        }
        return json;
    }


    public JSONObject testCode(HttpServletRequest request){
        return null;
    }
}
