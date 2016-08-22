package com.example.sign_online.Baseclass;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 曾志强 on 2016/1/18.
 */
public class UserMsg implements Serializable {
    private String useremail = null;
    private String userpassword = null;
    private String username = null;
    private String userreallyname = null;
    private String userphonenumber = null;
    private String usersex = null;
    private String useryearold = null;
    private List<String> listpictureurl = null;
    //头像
    private Bitmap usericon = null;

    public Bitmap getUsericon() {
        return usericon;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserreallyname() {
        return userreallyname;
    }

    public void setUserreallyname(String userreallyname) {
        this.userreallyname = userreallyname;
    }

    public String getUserphonenumber() {
        return userphonenumber;
    }

    public void setUserphonenumber(String userphonenumber) {
        this.userphonenumber = userphonenumber;
    }

    public String getUsersex() {
        return usersex;
    }

    public void setUsersex(String usersex) {
        this.usersex = usersex;
    }

    public String getUseryearold() {
        return useryearold;
    }

    public void setUseryearold(String useryearold) {
        this.useryearold = useryearold;
    }

    public List<String> getListpictureurl() {
        return listpictureurl;
    }

    public void setListpictureurl(List<String> listpictureurl) {
        this.listpictureurl = listpictureurl;
    }

    public void setUsericon(Bitmap usericon) {
        this.usericon = usericon;
    }
}