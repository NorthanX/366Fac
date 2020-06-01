package Dao;

import entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.sql.Date;
import java.util.List;

@Mapper
public interface UserDao {

    @Insert("insert into user(UserID Usermail Userpic Userpwd Userinfo Regtime role) values (${UserID},${Usermail},${Userpic},${Userpwd},${Userinfo},${Regtime},${role})")
    public void insertUser(User user);
    @Delete("delete from user where UserID=#{UserID}")
    public void deleteUser(String id);
    @Select("select UserID,Usermail,Userpic,Userpwd,Userinfo,Regtime,role from user where Usermail=${Usermail}")
    public User getUserByMail(String mail);
    @Select("select UserID,Usermail,Userpic,Userpwd,Userinfo,Regtime,role from user where UserID=${UserID}")
    public User getUserById(String id);
}
