package com.inquirybox.demo.tool;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUser {
    public boolean testEmail(String email){

        boolean flag = false;
        try {
            String check = "^[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public boolean testUserName(String userName){

        boolean flag = false;
        try {
            String check = "^[A-Za-z0-9]+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(userName);
            flag = matcher.matches();
            if(userName.length()>10||userName.length()<6)
                flag = false;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public boolean testUserPassword(String password){
        boolean flag;
        if(password.length()!=0){
            flag = true;
        }else{
            flag= false;
        }
        return flag;
    }
    /*
    密码二次加密
     */
    public static String resetPasswrd(String password){

        char[] status = password.toCharArray();

        for(int i =0;i<password.length();i++){
            status[i]+=i%3;
        }

        String ss = Arrays.toString(status).replaceAll("[\\[\\]\\s,]", "");;
        return ss;
    }

    public static void main(String[] args) {
        TestUser testUser =new TestUser();
        System.out.println(testUser.resetPasswrd("291191"));
    }
}
