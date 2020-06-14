package com.controller;

import com.Model.*;
import com.Service.BlogService;
import com.Service.TagService;
import com.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
//index为主页，包含登录，注册，

public class IndexController {
    @Autowired
    private UserService userService;
@Autowired
HostHolder hostHolder;
@Autowired
    BlogService blogService;
@Autowired
    TagService tagService;
    @PostMapping(path="/userRegister")
    public String register(Model model, HttpServletResponse httpResponse, @RequestParam(value="username") String username, @RequestParam(value="password") String password,
                           @RequestParam(value="emailAddress") String mail){
        Map<String,String> map = userService.register(username,password,mail);
        System.out.println("indexController: register");
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            httpResponse.addCookie(cookie);
            return "redirect:/";
        }else {
            model.addAttribute("msg",map.get("msg"));
            if (map.get("reg").equals("success"))return "redirect:/";
            else return "redirect:/in";
        }
    }

    @RequestMapping("/register")
    public String ToRegister(){
        return "Register1";

    }

    @RequestMapping(path = {"/","/index"})
    public String jumpToIndex(Model model){
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        List<Blog> blogs=blogService.getLatest();
        model.addAttribute("blogs",blogs);
        /*List<ViewObject> vos = new ArrayList<>();
        for(Blog blog:blogs){
            ViewObject vo = new ViewObject();
            List<Tag> tags;
            tags=tagService.getTagByBlogID(blog.getBlogID());
            if (tags==null)tags = new ArrayList<>();
            vo.set("blog",blog);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);*/
        return "index";

    }

    @RequestMapping("/in")
    public String in(Model model,@RequestParam(value = "next",required = false)String next){
        model.addAttribute("next",next);
        User user = hostHolder.getUser();

        return "Login1";
    }

    @PostMapping("/userLogin")
    public String login(Model model, HttpServletResponse httpResponse,
                        @RequestParam("username") String username, @RequestParam("password") String password){
        Map<String,String> map = userService.login(username,password);
        System.out.println("indexController: login");
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket",map.get("ticket"));
            cookie.setPath("/");
            httpResponse.addCookie(cookie);

            return "redirect:/";
        }else {
            model.addAttribute("msg", map.get("msg"));
            return "Login1";
        }
    }

    @RequestMapping("/userLogout")
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
    @RequestMapping("/userForgetPassword")
    public String forgetPassword(){

        return "ForgetPassword";
    }

    @RequestMapping("/aboutUs")
    public String aboutUs(){
        return "About";
    }

}