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
    private List<Comment> tempReplys = new ArrayList<>();


    public List<Comment> listCommentByBlogID(int blogId) {
        //先找所以最上级的评论
        List<Comment> comments = commentDao.findByBlogIDAndParentCommentNull(blogId);

        return eachComment(comments);
    }


    public void saveComment(Comment comment) {
        int parentCommentId = comment.getParentCommentID();
        if (parentCommentId != -1) {
            comment.setParentComment(commentDao.getCommentByCommentId(parentCommentId));
        } else {
            comment.setParentComment(null);
        }
        comment.setCommentTime(new Date());
        commentDao.insertComment(comment);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys1 = commentDao.findReplyCommentsByParentCommentId(comment.getCommentID());
            for (Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                reply1.setParentComment(comment);
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);

            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (commentDao.findReplyCommentsByParentCommentId(comment.getCommentID()).size()>0) {
            List<Comment> replys = commentDao.findReplyCommentsByParentCommentId(comment.getCommentID());
            for (Comment reply : replys) {
                reply.setParentComment(comment);
                tempReplys.add(reply);
                if (commentDao.findReplyCommentsByParentCommentId(reply.getCommentID()).size()>0) {
                    recursively(reply);
                }
            }
        }
    }


    public int getTotalComments(int BlogID) {
        return commentDao.getTotalComments(BlogID);
    }

    public void deleteComment(int ID){
        commentDao.deleteComment(ID);
    }

}
