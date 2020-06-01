package Dao;

import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;

@Mapper
public interface UserDao {
    String TABLE_NAME = " user ";
    private String UserID;
    private String Usermail;
    private String Userpic;
    private String Userpwd;
    private String Userinfo;
    private Date Regtime;
    private String role;
}
