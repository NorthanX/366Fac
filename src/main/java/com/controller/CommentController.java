package com.controller;

import com.Model.Comment;
import com.Model.HostHolder;
import com.Service.BlogService;
import com.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping(path = "/addComment/{blogId}")
    public String addComment(@PathVariable("blogId") String  BlogID, @RequestParam("content")String content
            /*, @RequestParam(value = "next",required = false)String next*/){
        Comment comment = new Comment();
        int blogId = Integer.parseInt(BlogID);
        if (hostHolder.getUser()==null)
            return "redirect:/in?next=/article/"+blogId;
        else
            comment.setUserID(hostHolder.getUser().getUserID());
        comment.setCommentContent(content);
        comment.setCommentTime(new Date());
        comment.setCommentID(blogId);
        //comment.setStatus(0);
        commentService.saveComment(comment);

        int count = commentService.getTotalComments(blogId);
        //blogService.addConut(blogId,count);

        /*eventProducer.fireEvent(new EventModel().setType(EventType.COMMENT)
                .setActorId(hostHolder.getUser().getId())
                .setExts("username",hostHolder.getUser().getName())
                .setExts("email","zhenyutu@126.com")
                .setExts("blogId",String.valueOf(blogId)));*/

        return "redirect:/blog/"+blogId;
    }

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable int blogId, Model model) {
        List<Comment> comments = commentService.listCommentByBlogID(blogId);
        int totalComment = 0;
        for (Comment comment : comments) {
            totalComment++;
            List<Comment> replyComments = comment.getReplyComments();
            for (Comment replyComment : replyComments) {
                totalComment++;
            }
        }
        model.addAttribute("totalComment", totalComment);
        model.addAttribute("comments", comments);

        return "blog/single :: commentList";
    }
}
