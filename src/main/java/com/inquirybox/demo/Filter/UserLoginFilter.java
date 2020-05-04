package com.inquirybox.demo.Filter;

import com.inquirybox.demo.service.SessionIdService;
import com.inquirybox.demo.service.UserService;
import com.inquirybox.demo.util.User;
import io.lettuce.core.ScriptOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserLoginFilter implements HandlerInterceptor {


    public SessionIdService sessionIdService;


    public UserService userService;

    public UserLoginFilter(SessionIdService sessionIdService, UserService userService) {
        this.sessionIdService = sessionIdService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session=request.getSession();
        String userName = (String)session.getAttribute("userName");
        System.out.println(userName);
        User user = userService.getUserByUserName(userName);
        //sessionID和数据库里面的是否相等
        if(session.getId().equals(sessionIdService.selecetSessionByUserId( user.getUserId() ).getSessionID() ) ){
            return true;
        }
        return false;
    }

}
