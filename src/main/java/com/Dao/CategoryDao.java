package com.Dao;

import com.Model.Category;
import com.Model.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface CategoryDao {

    @Insert("insert into category(BlogID, TagID) values(#{BlogID},#{TagID})")
    @Options(useGeneratedKeys = true, keyProperty = "ID", keyColumn = "ID")
    public int insertCategory(Category category);
    @Delete("delete from category where BlogID=#{BlogID}")
    public int deleteCategory(int ID);
    @Select("select ID BlogID TagID where BlogID=#{BlogID}")
    public int getCategoryByBlogID(int ID);
    @Select("select ID BlogID TagID where TagID=#{TagID}")
    public int getCategoryByTagID(int ID);
    @Select("select TagID,TagName from tag where TagID in (select TagID from category where BlogID=#{BlogID})")
    public List<Tag> getTagsByBlogID(int id);
}
