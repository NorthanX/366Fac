package com.Service;

import com.Dao.CommentDao;
import com.Model.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    //存放迭代找出的所有子代的集合

    public void addComment(Comment comment){
        commentDao.insertComment(comment);
    }

    public List<Comment> getComment(int BlogID){
        return commentDao.getCommentByBlogID(BlogID);
    }

    public void deleteComment(int CommentID){
        commentDao.deleteComment(CommentID);
    }
}
