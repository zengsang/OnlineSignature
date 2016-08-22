package com.example.sign_online.Thread;

import android.content.Context;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyBaseSelectThread;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/2/8.
 */
public class SSelectProtocelMouldThread extends MyBaseSelectThread{
    private Context context=null;
    private MyBaseHandler mybasehandler=null;
    private int msgwhat=0;
    private final static  int INTERNET_ERROR=0;
    //方法名
    private String methodname=null;
    //查询记录数目
    private int number;
    //形参
    private static ArrayList<String> list_form_param=new ArrayList<String>();
    //实参
    private ArrayList<String> list_real_param=new ArrayList<String>();
    static{
        list_form_param.add("useremail");
    }
    public SSelectProtocelMouldThread(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_real_param){
        super(context,mybasehandler,msgwhat,list_form_param,list_real_param,"Getprotocelmould");
    }
    @Override
    public void run() {
        super.run();
    }
}
