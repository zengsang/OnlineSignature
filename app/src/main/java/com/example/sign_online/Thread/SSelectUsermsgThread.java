package com.example.sign_online.Thread;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Xml;
import android.widget.Toast;

import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.Baseclass.UserMsg;
import com.example.sign_online.Tools.NetAccess.HttpConnSoap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/** 用来查询用户信息的线程
 * Created by 曾志强 on 2016/1/18.
 */
public class SSelectUsermsgThread extends Thread{
    private Context context=null;
    private MyBaseHandler mybasehandler=null;
    private int msgwhat=0;
    private final static  int INTERNET_ERROR=0;
    //实参
    private ArrayList<String> list_real_param=new ArrayList<String>();

    public SSelectUsermsgThread(Context context, MyBaseHandler mybasehandler, int msgwhat, ArrayList<String> list_real_param){
        this.context=context;
        this.mybasehandler=mybasehandler;
        this.msgwhat=msgwhat;
        this.list_real_param=list_real_param;
    }
    //访问网络
    @Override
    public void run() {
        super.run();
        UserMsg userMsg =null;
        //形式参数
        ArrayList<String> list_form_param=new ArrayList<String>();
        list_form_param.add("useremail");
        try {
            InputStream is= HttpConnSoap.GetInputstream("Sselect", list_form_param, list_real_param);
            if(is!=null){
                userMsg =Xmlparse(is);
                Message msg=Message.obtain();
                msg.what=msgwhat;
                Bundle data=new Bundle();
                data.putSerializable("UserMsg", userMsg);
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
    public UserMsg Xmlparse(InputStream inputstream)
            throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        UserMsg userMsg = new UserMsg();
        int count = 0;
        parser.setInput(inputstream, "utf-8");
        int parser_type = parser.getEventType();

        while (parser_type != XmlPullParser.END_DOCUMENT) {
            // xml文件没有结束
            switch (parser_type) {
                case XmlPullParser.START_TAG:
                    String name = parser.getName();

                    if (name.equalsIgnoreCase("string")) {
                        switch (count) {
                            case 0:
                                parser_type=parser.next();
                                System.out.println("jksdfjlksadjfkj"+parser.getText());
                                userMsg.setUseremail(parser.getText());
                                count = 1;
                                break;
                            case 1:
                                parser_type = parser.next();
                                System.out.println("jksdfjlksadjfkj" + parser.getText());
                                userMsg.setUserpassword(parser.getText());
                                count = 2;
                                break;
                            case 2:
                                parser_type = parser.next();
                                userMsg.setUsername(parser.getText());
                                System.out.println("jksdfjlksadjfkj" + parser.getText());
                                count = 3;
                                break;
                            case 3:
                                parser_type = parser.next();
                                userMsg.setUserreallyname(parser.getText());
                                System.out.println("jksdfjlksadjfkj" + parser.getText());
                                count=4;
                                break;
                            case 4:
                                parser_type = parser.next();
                                userMsg.setUserphonenumber(parser.getText());
                                System.out.println("jksdfjlksadjfkj" + parser.getText());
                                count=5;
                                break;
                            case 5:
                                parser_type = parser.next();
                                userMsg.setUsersex(parser.getText());
                                System.out.println("jksdfjlksadjfkj" + parser.getText());
                                count=6;
                                break;
                            case 6:
                                parser_type = parser.next();
                                userMsg.setUseryearold(parser.getText());
                                System.out.println("jksdfjlksadjfkj" + parser.getText());
                                count=7;
                                break;

                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    // System.out.println(parser.getText());
                    // 添加一个对象
                    if (parser.getName().equalsIgnoreCase("string") && count ==7) {
                        // my = new My_problem();
                        count = 0;
                        return userMsg;
                    }
            }

            // System.out.println("count的值"+count);

            parser_type = parser.next();

        }
        return userMsg;
    }


}
