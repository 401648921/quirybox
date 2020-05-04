package com.inquirybox.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.inquirybox.demo.content.QuestionContent;
import com.inquirybox.demo.content.UserContent;
import com.inquirybox.demo.service.*;
import com.inquirybox.demo.tool.TestUser;
import com.inquirybox.demo.util.Question;
import com.inquirybox.demo.util.Report;
import com.inquirybox.demo.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ManageController implements QuestionContent, UserContent {
    @Autowired
    UserService userService;

    @Autowired
    private InformationService inforamtionService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private  ReportService reportService;

    TestUser testUser = new TestUser();


    @RequestMapping("manage/{page}")
    public String manage1(HttpServletRequest request, Model model,@PathVariable int page){
        HttpSession session=request.getSession();
        //获取登录用户
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            return "404";
        }
        //查看是否为管理员
        if(userLogin.getUserRole()!=1){
            return "404";
        }

        //获得举报信息
        List<Report> list = reportService.selectAll();
        List<Report> list1;
        if(list.size()>page*4){
            list1 = list.subList(page*4-4,page*4);
        }else{
            list1=list.subList(page*4-4,list.size());
        }
        int pageAll = list.size()/4+1;

        User user = userLogin;
        model.addAttribute("user", user);
        model.addAttribute("userLogin",userLogin);
        model.addAttribute("page",page);
        model.addAttribute("reports",list1);
        model.addAttribute("pageAll",pageAll);
        model.addAttribute("flag",userLogin.getUserId()==user.getUserId()?"true":"false");
        model.addAttribute("path","manage");
        model.addAttribute("flag1",userLogin.getUserId()==user.getUserId()&&userLogin.getUserRole()==1?"true":"false");

        return "manage";
    }

    /*
    管理员查看用户信息
     */
    @RequestMapping("search/{userPn}/{page}")
    public String manage2(HttpServletRequest request, Model model,@PathVariable int page,@PathVariable String userPn){
        HttpSession session=request.getSession();
        //获取登录用户
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            return "404";
        }
        //查看是否为管理员
        if(userLogin.getUserRole()!=1){
            return "404";
        }
        //获取被查看用户信息
        User user;
        if(userPn.contains("@")){
            user=userService.selectByEmail(userPn);
        }else{
            user = userService.getUserByUserName(userPn);
        }

        //得到问题
        //获取提问箱问题数
        List<Question> askedlist = questionService.selectAsked(user.getUserId());
        int askedNumber = askedlist.size();
        //获取提问数
        List<Question> askList = questionService.selectAsk(user.getUserId());
        int askNumber = askList.size();
        //获取回复的数量
        List<Question> respenseList = questionService.selectNotNull(user.getUserId());
        int respendNumber = respenseList.size();
        List<Question> list1 = askList;
        List<Question> list2 = askedlist;
        List<Question> list3 = respenseList;
        int pageAll = askedlist.size()/4+1;
        int askedlistPageAll = askedlist.size();
        int askListPageAll = askList.size();
        int respenseListPageAll = respenseList.size();


        //分页
       if(askedlist.size()>page*4){
            askedlist = askedlist.subList(page*4-4,page*4);
        }else{
            askedlist=askedlist.subList(page*4-4,list2.size());
        }

        if(askList.size()>page*4){
            askList = askList.subList(page*4-4,page*4);
        }else{
            askList=askList.subList(page*4-4,list1.size());
        }


       if(respenseList.size()>page*4){
            respenseList = respenseList.subList(page*4-4,page*4);
        }else{
            respenseList=respenseList.subList(page*4-4,list3.size());
        }


        model.addAttribute("user", user);
        model.addAttribute("active", user.getUserActive()==ACTIVATION?"已激活":"未激活");
        model.addAttribute("ban", user.getUserBan()==USER_BAN?"封禁中":"未被封禁");
        model.addAttribute("userLogin",userLogin);
        model.addAttribute("page",page);
        model.addAttribute("askedList",askedlist);
        model.addAttribute("pageAll",pageAll);
        model.addAttribute("askedlistPageAll",askedlistPageAll);
        model.addAttribute("askList",askList);
        model.addAttribute("askListPageAll",askListPageAll);
        model.addAttribute("respenseList",respenseList);
        model.addAttribute("respenseListPageAll",respenseListPageAll);
        model.addAttribute("flag",userLogin.getUserId()==user.getUserId()?"true":"false");
        model.addAttribute("path","manage");
        model.addAttribute("flag1",userLogin.getUserId()==user.getUserId()&&userLogin.getUserRole()==1?"true":"false");
        model.addAttribute("pathOne","/quiryBox/"+user.getUserName()+"/1");

        return "search";
    }

    @ResponseBody
    @RequestMapping(value="toBan",method = RequestMethod.POST)
    public JSONObject simpleUpdateUserName(@RequestParam("userId") int userId, HttpServletRequest request){
        JSONObject json=new JSONObject();
        HttpSession session=request.getSession();
        User userLogin;
        //获取登录用户
        String userName1 = (String)session.getAttribute("userName");
        if(userName1!=null){
            userLogin = userService.getUserByUserName(userName1);
        }else{
            json.put("result","fail");
            return json;
        }
        //查看是否是管理员
        if(userLogin.getUserRole()!=ADMINISTRATOR){
            json.put("result","fail");
            return json;
        }

        //int id=userService.getUserByUserName(userName).getUserId();
        User user = userService.selectByUserId(userId);
        if(user.getUserBan()==USER_NO_BAN){
            userService.updateUsrrBan(user.getUserId(),USER_BAN);
            json.put("result","ban");
            return json;
        }else{
            userService.updateUsrrBan(user.getUserId(),USER_NO_BAN);
            json.put("result","noBan");
            return json;
        }
    }

    @RequestMapping("search")
    public String manage1(HttpServletRequest request,int reportedId ){
        HttpSession session=request.getSession();
        //获取登录用户
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            return "404";
        }
        //查看是否为管理员
        if(userLogin.getUserRole()!=1){
            return "404";
        }

        return "redirect:search/"+userService.selectByUserId(reportedId).getUserName()+"/1";
    }

    @RequestMapping(value = "searchDetail",method = RequestMethod.POST)
    public String manage2(HttpServletRequest request,String reportedId ){
        HttpSession session=request.getSession();
        //获取登录用户
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            return "404";
        }
        //查看是否为管理员
        if(userLogin.getUserRole()!=1){
            return "404";
        }
        User user;
        if(reportedId.contains("@")){
            user = userService.selectByEmail(reportedId);
        }else{
            user = userService.getUserByUserName(reportedId);
        }
        if(user == null){
            return "redirect:/manage/1";
        }
        return "redirect:search/"+user.getUserName()+"/1";
    }
}
