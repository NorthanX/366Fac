package com.Model;

import com.Util.MD2HTML;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Blog {
    private int BlogID;
    private String AuthorID;
    private String Content;
    private String ContentMD;
    private String ContentHTML;
    private int CommentCount;
    private Date CreateDate;
    private String Title;
    private String Type;
    private int Likes;
    private String LikeID;

    public int getBlogID() {
        return BlogID;
    }

    public void setBlogID(int blogID) {
        BlogID = blogID;
    }

    public String getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(String authorID) {
        AuthorID = authorID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getContentMD() {
        return ContentMD;
    }

    public void setContentMD(String contentMD) {
        ContentMD = contentMD;
    }

    public String getContentHTML() {
        return ContentHTML;
    }

    public void setContentHTML(String contentHTML) {
        ContentHTML = contentHTML;
    }

    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public String getLikeID() {
        return LikeID;
    }

    public void setLikeID(String likeID) {
        LikeID = likeID;
    }

    public Blog(){}
    public Blog(int blogID, String authorID, String content, String title) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.format(date);
        this.BlogID = blogID;
        this.AuthorID = authorID;
        this.Content= MD2HTML.markdown2Html(content);
        this.CreateDate = date;
        this.Title=title;
        this.Likes=0;
    }


}
