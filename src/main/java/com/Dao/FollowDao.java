package com.Dao;

import com.Model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface FollowDao {

    @Insert("insert into follow(followerid,bloggerid) values(#{FollowerId},#{BloggerId})")
    public void followBlogger(String FollowerId,String BloggerId);

    @Delete("delete from follow where FollowerId=#{FollowerId} and BloggerId=#{BloggerId}")
    public void unfollow(String FollowerId,String BloggerId);

    @Select("select * from user where userid in (select BloggerId from follow where FollowerId=#{FollowerId})")
    public List<User> MyFollowBlogger(String FollowerId);

    @Select("select count(*) from follow where BloggerId=#{BloggerId} and FollowerId=#{FollowerId} ")
    public int followed(String FollowerId,String BloggerId);

    @Select("select * from user where userid in (select BloggerId from follow where BloggerId=#{UserId})")
    public List<User> MyFollowers(String UserId);

    @Select("select count(*) from follow where BloggerId=#{BloggerId}")
    public int getFollowerCount(String BloggerId);

    @Select("select count(*) from follow where FollowerId=#{FollowerId}")
    public int getBloggerCount(String FollowerId);



}
