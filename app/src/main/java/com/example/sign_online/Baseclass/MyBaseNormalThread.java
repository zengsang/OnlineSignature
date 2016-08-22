package com.example.sign_online.Baseclass;

import android.content.Context;

import com.example.sign_online.Tools.NetAccess.HttpConnSoap;

import java.io.InputStream;
import java.util.ArrayList;

/**所有的访问网络线程的共同点抽象到此层，继承这个父类
 * Created by 曾志强 on 2016/1/18.
 */
public class MyBaseNormalThread extends Thread{
    private Context context=null;
    private MyBaseHandler mybasehandler=null;
    private int msgwhat=0;
    private final static  int INTERNET_ERROR=0;
    //形参
    private ArrayList<String> list_form_param=new ArrayList<String>();
    //实参
    private ArrayList<String> list_real_param=new ArrayList<String>();
    //后台方法名
    private String methodname=null;
    public MyBaseNormalThread(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_form_param, ArrayList<String> list_real_param, String methodname){
        this.context=context;
        this.mybasehandler=mybasehandler;
        this.msgwhat=msgwhat;
        this.list_real_param=list_real_param;
        this.list_form_param=list_form_param;
        this.methodname=methodname;
    }

    @Override
    public void run() {
        super.run();
        InputStream inputStream= HttpConnSoap.GetInputstream(methodname,list_form_param, list_real_param);
        if(inputStream!=null){
            //网络没有问题,更新用户数据
            (new HttpConnSoap(context)).GetWebServre(inputStream,methodname);
            mybasehandler.sendEmptyMessage(msgwhat);
        }else{
            //网络错误
            mybasehandler.sendEmptyMessage(INTERNET_ERROR);
        }
    }
}
