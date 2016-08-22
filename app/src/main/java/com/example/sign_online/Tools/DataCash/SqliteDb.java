package com.example.sign_online.Tools.DataCash;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**主要是用于创建sqlite数据库，一个为协议模版数据库，一个为用户协议数据库
 * 协议模板数据库包括模板名 模板体，模板label
 * 用户协议数据库包括用户协议所属模板，协议产品(图片路径)，日期
 * Created by 曾志强 on 2016/1/23.
 */
public class SqliteDb extends SQLiteOpenHelper{/*
    //上下文
    private Context context=null;*/
    //模板数据库名
    private  final static String DB_NAME="signonline.db";
    //模板表名
    private  final static String MOULD_TABLE_NAME="mould";
    //用户协议表名
    private  final static String USER_PROTOCEL_TABLE_NAME="protocel";
    //模板数据库各属性名
    //模板名
    private final static String MOULD_NAME="mouldname";
    //模板体
    private final static String MOULD_CONTENT="mouldcontent";
    //模板label
    private final static String MOULD_LABEL_PATH="mouldlabel";
    //用户协议属性
    //协议产品（存图片的路径）
    private final static String USER_PROTOCEL_PATH="protocel_path";
    //协议日期
    private final static String USER_PROTOCEL_DATE="protocel_date";
    //创建模板表语句
    private String CREATE_MOULD_TABLE=" create table " + MOULD_TABLE_NAME
            + " (" + MOULD_NAME + " text not null" + " ,"
            + MOULD_CONTENT +" text not null" + " ,"
            +MOULD_LABEL_PATH + " text not null )";
    //创建用户协议表语句
    private String CREATE_USER_PROTOCEL_TABLE=" create table " + USER_PROTOCEL_TABLE_NAME
            + " (" + MOULD_NAME + " text not null" + " ,"
            + " (" + USER_PROTOCEL_DATE + " text not null" + " ,"
            + USER_PROTOCEL_PATH +" text not null)" ;

    //构造器
    public SqliteDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(null,DB_NAME, null, 0);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_MOULD_TABLE);
        db.execSQL(CREATE_USER_PROTOCEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
