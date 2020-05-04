package com.inquirybox.demo.config;

import com.inquirybox.demo.Filter.UserLoginFilter;
import com.inquirybox.demo.service.SessionIdService;
import com.inquirybox.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FilterConfig  implements WebMvcConfigurer {
    @Autowired
    private SessionIdService sessionIdService;

    @Autowired
    private UserService userService;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginFilter(sessionIdService,userService)).addPathPatterns("manage/*").
                addPathPatterns("search/*").
                addPathPatterns("/toBan").
                addPathPatterns("/search").
                addPathPatterns("/searchDetail").
                addPathPatterns("/person").
                addPathPatterns("/updatePic").
                addPathPatterns("/updatePwd").
                addPathPatterns("/updateUserName").
                addPathPatterns("/updateEmail").
                addPathPatterns("/updateBoxOpen").
                addPathPatterns("/putQuestion").
                addPathPatterns("/updateblack").
                addPathPatterns("/createReport").
                addPathPatterns("/updateQuestion");
    }
}
