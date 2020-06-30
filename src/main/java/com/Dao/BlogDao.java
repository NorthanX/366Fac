package com.Dao;

import com.Model.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogDao {

    @Insert("insert into blog(AuthorId,Content,CommentCount, CreateDate,Title,Type,Likes,LikeID) values(#{AuthorID},#{Content},#{CommentCount},#{CreateDate},#{Title},#{Type},0, '1,') ")
    @Options(useGeneratedKeys = true, keyProperty = "BlogID", keyColumn = "BlogID")
    int insertBlog(Blog blog);

    @Delete("delete from blog where BlogID=#{id}")
    void deleteById(int id);

    @Select("select count(*) from blog")
    int getCount();

    @Update("update blog set content=#{content},title=#{title} where blogId=#{blogId}")
    public void updateBlog(String content, int blogId,String title);

    @Select("select * from blog where BlogID=#{blogId}")
    Blog getById(int blogId);

    @Select("select * from blog where Title like #{title} order by BlogID desc" )
    List<Blog> getByTitle(String title);

    @Select("select * from blog where Title like CONCAT(CONCAT('%',#{title},'%')) order by BlogID desc")
    List<Blog> getByTitleEX(String title);

    //////////////////////////////////////////////////选择作者的blog
    @Select("select * from blog where AuthorId=#{author} order by BlogID desc")
    List<Blog> getByAuthor(String author);

    ///////////////////////////////////////////////////选择tag的blog
    @Select("select BlogID,AuthorId,Content,CommentCount, CreateDate,Title,Type,Likes,LikeID from blog where BlogID in (select BlogID from tag where tag=#{tag}) order by BlogID desc)")
    List<Blog> getByTag(int tag);

    @Select("select BlogID,AuthorId,Content,CommentCount, CreateDate,Title,Type,Likes,LikeID from blog where BlogID in (select category.BlogID from category,tag where tag.TagName like #{tag} and category.TagID=tag.TagID )")
    List<Blog> getByTagName(String tag);

    @Select("select BlogID,AuthorId,Content,CommentCount, CreateDate,Title,Type,Likes,LikeID from blog order by BlogID desc")
    List<Blog> getLatestBlogs();

    @Select("select CommentCount from blog where BlogID=#{BlogID}")
    int getCommentCount(int BlogID);
    /**
     * liked return !=0; not liked return 0
     */
    @Select("select find_in_set(#{UserId},LikeID) from blog where BlogID=#{BlogID}")
    Integer liked(String UserId,int BlogID);

    @Update("update blog set likeID=CONCAT(likeID,#{userID},','),Likes=Likes+1 where blogId=#{BlogID}")
    Integer likeBlog(String userID,int BlogID);

    @Select("select BlogID,AuthorId,Content,CommentCount, CreateDate,Title,Type,Likes,LikeID from blog where Type=#{Type}")
    List<Blog> getByType(String type);

    @Select("select Likes from blog where BlogID=#{BlogID}")
    public int getLikeNum(int BlogID);

    @Select("select * from blog where BlogID in (select Max(BlogID) from blog)")
    Blog getLatestBlog();
}
