package com.controller;

import com.Model.Accessory;
import com.Model.Blog;
import com.Model.HostHolder;
import com.Model.User;
import com.Service.AccessoryService;
import com.Service.BlogService;
import com.Service.FollowService;
import com.Service.UserService;
import com.Util.ConstantPool;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/user/")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private HostHolder hostHolder;

    @Resource
    private FollowService followService;

    @Resource
    private BlogService blogService;

    @Resource
    private AccessoryService accessoryService;

    @RequestMapping("/editUser")
    public String editInfo(Model model){
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("isPic", 1);
        return "EditInfo";
    }

    // 上传头像
    @RequestMapping("/uploadPic")
    public String upload(@RequestParam("file") MultipartFile file, Model model,
                         HttpServletResponse response) throws IOException {
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        boolean isEmpty = file.isEmpty();
        System.out.println("\tisEmpty=" + isEmpty);
        if (isEmpty) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out=response.getWriter();
            out.print("<script >alert('图片为空！');</script>");
            out.print("<script >location='http://106.14.12.11:8080/user/editUser';</script>");
            out.flush();
            return "";
        }
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        String filename1;
        String suffixName = filename.substring(filename.lastIndexOf("."));  // 后缀名
        // 定义上传文件保存路径
        String path = "/accessory/";//图片要上传到哪个文件夹，部署时注意
        // 新文件名
        filename = UUID.randomUUID() + suffixName;
        if (!suffixName.equals(ConstantPool.SEPARATORBMP)
            && !suffixName.equals(ConstantPool.SEPARATORGIF)
            && !suffixName.equals(ConstantPool.SEPARATORJPEG)
            && !suffixName.equals(ConstantPool.SEPARATORJPG)
            && !suffixName.equals(ConstantPool.SEPARATORPNG)){
            model.addAttribute("isPic",0);
            model.addAttribute("create",0);
            model.addAttribute("user",user);
            System.out.println("NOT PICTURE!!!!!");
            //PrintWriter out=response.getWriter();
            //out.print("<script type='text/javascript' charset='UTF-8'>alert('Not a picture!');</script>");
            return "EditInfo";
        }
        // 新建文件
        File filepath = new File(path, filename);
        // 判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            // 写入文件
            //file.transferTo(new File(path + File.separator + filename));
            file.transferTo(new File("/root"+path + filename));
            Thumbnails
                    .of("/root"+path + filename)
                    .size(128, 128)
                    .outputQuality(1.0f)
                    .keepAspectRatio(false)
                    .toFile("/root"+path + filename);
        } catch ( IOException e) {
            e.printStackTrace();
        }
        // 将src路径发送至html页面
        user.setUserpic("/upload/"+filename);
        System.out.println(path+filename);
//        System.out.println("userID:"+user.getUserID());
        userService.updateUser(user);
//        System.out.println("UserController 111 : "+user.getUserpic());
        return "redirect:/user/editUser";
    }

    @RequestMapping("/editConfirm")
    public String editInfoC(Model model, @ModelAttribute("info") String info, @ModelAttribute("username") String username){
        User user=userService.getUserById(hostHolder.getUser().getUserID());
        user.setUserinfo(info);
        int updateSuccess=userService.updateUser(user);
        if (updateSuccess!=1){
            //加入警告信息
            return "redirect:/user/editUser";
        }
        else {
            return "redirect:/user/show";
        }
    }

    @RequestMapping("/show")
    public String showUser(Model model){
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        return "UserInfo";
    }

    @RequestMapping("/showBlogger")
    public String showFollower(Model model){
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }

        List<User> bloggers=followService.MyFollowBlogger(user.getUserID());
        model.addAttribute("bloggers",bloggers);
        return "MyInterest";
    }

    @RequestMapping("/{userID}/blogList/follow")
    public String follow(@PathVariable("userID") String authorID, Model model){
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        User author = userService.getUserById(authorID);

        if (user.getUserID().equals(authorID)){
            model.addAttribute("isUser",true);
        }
        else {
            model.addAttribute("isUser",false);
        }
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        model.addAttribute("author",author);

        List<Blog> blogList=blogService.getByAuthor(authorID);
        int followee=followService.MyFollowBlogger(authorID).size();
        int followers=followService.MyFollowers(authorID).size();
        Boolean following=followService.followed(user.getUserID(),authorID);

        followService.followBlogger(user.getUserID(),authorID);
        model.addAttribute("blogList",blogList);
        model.addAttribute("following",true);
        model.addAttribute("follower",followers+1);
        model.addAttribute("blogger",followee);

        return "redirect:/user/{userID}/blogList";
    }

    @RequestMapping("/{userID}/blogList/unfollow")
    public String unfollow(@PathVariable("userID") String authorID, Model model){
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        User author = userService.getUserById(authorID);

        if (user.getUserID().equals(authorID)){
            model.addAttribute("isUser",true);
        }
        else {
            model.addAttribute("isUser",false);
        }

        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        model.addAttribute("author",author);

        List<Blog> blogList=blogService.getByAuthor(authorID);
        int followee=followService.MyFollowBlogger(authorID).size();
        int followers=followService.MyFollowers(authorID).size();
        Boolean following=followService.followed(user.getUserID(),authorID);

        followService.unFollowBlogger(user.getUserID(),authorID);
        model.addAttribute("blogList",blogList);
        model.addAttribute("following",false);
        model.addAttribute("follower",followers-1);
        model.addAttribute("blogger",followee);

        return "redirect:/user/{userID}/blogList";
    }

    @RequestMapping("/{userID}/blogList")
    public String showBlogList(@PathVariable("userID") String authorID, Model model){
        User user = hostHolder.getUser();
        if (user!=null){
            user=userService.getUserById(user.getUserID());
        }
        User author = userService.getUserById(authorID);

        if (user != null){
            if (user.getUserID().equals(authorID)){
                model.addAttribute("isUser",true);
            }
            else {
                model.addAttribute("isUser",false);
            }
        }
        else {
            model.addAttribute("isUser",false);
        }

        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        model.addAttribute("author",author);

        List<Blog> blogList=blogService.getByAuthor(authorID);
        int followers=followService.MyFollowers(authorID).size();
        int followee=followService.MyFollowBlogger(authorID).size();
        Boolean following=false;
        if (user!=null){
            following=followService.followed(user.getUserID(),authorID);
        }

        model.addAttribute("blogs",blogList);
        model.addAttribute("follower",followers);
        model.addAttribute("blogger",followee);
        model.addAttribute("following",following);
        System.out.println(followee);
        System.out.println(followers);
        return "BlogList";
    }


    @RequestMapping("/{userID}/blogList/delete/{blogId}")
    public String deleteBlog(@PathVariable("userID") String authorID, @PathVariable("blogId") int blogId,
                             Model model){
        List<Accessory> accessories = accessoryService.getAccessory(blogId);

        for (Accessory accessory:accessories){
            String filename=accessory.getAddress();
            File file = new File("/root/accessory/"+blogId+"/"+filename);
            if (file.exists()) {
                file.delete();
                accessoryService.deleteAccessory(filename,blogId);
                System.out.println("文件已经被成功删除");
            }
        }

        blogService.deleteById(blogId);
        return "redirect:/user/{userID}/blogList";
    }

    @RequestMapping(value = "/change",method = RequestMethod.POST)
    public String changePWD(@RequestParam("code")String code, @RequestParam("email") String email, @RequestParam("password")String password, @CookieValue("ticket") String ticket,Model model){
        User user = hostHolder.getUser();

        Map<String,String> map = new HashMap<String,String>();
        map=userService.changePassword(user,code,password,email);
        if (map.containsKey("success")){
            userService.logout(ticket);
            return "redirect:/in";
        }
        String msg=map.get("msg");
        model.addAttribute("msg",msg);
        return "ChangePassword";
    }


}
