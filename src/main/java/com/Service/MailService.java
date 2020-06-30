package com.Service;

import com.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class MailService {

    @Autowired
    JavaMailSenderImpl mailSender;

    /**
     * 从配置文件中获取发件人
     */
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 邮件发送
     * @param receiver 收件人
     * @param verCode 验证码
     * @throws MailSendException 邮件发送错误
     */
    @Async
    public void sendEmailVerCode(String receiver, String verCode) throws MailSendException {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("TSS博客网验证码");	//设置邮件标题
        message.setText("Tss博客网:\n"
                + "\n本次请求的邮件验证码为:[" + verCode + "],本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。");	//设置邮件正文
        message.setTo(receiver);	//设置收件人
        message.setFrom(sender);	//设置发件人

        System.out.println("MailService: before send");
        mailSender.send(message);	//发送邮件
        System.out.println("mailService : send mail now");
        /*设置缓存*/
        redisTemplate.opsForValue().set(receiver, verCode);
        System.out.println("vercode:"+verCode);
        System.out.println("mailReceiver:"+receiver);

        System.out.println("Mailservice code:"+redisTemplate.opsForValue().get(receiver));
        /**
         * K key, final long timeout, final TimeUnit unit
         * key 存储数据的key值
         * TimeUnit 时间单位
         * timeout 数据的过期时间
         * */
        redisTemplate.expire(receiver,60*5, TimeUnit.SECONDS);
    }

}