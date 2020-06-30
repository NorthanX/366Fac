package com.controller;

import com.Model.*;
import com.Service.*;
import com.Util.FileUtil;
import com.Util.MD2HTML;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    @Autowired
    private AccessoryService accessoryService;
    /**
     * add blog, add tag
     * */
    @RequestMapping("/writeBlog")
    public String writeBlog(Model model){
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        return "WriteBlog";
    }

    @RequestMapping("/AddBlog")
    public String addBlog(@RequestParam("title")String title,
                          @RequestParam("tag")String tag,
                          @RequestParam("content")String content,@RequestParam("category") String type, Model model){
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setAuthorID(user.getUserID());
        blog.setCreateDate(new Date());
        blog.setCommentCount(0);
        blog.setType(type);
        //blog.setContent(MD2HTML.markdown2Html(content));
        blog.setContent(content);
        blog.setLikes(0);
        blogService.addBlog(blog);
        int articleId =blog.getBlogID();
//还需要设置tag，设置tag表，设置category表，设置blogId(自增)
        //假设tags是由,隔开的一个字符串
        tag.replaceAll(" ","");
//        System.out.println("tag: "+tag);
        String[] tags = tag.split(",");
//        System.out.println("tags: "+tags);
        for (String tagName:tags){
            Tag tmpTag = tagService.getByName(tagName);
            //if it is a new tag,create a new tag
//            System.out.println("tmptag: "+tmpTag);
            if (tmpTag==null){
//                System.out.println("create a tag: "+tagName);
                Tag tag1=new Tag();
                tag1.setTagName(tagName);
                tag1.setTagNum(1);
                tagService.addTag(tag1);
//                System.out.println("add tag: "+tag1.getTagName());
                int TagID=tag1.getTagID();
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
        Blog latestBlog=blogService.getLatestBlog();
        return "redirect:/blog/"+latestBlog.getBlogID();
    }

    @RequestMapping("/EditABlog/{blogId}")
    public String editBlog(@PathVariable("blogId")int id, Model model){
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }

        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blog",blog);
        return "EditBlog";
    }
    @RequestMapping("/SaveBlog")
    public String editBlog(@RequestParam("title")String title, @RequestParam("content")String content, @RequestParam("id")int blogId){
        blogService.updateBlog(content,blogId,title);
        return "redirect:/blog/"+blogId;
    }

    @RequestMapping("/blog/{blogId}")
    public String tagsOfBlog(Model model, @PathVariable("blogId")int blogId){
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }

        Blog blog = blogService.getBlogById(blogId);
        List<Tag> tags=tagService.getTagByBlogID(blogId);
        List<Comment> comments = commentService.getComment(blogId);
        List<Accessory> accessories = accessoryService.getAccessory(blogId);
//        int likeNum = blogService.getLikeNum(blogId);
        blog.setContent(MD2HTML.markdown2Html(blog.getContent()));
//        System.out.println(blogId);
//        for (Comment comment:comments){
//            System.out.println("comment:"+comment.getCommentID());
//        }
        /*System.out.println(user.getUserID());
        System.out.println(blogId);*/
        if (accessories.size()==0){
            model.addAttribute("hasA",0);
//            System.out.println("hasA:"+0);
        }
        else {
            model.addAttribute("hasA",1);
//            System.out.println("hasA:"+1);
        }


        model.addAttribute("blog",blog);
        model.addAttribute("tags",tags);
        User author = userService.getUserById(blog.getAuthorID());
        model.addAttribute("Author",author);
        model.addAttribute("comments",comments);
        model.addAttribute("accessories",accessories);
        boolean likeBlog;
        if (user!=null){
            likeBlog=blogService.UserLiked(user.getUserID(),blogId);
            if(likeBlog){
                //点赞过
                model.addAttribute("liked",1);
            } else {
                model.addAttribute("liked",0);
            }
        }
        else {
            model.addAttribute("liked",0);
        }

        if (user==null){
            model.addAttribute("isAuthor",0);
        }
        else {
            if (blog.getAuthorID().equals(user.getUserID())){
                model.addAttribute("isAuthor",1);
            }
            else {
                model.addAttribute("isAuthor",0);
            }
        }

        return "BrowseBlog";
    }

    @RequestMapping("/blog/{blogId}/like")
    public String likeBlog(@PathVariable("blogId") int blogId, Model model){
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }

        blogService.like(user.getUserID(),blogId);
        int liked;
        if(blogService.UserLiked(user.getUserID(),blogId))liked=1;
        else liked=0;
//        System.out.println("like:"+liked);
        model.addAttribute("liked",liked);

        return "redirect:/blog/{blogId}";
    }



//    //暂不加入分页
    //暂不考虑tag
//    @RequestMapping(value = "/tag/{TagID}", method = RequestMethod.GET)
//    public String tag(Model model, @PathVariable("TagID")int TagID){
//        List<Blog> blogs=blogService.getByTag(TagID);
//        List<ViewObject> viewobjects = new ArrayList<>();
//        for(Blog blog:blogs){
//            ViewObject vo = new ViewObject();
//            List<Tag> tags = tagService.getTagByBlogID(blog.getBlogID());
//            vo.set("blog",blog);
//            vo.set("tags",tags);
//            viewobjects.add(vo);
//        }
//        model.addAttribute("viewobjects",viewobjects);
//        return "Tag";
//    }

    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/UpdateImage", method = RequestMethod.POST)
    public JSONObject upLoad(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file
    ) {
        JSONObject object = new JSONObject();
        try {
        
            String realPath;
            realPath = "/root/accessory";
            
            File myFlie = new File(realPath);
            if (!myFlie.exists()) {
                myFlie.mkdirs();
            }
            //获取上传文件的文件名
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            fileName = uuid + "_" + fileName;
            file.transferTo(new File(realPath, fileName));
//            System.out.println("-----------filePath: "+realPath);
            object.put("success", 1);
            object.put("message", "上传成功");
            object.put("url", "/upload/"+ fileName);
        } catch (Exception e) {

            object.put("success", 0);
            object.put("message", "上传失败");
        }
        return object;
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
        if (user==null){
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
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
        }
        return "index";
    }

    @RequestMapping("/blog/{blogId}/download/{filename}")
    public String download(@PathVariable("filename") String filename,
                         @PathVariable("blogId") int BlogId) throws IOException {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        // 设置信息给客户端不解析
        String type = new MimetypesFileTypeMap().getContentType(filename);
        // 设置contenttype，即告诉客户端所发送的数据属于什么类型
        response.setHeader("Content-type",type);
        // 设置编码
        String hehe = new String(filename.getBytes("utf-8"), "iso-8859-1");
        // 设置扩展头，当Content-Type 的类型为要下载的类型时 , 这个信息头会告诉浏览器这个文件的名字和类型。
        response.setHeader("Content-Disposition", "attachment;filename=" + hehe);
        FileUtil.download(BlogId+"/"+filename, response);
        System.out.println("file: "+BlogId+"/"+filename);
        return "redirect:/blog/{blogId}";
    }

    @RequestMapping("/blog/{blogId}/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @PathVariable("blogId") int BlogId,HttpServletResponse response) throws IOException {
        User user = userService.getUserById(hostHolder.getUser().getUserID());
        boolean isEmpty = file.isEmpty();
        System.out.println("\tisEmpty=" + isEmpty);
        if (isEmpty) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out=response.getWriter();
            out.print("<script >alert('文件为空！');</script>");
            out.print("<script >location='http://106.14.12.11:8080/blog/"+BlogId+"';</script>");
            out.flush();
            return "";
        }
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        // 定义上传文件保存路径
        String path = "/root/accessory/"+BlogId+"/";;
        // 新建文件
        File filepath = new File(path, filename);
        System.out.println("toDirectory："+filepath);
        System.out.println("filepath.getParentFile().exists(): "+(filepath.getParentFile().exists()));
        System.out.println("filepath.getParentFile(): "+filepath.getParentFile());
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
            System.out.println("blogcontroller 351 mkdir");
        }
        try {
            // 写入文件
            //file.transferTo(new File(path + File.separator + filename));
            file.transferTo(new File(path + filename));
            System.out.println("write");

        } catch ( IOException e) {
            e.printStackTrace();
        }
        // 将src路径发送至html页面
        Accessory accessory=new Accessory();
        accessory.setAddress(filename);
        accessory.setBlogID(BlogId);
        accessoryService.addAccessory(accessory);

        return "redirect:/blog/{blogId}";
    }

    @RequestMapping("/blog/{blogId}/delete/{filename}")
    public String deleteA(@PathVariable("filename") String filename, Model model,
                          @PathVariable("blogId") int BlogId,HttpServletResponse response){

        File file = new File("/root/accessory/"+BlogId+"/"+filename);

        if (file.exists()) {
            file.delete();
            accessoryService.deleteAccessory(filename,BlogId);
            System.out.println("文件已经被成功删除");
        }
        return "redirect:/blog/{blogId}";
    }
}
