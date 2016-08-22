package com.example.sign_online.Tools.DataCash;

import android.graphics.Bitmap;

import com.example.sign_online.Baseclass.UserMsg;

import java.io.Serializable;
import java.util.List;

/**用来存储常用到的某些值
 * Created by 曾志强 on 2016/1/20.
 */
public class Staticbean implements Serializable{
    //用于全局传递用户信息
    public static UserMsg userMsg=new UserMsg();
    //用于全局传递模板名
    public static List<String> listmouldproperty;
    //模板名，
    public static String protocelmouldname;
    //甲方签字图片
     public static Bitmap partabitmap;
    //乙方签字图片
    public static Bitmap partbbitmap;
    //协议提本身吐泡泡
    public static Bitmap protocelbitmap;
    //甲乙方签字合并图片
    public static Bitmap partaandpartbbitmap;
}
