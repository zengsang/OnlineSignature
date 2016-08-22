package com.example.sign_online.Thread;

import android.content.Context;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyBaseNormalThread;

import java.util.ArrayList;

/**将用户的协议图片通过流上传到服务器
 * Created by 曾志强 on 2016/2/3.
 */
public class SUploadUserprotocelToServerThread extends MyBaseNormalThread {

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
        list_form_param.add("useremail");
        list_form_param.add("protocelmouldname");
        list_form_param.add("date");
        list_form_param.add("protocelstring");
    }
    public SUploadUserprotocelToServerThread(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_real_param){
        super(context,mybasehandler,msgwhat,list_form_param,list_real_param,"Saveuserprotocel");
    }


    @Override
    public void run() {
        super.run();
    }
}
