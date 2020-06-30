package com.Dao;

import com.Model.Accessory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccessoryDao {

    @Insert("insert into accessory(BlogID,Address) values (#{BlogID},#{Address})")
    public void insertAccessory(Accessory accessory);
    @Delete("delete from accessory where BlogID=#{BlogID}")
    public void deleteBlog(int BlogID);
    @Delete("delete from accessory where Address=#{Address} and BlogID=#{BlogID}")
    public void deleteAccessory(String Address, int BlogID);
    @Select("select BlogID,Address from accessory where BlogID=#{BlogID}")
    public List<Accessory> getAccessoryByID(int ID);
}
