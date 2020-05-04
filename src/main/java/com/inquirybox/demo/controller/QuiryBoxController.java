package com.inquirybox.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.inquirybox.demo.content.QuestionContent;
import com.inquirybox.demo.content.UserContent;
import com.inquirybox.demo.service.BlacklistService;
import com.inquirybox.demo.service.QuestionService;
import com.inquirybox.demo.service.ReportService;
import com.inquirybox.demo.service.UserService;
import com.inquirybox.demo.util.Question;
import com.inquirybox.demo.util.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class QuiryBoxController implements UserContent, QuestionContent {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    private ReportService reportService;

    @RequestMapping(value="quiryBox/{name}/{page}")
    public String loginController(@PathVariable String name,@PathVariable int page, Model model,HttpServletRequest request) {
        HttpSession session=request.getSession();
        //获取登录用户
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            userLogin = new User("游客",null,0,0,null,0,3,new Date(),null);
        }
        //初始化访问用户的头像
        String picPath = "http://39.97.168.17:8080/registerp.jpg";
        User user = userService.getUserByUserName(name);
        if(user == null){
            return "404";
        }
        if(user.getUserPicture()==null){
            picPath = "http://39.97.168.17:8080/registerp.jpg";
        }
        //得到问题
        List<Question> list = questionService.selectQuestion(QUESTION_ALIVE,user.getUserId());
        List<Question> list1;
        if(list.size()>page*4){
            list1 = list.subList(page*4-4,page*4);
        }else{
            list1=list.subList(page*4-4,list.size());
        }
        int pageAll = list.size()/4+1;
        //获取是不是被拉黑对象
        boolean black = blacklistService.selectByUserId(user.getUserId(),userLogin.getUserId())!=null;
        //查看是否应该开启提问箱
        boolean boxOpen = false;
        if(userLogin.getUserBan()== USER_NO_BAN&&user.getUserBoxOpen()==OPEN_BOX&&userLogin.getUserActive()!=NO_ACTIVATION&&user.getUserActive()!=NO_ACTIVATION&&user.getUserBan()== USER_NO_BAN&&blacklistService.selectByUserId(user.getUserId(),userLogin.getUserId())==null)
            boxOpen = true;
        model.addAttribute("boxOpen",boxOpen?"true":"false");
        model.addAttribute("black", black==true?"true":"false");
        model.addAttribute("picPath", picPath);
        model.addAttribute("user", user);
        model.addAttribute("userLogin",userLogin);
        model.addAttribute("page",page);
        model.addAttribute("questions",list1);
        model.addAttribute("pageAll",pageAll);
        model.addAttribute("flag",userLogin.getUserId()==user.getUserId()?"true":"false");
        model.addAttribute("path","quiryBox");
        model.addAttribute("flag1",userLogin.getUserId()==user.getUserId()&&userLogin.getUserRole()==1?"true":"false");
        return "quiryBox";
    }



    @RequestMapping(value="putQuestion")
    public String putQuestion(String content,String userName, HttpServletRequest request){
        if(content.length()<1||content.length()>50){
            return "404";
        }
        HttpSession session=request.getSession();
        //获取登录用户的用户名
        String loginUserName = (String)session.getAttribute("userName");
        //获取用户
        User userLogin = userService.getUserByUserName(loginUserName);
        User user = userService.getUserByUserName(userName);
        //查看提问者是 是否开启提问箱 否被封禁  是否被拉黑  是否激活
        if(!(userLogin.getUserBan()== USER_NO_BAN&&user.getUserBoxOpen()==OPEN_BOX&&userLogin.getUserActive()!=NO_ACTIVATION&&user.getUserActive()!=NO_ACTIVATION&&user.getUserBan()== USER_NO_BAN&&blacklistService.selectByUserId(user.getUserId(),userLogin.getUserId())==null))
            return "404";
        if(user==null||userLogin==null){
            return "404";
        }
        questionService.saveQuestion(userLogin.getUserId(),user.getUserId(),content,QUESTION_ALIVE);

        return "redirect:quiryBox/"+user.getUserName()+"/1";

    }

    @RequestMapping(value = "quiryBox/{userName}/updateQuestion")
    public String updateQuestion(String questionId,String response,@PathVariable String userName){
        int id = Integer.parseInt(questionId);
        if(response.length()==0){
            return "404";
        }
        questionService.updateQuestion(id,response);

        return "redirect:/quiryBox/"+userName+"/1";

    }
    /*
    拉黑管理
     */

    @ResponseBody
    @RequestMapping(value="updateblack",method = RequestMethod.POST)
    public JSONObject simpleUpdateUserName(@RequestParam("userName") String userName, HttpServletRequest request){
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
        int id=userService.getUserByUserName(userName).getUserId();
        if(blacklistService.selectByUserId(id,userLogin.getUserId())==null){
            blacklistService.insertByUserId(id,userLogin.getUserId());
            json.put("result","black");
            return json;
        }else{
            blacklistService.deleteByUserId(id,userLogin.getUserId());
            json.put("result","white");
            return json;
        }


    }

    /*
    举报发送
     */

    @ResponseBody
    @RequestMapping(value="createReport",method = RequestMethod.POST)
    public JSONObject createReport(@RequestParam("userId") String userId,@RequestParam("content") String content,HttpServletRequest request){
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
        //内容不能为空
        if(content.length()==0){
            json.put("result","fail");
            return json;
        }
        int id=Integer.parseInt(userId);
        reportService.insertBlacklist(content,id,userLogin.getUserId(),new Date());


        json.put("result","succeed");
        return json;

    }

    @RequestMapping(value = "updateQuestion")
    public String updateQuestionState(String questionId,HttpServletRequest request){
        HttpSession session=request.getSession();
        //获取登录用户的用户名
        String loginUserName = (String)session.getAttribute("userName");
        //获取用户
        User userLogin = userService.getUserByUserName(loginUserName);
        if(userLogin==null){
            return "redirect:login";
        }
        int id = Integer.parseInt(questionId);
        Question question = questionService.selectById(id);
        //删除者和用户不统一的情况且不是管理员
        if(userService.selectByUserId(question.getQuestionerId()).getUserId()!=userLogin.getUserId()&&userLogin.getUserRole()!=ADMINISTRATOR){
            return "redirect:login";
        }
        if(question.getQuestionState()==QUESTION_ALIVE)
            questionService.updateQuestionState(id,QUESTION_DELETED);
        else {
            questionService.updateQuestionState(id,QUESTION_ALIVE);
        }

        return "redirect:/quiryBox/"+userLogin.getUserName()+"/1";

    }
}
