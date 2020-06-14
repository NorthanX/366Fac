package com.Dao;

import com.Model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper

public interface UserDao {

    @Insert("insert into [User](UserID, Usermail, Userpic, Userpwd, Userinfo, Regtime, role) values (#{UserID},#{Usermail},#{Userpic},#{Userpwd},#{Userinfo},#{Regtime},#{role})")
    public void insertUser(User user);
    @Delete("delete from [User] where UserID=#{UserID}")
    public void deleteUser(String id);
    @Select("select UserID,Usermail,Userpic,Userpwd,Userinfo,Regtime,role from [User] where Usermail=#{Usermail}")
    public User getUserByMail(String mail);
    @Select("select UserID,Usermail,Userpic,Userpwd,Userinfo,Regtime,role from [User] where UserID=#{UserID}")
    public User getUserById(String id);
    @Select("select Userpwd from [User] where UserID=#{UserID} or UserMail=#{UserMail}")
    public String getPwd(String s);
    @Select("select * from [User] ")
    public List<User>getAll();

    @Update({"update [User] set Usermail=#{Usermail},Userpic=#{Userpic},Userinfo=#{Userinfo} where UserID=#{UserID}"})
    public void updateUser(User user);

    @Update("update [user] set Userpic=#{Userpic} where UserID=#{UserID}")
    public void updateUserPic(String UserID, String Userpic);
}
