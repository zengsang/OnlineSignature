package com.example.sign_online.Thread;

import android.content.Context;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyBaseNormalThread;
import com.example.sign_online.Baseclass.MyBaseSelectThread;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/3/16.
 */
public class SGettranslateFilefromServer extends MyBaseSelectThread{
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
        list_form_param.add("filename");
        list_form_param.add("receiveport");
    }
    public SGettranslateFilefromServer(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_real_param){
        super(context,mybasehandler,msgwhat,list_form_param,list_real_param,"Gettranslatefilebyreceiver");
    }
    @Override
    public void run() {
        super.run();
    }

}
