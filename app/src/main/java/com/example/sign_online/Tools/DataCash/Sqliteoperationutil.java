package com.example.sign_online.Tools.DataCash;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**对协议模板和协议进行操作
 * Created by 曾志强 on 2016/1/23.
 */
public class Sqliteoperationutil {
    //sqlopenholder对象
    SqliteDb sqliteDb =new SqliteDb(null,"signonline.db",null,0);

    /*向协议模板表中插入数据
    *insertintomould
    * @param name
    * @param content
     */
    public void Insertintomould(String name,String content,String mouldlabelpath){
        //数据库对象
        SQLiteDatabase sqLiteDatabase= sqliteDb.getWritableDatabase();
        String sqloperation="insert into mould values  + ('" + name + "','" + content + "','" + mouldlabelpath + "')" ;
        sqLiteDatabase.execSQL(sqloperation);
    }
    /*根据协议名删除协议模板表数据
 *deleteomould
 * @param name
  */
    public void Deleteomould(String name){
        //数据库对象
        SQLiteDatabase sqLiteDatabase= sqliteDb.getWritableDatabase();
        String sqloperation="delete mould where mouldname =" +"'" + name + "'"  ;
        sqLiteDatabase.execSQL(sqloperation);
    }


    /*查询所有协议模板
 *Selectallomouldcontent
  *return list
  */
    public List<String> Selectallmouldproperty(){
        List<String> listcontent=new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase= sqliteDb.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from mould",null);
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                //将cursor往下面移动
                listcontent.add(cursor.getString(cursor.getColumnIndex("mouldname")));
                listcontent.add(cursor.getString(cursor.getColumnIndex("mouldcontent")));
                listcontent.add(cursor.getString(cursor.getColumnIndex("mouldlabelpath")));
            }
        }
        return listcontent;
    }

    /*插入用户协议
    *Insertuserprotocel
    * @param mouldname
    * @param protocelpath
    * @paam protoceldate
     */
    public void Insertuserprotocel(String mouldname,String protocelpath,String protoceldate){
        String sqloperation="insert into protocel values  + ('" + mouldname + "','" + protocelpath + "' , '" + protoceldate + "')" ;
        SQLiteDatabase sqLiteDatabase= sqliteDb.getWritableDatabase();
        sqLiteDatabase.execSQL(sqloperation);
    }

    /*根据协议日期删除用户协议
  *Deleteuserprotocel
  * @paam protoceldate
   */
    public void Deleteuserprotocel(String protoceldate){
        String sqloperation="delete from protocel where protocel_date =" + "'" + protoceldate + "'";
        SQLiteDatabase sqLiteDatabase= sqliteDb.getWritableDatabase();
        sqLiteDatabase.execSQL(sqloperation);
    }


    /*根据协议模板名查询用户协议
  *Selectuserprotocel
  * @paam mouldname
   */
    public List<String> Selectuserprotocel(String mouldname){
        List<String> listprotocel=new ArrayList<String>();
        SQLiteDatabase sqLiteDatabase= sqliteDb.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select protocel_path,protocel_date from protocel where mouldname=?",new String[]{mouldname});
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                listprotocel.add(cursor.getString(cursor.getColumnIndex("protocel_path")));
                listprotocel.add(cursor.getString(cursor.getColumnIndex("protocel_date")));
            }
        }
        return listprotocel;
    }

}
