package com.Dao;

import com.Model.Blog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlogDao {

    @Insert("insert into blog(AuthorId,[Content],CommentCount, CreateDate,Title,Type,Likes,LikeID) values(#{AuthorID},#{Content},#{CommentCount},#{CreateDate},#{Title},#{Type},#{Likes}, #{LikeID}) ")
    @Options(useGeneratedKeys = true, keyProperty = "BlogID", keyColumn = "BlogID")
    int insertBlog(Blog blog);

    @Delete("delete from blog where BlogID=#{id}")
    void deleteById(int id);

    @Select("select count(*) from blog")
    int getCount();

    @Update("update blog set [content]=#{content},title=#{title} where blogId=#{blogId}")
    public void updateBlog(String content, int blogId,String title);

    @Select("select * from blog where BlogID=#{blogId}")
    Blog getById(int blogId);

    @Select("select * from blog where '%'+#{title}+'%' like Title or '%'+Title+'%' like #{title}" )
    List<Blog> getByTitle(String title);

    @Select("select * from blog where Title like CONCAT(CONCAT('%',#{title},'%')) order by BlogID desc")
    List<Blog> getByTitleEX(String title);

    //////////////////////////////////////////////////选择作者的blog
    @Select("select top 5 BlogID,AuthorId,[Content],CommentCount, CreateDate,Title,Type,Likes,LikeID from blog where AuthorId=#{author}")
    List<Blog> getByAuthor(String author);

    ///////////////////////////////////////////////////选择tag的blog
    @Select("select BlogID,AuthorId,[Content],CommentCount, CreateDate,Title,Type,Likes,LikeID from blog where BlogID in (select BlogID from tag where tag=#{tag}) order by BlogID desc)")
    List<Blog> getByTag(int tag);

    @Select("select BlogID,AuthorId,[Content],CommentCount, CreateDate,Title,Type,Likes,LikeID from blog where BlogID in (select BlogID from tag where TagName like '%'+#{tag}+'%' or #{tag} like '%'+TagName+'%'))")
    List<Blog> getByTagName(String tag);

    @Select("select BlogID,AuthorId,[Content],CommentCount, CreateDate,Title,Type,Likes,LikeID from blog order by BlogID desc")
    List<Blog> getLatestBlogs();

    @Select("select CommentCount from blog where BlogID=#{BlogID}")
    int getCommentCount(int BlogID);
    /**
     * liked return !=0; not liked return 0
     */
    @Select("select PATINDEX(#{UserId}+',',LikeID) from blog where BlogID=#{BlogID}")
    int liked(String UserId,int BlogID);

    @Update("update blog set likeID=likeID+#{userID}+',' where blogId=#{BlogID}")
    int likeBlog(String userID,int BlogID);

    @Select("select BlogID,AuthorId,[Content],CommentCount, CreateDate,Title,Type,Likes,LikeID from blog where Type=#{Type}")
    List<Blog> getByType(String type);
}
