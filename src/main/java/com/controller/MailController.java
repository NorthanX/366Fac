package com.controller;


import com.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

import static com.Util.VerCodeGenerateUtil.generateVerCode;

@Controller
public class MailController {
    @Autowired
    private MailService mailService;


    @PostMapping("/sendEMail")
    public Object sendEmail(@RequestParam("emailAddress") String emailAddress) {
        try {
            System.out.println("mailController:send mail now");
            mailService.sendEmailVerCode(emailAddress, generateVerCode());
            return "邮件发送成功";
        } catch (Exception e) {
            return "邮件发送失败";
        }
    }

}

