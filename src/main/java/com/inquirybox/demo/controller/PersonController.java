package com.inquirybox.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.inquirybox.demo.content.UserContent;
import com.inquirybox.demo.service.EmailService;
import com.inquirybox.demo.service.InformationService;
import com.inquirybox.demo.service.UserService;
import com.inquirybox.demo.tool.CreateRand;
import com.inquirybox.demo.tool.TestUser;
import com.inquirybox.demo.util.User;
import io.lettuce.core.ScriptOutputType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class PersonController implements UserContent {

    @Autowired
    UserService userService;

    @Autowired
    InformationService informationService;

    @Autowired
    EmailService emailService;

    private String path = "39.97.168.17:8080/resetAction/";
    private String path1 = "/root/pic";
    TestUser testUser = new TestUser();

    @RequestMapping(value = "person")
    public String personPage(Model model, HttpServletRequest request){
        HttpSession session=request.getSession();
        //获取登录用户
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            return "404";
        }
        //获取头像
        String picPath = "http://39.97.168.17/registerp.jpg";
        if(userLogin == null){
            return "404";
        }
        if(userLogin.getUserPicture()==null){
            picPath = "http://39.97.168.17//registerp.jpg";
        }else{
            picPath=userLogin.getUserPicture();
        }
        User user = userLogin;
        model.addAttribute("picPath", picPath);
        model.addAttribute("user", user);
        model.addAttribute("userLogin",userLogin);
        model.addAttribute("path1","/quiryBox/"+user.getUserName()+"/1");
        model.addAttribute("flag1",userLogin.getUserId()==user.getUserId()&&userLogin.getUserRole()==1?"true":"false");
        model.addAttribute("boxOpen",user.getUserBoxOpen()==CLOSE_BOX?"提问箱未开启":"提问箱已开启");
        return "person";
    }

    @RequestMapping("updatePic")
    public String fileUpload(@RequestParam("fileName") MultipartFile file,HttpServletRequest request){
        HttpSession session=request.getSession();
        //获取登录用户
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(file.isEmpty()){
            return "404";
        }
        System.out.println(11111);
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            return "404";
        }
        System.out.println(11111);

        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //System.out.println(suffixName);
        if(!suffixName.equals(".gif")&&!suffixName.equals(".png")&&!suffixName.equals(".jpg")&&!suffixName.equals(".jpgf")){
            return "/login";
        }
        int size = (int) file.getSize();
        //System.out.println(fileName + "-->" + size);

        String path = "/root/pic" ;
        String text1 = CreateRand.getGUID();
        File dest = new File(path + "/" + userLogin.getUserId()+text1 +suffixName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件
            userService.updateUserPic(path1 + "/" + userLogin.getUserId()+ text1 +suffixName,userLogin.getUserId());
            return "redirect:person";
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "404";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "404";
        }
    }
    /*
    简单地改密码
     */
    @ResponseBody
    @RequestMapping(value="updatePwd",method = RequestMethod.POST)
    public JSONObject simpleUpdatePwd(@RequestParam("oldPwd") String oldPassword,@RequestParam("pwd") String password,HttpServletRequest request){
        //二次加密
        oldPassword=TestUser.resetPasswrd(oldPassword);
        password=TestUser.resetPasswrd(password);

        JSONObject json=new JSONObject();
        //密码不为空
        if(oldPassword.equals("")||password.equals("")){
            json.put("result","fail");
            return json;
        }
        //获取session中的用户
        HttpSession session=request.getSession();
        User userLogin;
        String userName = (String)session.getAttribute("userName");
        if(userName!=null){
            userLogin = userService.getUserByUserName(userName);
        }else{
            json.put("result","fail");
            return json;
        }
        //密码不正确的情况
        if(!userLogin.getUserPassword().equals(oldPassword)){
            json.put("result","fail");
            return json;
        }
        userService.updateByUserId(userLogin.getUserId(),password);
        json.put("result","succeed");
        return json;

    }
    /*
    改用户名
     */

    @ResponseBody
    @RequestMapping(value="updateUserName",method = RequestMethod.POST)
    public JSONObject simpleUpdateUserName(@RequestParam("userName") String userName,HttpServletRequest request){
        JSONObject json=new JSONObject();
        HttpSession session=request.getSession();
        User userLogin;
        //System.out.println(userName);
        //用户名合法性验证
        if(!testUser.testUserName(userName)){
            json.put("result","fail");
            return json;
        }
        String userName1 = (String)session.getAttribute("userName");
        if(userName1!=null){
            userLogin = userService.getUserByUserName(userName1);
        }else{
            json.put("result","fail");
            return json;
        }
       // System.out.println(userName);
        //用户名不能和原来相等
        if(userLogin.getUserName().equals(userName)){
            json.put("result","repeate");
            return json;
        }
        //用户名不重复
        if(userService.getUserByUserName(userName)!=null){
            json.put("result","fail");
            return json;
        }

        userService.updateUserName(userName,userLogin.getUserId());
        json.put("result","succeed");
        return json;

    }
    /*
    改邮箱
     */
    @ResponseBody
    @RequestMapping(value="updateEmail",method = RequestMethod.POST)
    public JSONObject simpleUpdateEmail(@RequestParam("userEmail") String userEmail,@RequestParam("userPwd")String password, HttpServletRequest request){
        //二次加密
        password=TestUser.resetPasswrd(password);
        //System.out.println(password);

        JSONObject json = new JSONObject();
        HttpSession session=request.getSession();
        User userLogin;
        //密码合法性验证
        if(!testUser.testUserPassword(password)){
            json.put("result","fail");
            return json;
        }
        String userName1 = (String)session.getAttribute("userName");
        //System.out.println(1);
        if(userName1!=null){
            userLogin = userService.getUserByUserName(userName1);
        }else{
            json.put("result","fail");
            return json;
        }
        //System.out.println(userLogin.getUserName());
        //密码要和原来的相同
        if(!userLogin.getUserPassword().equals(password)){
            json.put("result","fail");
            return json;
        }
        //System.out.println(userLogin.getUserPassword().equals(password));
        //邮箱不能和原来相等
        if(userLogin.getUserEmail().equals(userEmail)){
            json.put("result","repeate");
            return json;
        }
        //邮箱不重复
        if(userService.selectByEmail(userEmail)!=null){
            json.put("result","repeate1");
            return json;
        }

        userService.updateEmail(userEmail,userLogin.getUserId());
        json.put("result","succeed");
        userService.updateActive(userLogin.getUserId(),NO_ACTIVATION);

        //需要发送邮件让用户激活
        //生成密文
        String ciphertext = CreateRand.getGUID();
        //信息存在就删除
        informationService.deleteByUserId(userLogin.getUserId(),ACTIVE_USER);
        //存入信息
        informationService.insertInformation(ciphertext,userLogin.getUserId(),new Date(),ACTIVATION);
        emailService.sendSimpleMail(userEmail,"quiryBox:激活你的账号","您需要激活你的账户，打开http://"+path+ciphertext+"/"+userLogin.getUserId()+"\n\t\t\t\t激活账号");
        return json;

    }

    /*
   打开或关闭提问箱
    */
    @ResponseBody
    @RequestMapping(value="updateBoxOpen",method = RequestMethod.POST)
    public JSONObject UpdateBoxOpen(@RequestParam("userName") String userName, HttpServletRequest request){
        JSONObject json = new JSONObject();
        HttpSession session=request.getSession();
        User userLogin;
        String userName1 = (String)session.getAttribute("userName");
        if(userName1!=null){
            userLogin = userService.getUserByUserName(userName1);
        }else{
            json.put("result","fail");
            return json;
        }
        if(userLogin.getUserBoxOpen()==OPEN_BOX){
            userService.updateBoxOpen(userLogin.getUserId(),CLOSE_BOX);
            json.put("result","CLOSE");
        }else{
            userService.updateBoxOpen(userLogin.getUserId(),OPEN_BOX);
            json.put("result","OPEN");
        }
        return json;

    }
}
