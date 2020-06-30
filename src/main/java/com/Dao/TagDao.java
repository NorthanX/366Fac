package com.Dao;

import com.Model.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TagDao {

    @Insert("insert into tag(TagName,TagNum) values(#{TagName},#{TagNum})")
    @Options(useGeneratedKeys = true, keyProperty = "TagID", keyColumn = "TagID")
    int insertTag(Tag tag);
    @Select("select TagID,TagName from tag where TagName like #{TagName}")
    Tag getByName(String name);
    @Select("select TagID,TagName from tag")
    List<Tag> selectAll();
    @Update("update tag set TagNum=TagNum+1 where TagID=#{TagID}")
    public int incNum(int TagID);
}
