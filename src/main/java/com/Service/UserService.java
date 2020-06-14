package com.Service;

import com.Dao.FollowDao;
import com.Dao.LoginTicketDao;
import com.Dao.UserDao;
import com.Model.Follow;
import com.Model.LoginTicket;
import com.Model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService {

    @Resource
    private UserDao userDao;
    @Autowired
    private LoginTicketDao loginTicketDao;
    @Autowired
    RedisTemplate redisTemplate;
    @Resource
    private BCryptPasswordEncoder encoder;
    @Autowired
    private FollowDao followDao;

    public User getUserById(String id) {
        return userDao.getUserById(id);
    }

    public User getUserByMail(String mail) {
        return userDao.getUserByMail(mail);
    }

    public void addUser(User user) {
        userDao.insertUser(user);
    }

    public Map<String, String> login(String username, String password) {

        Map<String, String> map = new HashMap<>();

        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User u;
        if (StringUtils.contains(username, "@"))
            u = userDao.getUserByMail(username);
        else u = userDao.getUserById(username);
        if (u == null) {
            map.put("msg", "用户名或邮箱不存在");
            return map;
        }

        if (!encoder.matches(password, u.getUserpwd())) {
            map.put("msg", "密码错误");
            return map;
        }
        System.out.println("userservice login success");
        String ticket = addLoginTicket(u.getUserID());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * 默认注册时一定输入邮箱
     */
    public Map<String, String> register(String username, String password, String mail) {
        Map<String, String> map = new HashMap<>();
        map.put("reg","fail");
        Random random = new Random();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(mail)) {
            map.put("msg", "用户名或邮箱不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User u;

        u = userDao.getUserByMail(mail);
        if (u != null) {
            map.put("msg", "邮箱已经被占用");
            return map;
        }

        u = userDao.getUserById(username);
        if (u != null) {
            map.put("msg", "用户名已经被占用");
            return map;
        }

        //ValueOperations valueOperations = redisTemplate.opsForValue();
        /*从redis中获取验证码*/
        //String verificationCode = (String) valueOperations.get(mail);
        //System.out.println(verificationCode);
        /*判断提交的信息是否正确*/
        /*if (verificationCode == null || !verificationCode.equals(vercode)) {
            map.put("msg", "验证码错误");
            return map;
        }*/


        User user = new User(username, mail);
        user.setRole("user");
        user.setUserpwd(encoder.encode(password));
        userDao.insertUser(user);
        /*删除缓存中的数据*/
        //redisTemplate.delete(mail);
        map.put("reg","success");
        System.out.println("userservice register success");
        return map;
    }

    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

    public String addLoginTicket(String userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 30);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));

        loginTicketDao.insertLoginTicket(loginTicket);

        return loginTicket.getTicket();
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
        System.out.println(user.getUserpic());
    }

    @Transactional
    public void updateUserPic(User user) {
        userDao.updateUserPic(user.getUserID(), user.getUserpic());
        System.out.println("user Service "+user.getUserpic());
    }

    public void followBlogger(String BloggerId,User user){
        followDao.followBlogger(user.getUserID(),BloggerId);
    }
    public void unFollowBlogger(String BloggerId,User user){
        followDao.unfollow(user.getUserID(),BloggerId);
    }
    public List<User> MyFollowBlogger(String BloggerId,User user){
        return followDao.MyFollowBlogger(user.getUserID());
    }
    public boolean followed(String BloggerId,User user){
        return followDao.followed(user.getUserID(),BloggerId)==1;
    }
}
