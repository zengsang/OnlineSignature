package com.example.sign_online.Thread;

import android.content.Context;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyBaseNormalThread;

import java.util.ArrayList;

/**当用户接收完文件后，删除服务端的文件，减小服务端的压力
 * Created by 曾志强 on 2016/3/16.
 */
public class SDeleteTranslateFileInServer extends MyBaseNormalThread{
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
        list_form_param.add("filename");
        list_form_param.add("receiveport");
    }
    public SDeleteTranslateFileInServer(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_real_param){
        super(context,mybasehandler,msgwhat,list_form_param,list_real_param,"Deletetranslatefile");
    }

    @Override
    public void run() {
        super.run();
    }
}
