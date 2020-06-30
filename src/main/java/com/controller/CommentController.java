package com.controller;

import com.Model.Comment;
import com.Model.HostHolder;
import com.Model.User;
import com.Service.BlogService;
import com.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private HostHolder hostHolder;



    @RequestMapping("/blog/{blogId}/comment")
    public String addComment(@PathVariable("blogId") Integer blogId, Model model, @RequestParam("commentContent") String commentContent){
        User user = hostHolder.getUser();
        if (user==null){
            model.addAttribute("create",1);
        }else {
            model.addAttribute("create",0);
            model.addAttribute("user",user);
        }

        Comment comment=new Comment();
        comment.setCommentContent(commentContent);
        comment.setCommentTime(new Date());
        comment.setUserID(user.getUserID());
        comment.setUserpic(user.getUserpic());
        comment.setBlogID(blogId);

        commentService.addComment(comment);

        return "redirect:/blog/{blogId}";

    }
}
