package com.Dao;

import com.Model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    @Delete("delete from comment where CommentID=#{CommentID}")
    public void deleteComment(int ID);

    @Select("select * from comment where BlogID=#{BlogID}")
    public List<Comment> getCommentByBlogID(int BlogID);

    @Select("select * from comment where CommentID = #{CommentID}")
    Comment getCommentByCommentId(int CommentID);

    @Insert("insert into comment(UserID,CommentContent,BlogID,CommentTime,Userpic) values(#{UserID},#{CommentContent},#{BlogID},#{CommentTime},#{Userpic})")
    @Options(useGeneratedKeys = true, keyProperty = "CommentID", keyColumn = "CommentID")
    void insertComment(Comment comment);

    @Select("select count(*) from comment where BlogID = #{BlogID}")
    int getTotalComments(int BlogID);
}
