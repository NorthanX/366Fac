package com.controller;

import com.Dao.BlogDao;
import com.Model.Blog;
import com.Model.Tag;
import com.Model.ViewObject;
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
private TagService tagService;
    @RequestMapping("/search")
    public String searchBlog(@RequestParam String keyWord, Model model){
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
        model.addAttribute("keywords",keyWord);
        model.addAttribute("vos",vos);
        return "SearchResult";    }

}
