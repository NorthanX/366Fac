package com.Dao;

import com.Model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface UserDao {

    @Insert("insert into user(UserID, Usermail, Userpic, Userpwd, Userinfo, Regtime, role) values (#{UserID},#{Usermail},#{Userpic},#{Userpwd},#{Userinfo},#{Regtime},#{role})")
    public void insertUser(User user);
    @Delete("delete from user where UserID=#{UserID}")
    public void deleteUser(String id);
    @Select("select UserID,Usermail,Userpic,Userpwd,Userinfo,Regtime,role from user where Usermail=#{Usermail}")
    public User getUserByMail(String mail);
    @Select("select UserID,Usermail,Userpic,Userpwd,Userinfo,Regtime,role from user where UserID=#{UserID}")
    public User getUserById(String id);
    @Select("select Userpwd from user where UserID=#{UserID} or UserMail=#{UserMail}")
    public String getPwd(String s);
    @Select("select * from user ")
    public List<User>getAll();

    @Update({"update user set Usermail=#{Usermail},Userpic=#{Userpic},Userinfo=#{Userinfo},Userpwd=#{Userpwd} where UserID=#{UserID}"})
    public int updateUser(User user);

    @Update("update user set Userpic=#{Userpic} where UserID=#{UserID}")
    public void updateUserPic(String UserID, String Userpic);
}
