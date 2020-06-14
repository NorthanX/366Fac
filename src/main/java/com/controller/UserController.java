package com.controller;

import com.Model.HostHolder;
import com.Model.User;
import com.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Controller
@RequestMapping("/user/")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private HostHolder hostHolder;

    @GetMapping("/editUser")
    public String showUInfo(Model model){
        User user = hostHolder.getUser();
        model.addAttribute(user);
        return "EditUser";
    }

    @PostMapping("/editUser")
    public String editUser(@ModelAttribute User user){
        this.userService.updateUser(user);
        return "redirect:/user/show";
    }

    @RequestMapping("/show")
    public String showUser(Model model){
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        return "UserInfo";
    }

    // 执行上传
    @RequestMapping("/uploadPic")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));  // 后缀名
        // 定义上传文件保存路径
        String path = "F:\\two\\大二下\\软件工程基础\\大作业\\project\\src\\main\\resources\\static\\img";//图片要上传到哪个文件夹，部署时注意
        // 新文件名
        filename = UUID.randomUUID() + suffixName;
        // 新建文件
        File filepath = new File(path, filename);
        // 判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            // 写入文件
            file.transferTo(new File(path + File.separator + filename));
        } catch ( IOException e) {
            e.printStackTrace();
        }
        // 将src路径发送至html页面
        User user = hostHolder.getUser();
        user.setUserpic("/img/"+filename);
        System.out.println(path+File.separator+filename);
        System.out.println("userID:"+user.getUserID());
        userService.updateUser(user);
        model.addAttribute("User", user);
        return "redirect:/user/show";
    }


}
