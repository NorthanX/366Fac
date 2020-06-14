package com.Dao;

import com.Model.tinyBlog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface tinyBlogDao {
    ///////////////////////////选前十条最近的blog
    @Select("select top 10 blog.id,blog.title,year(blog.createDDate),month(blog.createDate from blog group by year(blog.createDate),month(blog.createDate))")
    public List<tinyBlog> getBlogSortByTime();
}
