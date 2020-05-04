package com.inquirybox.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.inquirybox.demo.service.SessionIdService;
import com.inquirybox.demo.service.UserService;
import com.inquirybox.demo.tool.CodeUtil;
import com.inquirybox.demo.tool.TestUser;
import com.inquirybox.demo.util.User;
import io.lettuce.core.ScriptOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private SessionIdService sessionIdService;

    @Autowired
    private UserService userService;

    //登录界面
    @RequestMapping(value="login")
    public String loginController( Model model) {
        model.addAttribute("flag","SIGHT IN");
        return "login";
    }


    @RequestMapping(value="login",method = RequestMethod.POST)
    public String registerUser(String userLoginText, String password, String testCode, HttpServletRequest request, Model model){

        HttpSession session=request.getSession();
        User user;
        //System.out.println("1");
        //System.out.println(password);
        if (!CodeUtil.checkVerifyCode(request)) {
            return "404";
        }

        //二次加密
        password= TestUser.resetPasswrd(password);



        if((user = userService.userLoginTest(userLoginText,password))==null){
            model.addAttribute("flag","用户名或密码错误");
            return "login";
        }else{
            //System.out.println("用户匹配");
            session.setAttribute("userName",user.getUserName());
            session.setAttribute("userPassword",user.getUserPassword());
            //存入sessionID存入数据库
            if(sessionIdService.selecetSessionByUserId(user.getUserId())==null){
                sessionIdService.insertSession(user.getUserId(),session.getId());
            }else{
                sessionIdService.deleteSession(user.getUserId());
                sessionIdService.insertSession(user.getUserId(),session.getId());
            }
            return "redirect:quiryBox/"+user.getUserName()+"/1";
        }


    }
    /*
    验证验证码是否正确
     */

    @ResponseBody
    @RequestMapping(value="testCode",method = RequestMethod.POST)
    public JSONObject testUserName(HttpServletRequest request){
        JSONObject json = new JSONObject();

        if(CodeUtil.checkVerifyCode(request)){
            json.put("result","succeed");
        }else{
            json.put("result","fail");
        }
        return json;
    }

    //注销
    @RequestMapping(value="logout")
    public String logout(String path,String userName,HttpSession session, SessionStatus sessionStatus ){
        session.invalidate();
        sessionStatus.setComplete();
        if(path.equals("quiryBox")){
            return "redirect:"+path+"/"+userName+"/1";
        }else if(path.equals("manage")){
            return "redirect:login";
        }


        return "redirect:"+path+"/"+userName+"/1";
    }

    @RequestMapping(value="logoutTo")
    public String logoutTo(String userName,HttpSession session, SessionStatus sessionStatus ){
        session.invalidate();
        sessionStatus.setComplete();
        return "redirect:login";
    }
}