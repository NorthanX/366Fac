package com.Model;

public class tinyBlog {
    private int id;
    private int BlogID;
    private String title;
    private int year;
    private int month;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlogID() {
        return BlogID;
    }

    public void setBlogID(int blogId) {
        BlogID = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
