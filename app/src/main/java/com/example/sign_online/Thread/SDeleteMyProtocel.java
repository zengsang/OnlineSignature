package com.example.sign_online.Thread;

import android.content.Context;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyBaseNormalThread;

import java.util.ArrayList;

/**
 * Created by 曾志强 on 2016/4/23.
 */
public class SDeleteMyProtocel extends MyBaseNormalThread {
    private Context context=null;
    private MyBaseHandler mybasehandler=null;
    private int msgwhat=0;
    private final static  int INTERNET_ERROR=0;
    //实参
    private ArrayList<String> list_real_param=new ArrayList<String>();
    //形参
    private static ArrayList<String> list_form_param=new ArrayList<String>();
    static {
        //初始化形参
        list_form_param.add("userEmail");
        list_form_param.add("protocelMoudelName");
        list_form_param.add("protocelName");
    }
    public SDeleteMyProtocel(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_real_param){
        super(context,mybasehandler,msgwhat,list_form_param,list_real_param,"DeleteMyProtocel");
    }

    @Override
    public void run() {
        super.run();
    }
}
