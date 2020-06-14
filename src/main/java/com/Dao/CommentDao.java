package com.Dao;

import com.Model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    @Delete("delete from comment where CommentID=#{CommentID}")
    public void deleteComment(int ID);

    @Select("select CommentID UserID CommentContent BlogID CommentTime from comment where BlogID=#{BlogID}")
    public List<Comment> getCommentByBlogID(int ID);

    @Select("select * from comment where BlogID = #{BlogID} and ParentCommentID = -1")
    List<Comment> findByBlogIDAndParentCommentNull(int BlogID);

    @Select("select * from comment where CommentID = #{CommentID}")
    Comment getCommentByCommentId(int CommentID);

    @Insert("insert into comment(CommentID UserID CommentContent BlogID CommentTime) values(#{CommentID},#{UserID},#{CommentContent},#{BlogID},#{CommentTime})")
    @Options(useGeneratedKeys = true, keyProperty = "CommentID", keyColumn = "CommentID")
    void insertComment(Comment comment);

    @Select("select * from comment where ParentCommentID = #{ParentCommentID} and ParentCommentID != -1")
    List<Comment> findReplyCommentsByParentCommentId(int ParentCommentID);

    @Select("select count(*) from comment where BlogID = #{BlogID}")
    int getTotalComments(int BlogID);
}
