package com.example.sign_online.Thread;

import android.content.Context;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.MyBaseNormalThread;

import java.util.ArrayList;

/**用来更新某一条用户信息，根据属性更新相应的某一列属性
 * Created by 曾志强 on 2016/1/30.
 */
public class SUpdateOneUsermsgThread extends MyBaseNormalThread {
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
        list_form_param.add("property");
        list_form_param.add("newvalue");
    }
    public SUpdateOneUsermsgThread(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_real_param){
        super(context,mybasehandler,msgwhat,list_form_param,list_real_param,"Soneupdate");
    }

    @Override
    public void run() {
        super.run();
    }
}
