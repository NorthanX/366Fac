package com.controller;

import com.Dao.FollowDao;
import com.Model.*;
import com.Service.*;
import com.Util.MD2HTML;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.omg.CORBA.COMM_FAILURE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    /**
     * add blog, add tag
     * */
    @RequestMapping("/AddBlog")
    public String addBlog(@RequestParam("title")String title,
                          @RequestParam("tag")String tag,
                          @RequestParam("content")String content,@RequestParam("category") String type, Model model){
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setAuthorID(hostHolder.getUser().getUserID());
        blog.setCreateDate(new Date());
        blog.setCommentCount(0);
        blog.setType(type);
        blog.setContent(MD2HTML.markdown2Html(content));
        blog.setLikes(0);
        //blog.setContentHTML(MD2HTML.markdown2Html(content));//md转html方法
        //blog.setContentMD(content);
        int articleId = blogService.addBlog(blog);
//还需要设置tag，设置tag表，设置category表，设置blogId(自增)
        //假设tags是由,隔开的一个字符串
        tag.replaceAll(" ","");
        String[] tags = tag.split(",");
        for (String tagName:tags){
            Tag tmpTag = tagService.getByName(tagName);

            //if it is a new tag,create a new tag
            if (tmpTag==null){
                Tag tag1=new Tag();
                tag1.setTagName(tagName);
                tag1.setTagNum(1);
                int TagID=tagService.addTag(tag1);

                //category insert
                Category category = new Category();
                category.setTagID(TagID);
                category.setBlogID(articleId);
                tagService.addCategory(category);
            } else{
                tagService.incNum(tmpTag.getTagID());
                Category category=new Category();
                category.setBlogID(articleId);
                category.setTagID(tmpTag.getTagID());
                tagService.addCategory(category);
            }
        }

        return "UserInfo";
    }

    @RequestMapping("/EditABlog/{blogId}")
    public String editBlog(@PathVariable("blogId")int id, Model model){
        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blog",blog);
        return "EditBlog";
    }
    @RequestMapping("/SaveBlog")
    public String editBlog(@RequestParam("title")String title, @RequestParam("content")String content, @RequestParam("id")int blogId){
        blogService.updateBlog(content,blogId,title);
        return "UserInfo";
    }

    @RequestMapping("/blog/{BlogID}")
    public String tagsOfBlog(Model model, @PathVariable("BlogID")int blogId){
        Blog blog = blogService.getBlogById(blogId);
        List<Tag> tags=tagService.getTagByBlogID(blogId);
        List<Comment> comments=commentService.listCommentByBlogID(blogId);
        int totalComment=0;
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        for (Comment comment: comments){
            totalComment++;
            List<Comment> replyComments = comment.getReplyComments();
            for (Comment replyComment : replyComments) {
                totalComment++;
            }
        }
        System.out.println(blogId);
        model.addAttribute("blog",blog);
        model.addAttribute("tags",tags);
        model.addAttribute("totalComment", totalComment);
        model.addAttribute("comments",comments);
        return "ShowBlog";
    }

    //暂不加入分页
    @RequestMapping(value = "/tag/{TagID}", method = RequestMethod.GET)
    public String tag(Model model, @PathVariable("TagID")int TagID){
        List<Blog> blogs=blogService.getByTag(TagID);
        List<ViewObject> viewobjects = new ArrayList<>();
        for(Blog blog:blogs){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByBlogID(blog.getBlogID());
            vo.set("blog",blog);
            vo.set("tags",tags);
            viewobjects.add(vo);
        }
        model.addAttribute("viewobjects",viewobjects);
        return "Tag";
    }

    @RequestMapping("/UpdateImage")
    @ResponseBody
    public JSONObject imageUpload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file, HttpServletRequest request) {
        //路径
        String path = request.getSession().getServletContext().getRealPath("/");
        //使用硬盘路径替代path，防止重新部署Tomcat项目图片丢失，此处tomcat已经docbase指向了"D:/"
        //path = "D:/";
        //保存相对路径，解决同源问题
        String relativePath = "/static/img/";
        //文件名
        String trueFileName = file.getOriginalFilename();
        //后缀名
        String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
        //重新命名
        String fileName = System.currentTimeMillis() + "_" + (Math.random()*900+100) + suffix;
        //io
        File targetFile = new File(path+relativePath, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回给前端
        JSONObject res = new JSONObject();
        res.put("url", relativePath + fileName);
        res.put("success", 1);
        res.put("message", "upload success!");
        return res;
    }


    @RequestMapping("/writeBlog")
    public String writeBlog(Model model){
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        return "WriteBlog1";

    }
    // 执行上传
    @RequestMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        // 定义上传文件保存路径
        String path = "/static/upload";
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
        model.addAttribute("filename", "/images/"+filename);
        return "redirect:/AddBlog";
    }

    @RequestMapping(value = "/type/{type}",method = RequestMethod.GET)
    public String category(Model model, @PathVariable("type")String typeName){
        List<Blog> blogs = blogService.getByType(typeName);
        List<ViewObject> vos = new ArrayList<>();
        for (Blog blog:blogs){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByBlogID(blog.getBlogID());
            vo.set("blog",blog);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);

        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }
        return "index";
    }


    @RequestMapping("/news")
    public String bloggerNews(Model model){

        String userId = hostHolder.getUser().getUserID();
        List<ViewObject> vos = new ArrayList<>();
        for (User blogger:followService.MyFollowBlogger(userId)){
            for (Blog tmpBlog:blogService.getByAuthor(blogger.getUserID())){
                ViewObject vo = new ViewObject();
                List<Tag> tags = tagService.getTagByBlogID(tmpBlog.getBlogID());
                vo.set("blog",tmpBlog);
                vo.set("tags",tags);
                vos.add(vo);
            }
        }

        model.addAttribute("vos",vos);
        User user = hostHolder.getUser();
        if (user==null||"admin".equals(user.getRole())){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }
        return "index";
    }
}
