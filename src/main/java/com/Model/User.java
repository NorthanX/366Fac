package com.Model;


import java.util.Date;
import java.text.SimpleDateFormat;

public class User {
    private String UserID;
    private String Usermail;
    private String Userpic;
    private String Userpwd;
    private String Userinfo;
    private Date Regtime;
    private String Role;

    public User() {
    }

    public User(String name, String mail) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.format(date);
        this.UserID = name;
        this.Usermail = mail;
        this.Userpic = "/img/defaultUser.ico";
        this.Userpwd = "";
        this.Userinfo = "";
        this.Regtime = date;
        this.Role = "user";
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        this.Role = role;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsermail() {
        return Usermail;
    }

    public void setUsermail(String usermail) {
        Usermail = usermail;
    }

    public String getUserpic() {
        return Userpic;
    }

    public void setUserpic(String userpic) {
        Userpic = userpic;
    }

    public String getUserpwd() {
        return Userpwd;
    }

    public void setUserpwd(String userpwd) {
        Userpwd = userpwd;
    }

    public String getUserinfo() {
        return Userinfo;
    }

    public void setUserinfo(String userinfo) {
        Userinfo = userinfo;
    }

    public Date getRegtime() {
        return Regtime;
    }

    public void setRegtime(Date regtime) {
        Regtime = regtime;
    }
}
