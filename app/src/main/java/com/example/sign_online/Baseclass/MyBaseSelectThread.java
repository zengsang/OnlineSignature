package com.example.sign_online.Baseclass;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;

import com.example.sign_online.Tools.NetAccess.HttpConnSoap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**用于抽象出所有选择的访问网络进程的Thread,所有访问网络有关的Thread均继承自此Thread
 * Created by 曾志强 on 2016/2/8.
 */
public class MyBaseSelectThread extends Thread{
    private Context context=null;
    private MyBaseHandler mybasehandler=null;
    private int msgwhat=0;
    private final static  int INTERNET_ERROR=0;
    //方法名
    private String methodname=null;
    //形参
    private ArrayList<String> list_form_param=new ArrayList<String>();
    //实参
    private ArrayList<String> list_real_param=new ArrayList<String>();

    public MyBaseSelectThread(Context context, MyBaseHandler mybasehandler,int msgwhat,ArrayList<String> list_form_param, ArrayList<String> list_real_param,String methodname){
        this.context=context;
        this.mybasehandler=mybasehandler;
        this.msgwhat=msgwhat;
        this.list_form_param=list_form_param;
        this.list_real_param=list_real_param;
        this.methodname=methodname;
    }

    @Override
    public void run() {
        super.run();
        try {
            InputStream is= HttpConnSoap.GetInputstream(methodname,list_form_param, list_real_param);
            if(is!=null){
                ArrayList<String> urllist=null;
                urllist=Xmlparse(is);
                Message msg=Message.obtain();
                msg.what=msgwhat;
                Bundle data=new Bundle();
                data.putStringArrayList("data", urllist);
                System.out.println("run方法中的情况"+urllist);
                msg.setData(data);
                mybasehandler.sendMessage(msg);
            }
            else{
                Message msg=Message.obtain();
                mybasehandler.sendEmptyMessage(INTERNET_ERROR);
            }
        } catch (XmlPullParserException e) {
            Toast.makeText(context, "网络错误", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context,"网络错误",Toast.LENGTH_LONG).show();
        }

    }

    // 解析远程xml
    public ArrayList<String> Xmlparse(InputStream inputstream)
            throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        ArrayList<String> listdata=new ArrayList<String>();
        parser.setInput(inputstream, "utf-8");
        int parser_type = parser.getEventType();

        while (parser_type != XmlPullParser.END_DOCUMENT) {
            // xml文件没有结束
            switch (parser_type) {
                case XmlPullParser.START_TAG:
                    String name = parser.getName();
                    if (name.equalsIgnoreCase("string")) {
                        parser_type = parser.next();
                        listdata.add(parser.getText());
                        System.out.println("读取情况"+parser.getText());
                    }
                    break;
            }

            // System.out.println("count的值"+count);

            parser_type = parser.next();

        }
        System.out.println("Xmlparam中的情况"+listdata);
        return listdata;
    }
}
