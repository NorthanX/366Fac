package com.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Comment {
    private int CommentID;

    private int BlogID;

    private String UserID;

    private String email;

    private String CommentContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date CommentTime;

    private Byte CommentStatus;

    private Byte isDeleted;

    private Byte isAdmin;//?

    private Comment ParentComment;

    private Blog blog;//?

    private int ParentCommentID;

    private List<Comment> ReplyComments;

    public Comment(){
    }

    public List<Comment> getReplyComments() {
        return ReplyComments;
    }

    public int getCommentID() {
        return CommentID;
    }

    public void setCommentID(int commentID) {
        this.CommentID = commentID;
    }

    public int getBlogID() {
        return BlogID;
    }

    public void setBlogID(int blogID) {
        this.BlogID = blogID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        this.CommentContent = commentContent;
    }

    public Date getCommentTime() {
        return CommentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.CommentTime = commentTime;
    }

    public Byte getCommentStatus() {
        return CommentStatus;
    }

    public void setCommentStatus(Byte commentStatus) {
        this.CommentStatus = commentStatus;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Comment getParentComment() {
        return ParentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.ParentComment = parentComment;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public int getParentCommentID() {
        return ParentCommentID;
    }

    public void setParentCommentID(int parentCommentID) {
        this.ParentCommentID = parentCommentID;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.ReplyComments = replyComments;
    }

}
