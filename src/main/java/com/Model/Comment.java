package com.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Comment {
    private int CommentID;

    private int BlogID;

    private String UserID;

    private String CommentContent;

    private String Userpic;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date CommentTime;

    private Byte CommentStatus;

    private Byte isDeleted;

    public Comment(){
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

    public String getUserpic() {
        return Userpic;
    }

    public void setUserpic(String userpic) {
        Userpic = userpic;
    }
}
