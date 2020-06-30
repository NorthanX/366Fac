package com.controller;

import com.Dao.BlogDao;
import com.Model.*;
import com.Service.BlogService;
import com.Service.TagService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private BlogService blogService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    private TagService tagService;

    @RequestMapping("/search")
    public String searchBlog(@RequestParam("keyWord") String keyWord, Model model){
        if (keyWord==null){
            return "SearchResult";
        }
        List<ViewObject> vos=new ArrayList<>();
        for(Blog blog:blogService.search(keyWord)){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByBlogID(blog.getBlogID());
            vo.set("blog",blog);
            vo.set("tags",tags);
            vos.add(vo);
        }
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        model.addAttribute("keywords",keyWord);
        model.addAttribute("vos",vos);
        return "SearchResult";    }

    @RequestMapping("/searchTag")
    public String searchByTag(Model model,@RequestParam("keyWord") String tagName){
        if (tagName==null){
            return "SearchResult";
        }
        List<Blog> blogs = blogService.getByTagName(tagName);
        List<ViewObject> vos = new ArrayList<>();
//        model.addAttribute("blogs",blogs);
//        System.out.println("blognum: "+blogs.size());

        for (Blog blog:blogs){
            ViewObject vo = new ViewObject();
            List<Tag> tags = tagService.getTagByBlogID(blog.getBlogID());
            vo.set("blog",blog);
            vo.set("tags",tags);
            vos.add(vo);
        }
        model.addAttribute("vos",vos);
        model.addAttribute("keywords",tagName);
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }
        return "SearchResult";
    }
}
