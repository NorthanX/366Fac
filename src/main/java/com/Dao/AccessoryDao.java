package com.Dao;

import com.Model.Accessory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AccessoryDao {

    @Insert("insert into accessory(BlogID Address) values (#{BlogID},#{Address})")
    public void insertAccessory(Accessory accessory);
    @Delete("delete from accessory where BlogID=#{BlogID}")
    public void deleteBlog(String BlogID);
    @Delete("delete from accessory where Address=#{Address}")
    public void deleteAccessory(String Address);
    @Select("select BlogID,Address from accessory where BlogID=#{BlogID}")
    public void getAccessoryByID(String ID);
}
