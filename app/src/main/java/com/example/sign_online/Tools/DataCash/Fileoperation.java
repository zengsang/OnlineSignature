package com.example.sign_online.Tools.DataCash;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.sign_online.Baseclass.MyBaseHandler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**提供本项目所有对于文件的需求操作
 * Created by 曾志强 on 2016/1/24.
 */
public class Fileoperation {
    //上下文
    private Context context=null;
    //hander
    private MyBaseHandler myBaseHandler=null;
    //本地没有图片
    private static final int NO_PICTURE=0;
    private static final int EXIT_PICTURE=1;
    private static final int SELECT_USERICON_SUCCESS=2;
    //获取缓存sd卡根路径
    private String filedirectorypath = Environment.getExternalStorageDirectory().toString().trim()+"/"+"OnlineSignature";
    //协议模板文件夹
    private String protocelmould="protocelmould";
    //用户协议文件夹
    private String protocels="protocels";
    //用户头像文件夹
    private String usericon="usericon";
    //用户消息
    private String usertranslatemsg="usertranslatemsg";
    //其他文件
    private String userotherfile="userotherfile";
    //构造器
    public Fileoperation(Context context,MyBaseHandler myBaseHandler){
        this.context=context;
        this.myBaseHandler=myBaseHandler;
    }

    /*为了防止用户对自身协议的随意删除，将其存入data/data目录
        * Saveuserprotocel
        * @
         */
    public void Saveuserprotocel(String path){

    }


    /*获取手机中的所有图片路径
    *Getallpictures
     */
    public void Getallpictures(){
        //开启一个新的线程
        new Thread(){
            @Override
            public void run() {
                super.run();
                ArrayList<String> pictureurlpath=new ArrayList<String>();
                //系统图片路径
                Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver=context.getContentResolver();
                Cursor cursor=contentResolver.query(uri,null,null,null,null);
                if(cursor==null||cursor.getCount()<=0){
                    //手机中并没有图片
                    return;
                }
                while(cursor.moveToNext()){
                    //获取角码
                    int index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //获取对应文件的路径
                    String path=cursor.getString(index);
                    pictureurlpath.add(path);
                }
                Message msg=Message.obtain();
                msg.what=EXIT_PICTURE;
                Bundle bundle=new Bundle();
                bundle.putStringArrayList("pictureurlpath",pictureurlpath);
                msg.setData(bundle);
                //发送数据到主线程
                myBaseHandler.sendMessage(msg);
            };
        }.start();
    }


    /*初始化用户的本地文件，为用户保存文件做准备
    *InitFile
    * @param useremail 用户邮箱
     */
    public void InitFile(String useremail){
        File userfile=new File(filedirectorypath+"/"+useremail);
        if(userfile.exists()){
            //该用户存在对应的文件夹
            userfile.delete();
        }
        else{
            //不存在则新建相应目录
            userfile.mkdirs();
            //头像，模板，协议目录
            (new File(userfile,usericon)).mkdirs();
            (new File(userfile,protocelmould)).mkdirs();
            (new File(userfile,protocels)).mkdirs();
            (new File(userfile,usertranslatemsg)).mkdirs();
            (new File(userfile,userotherfile)).mkdirs();
        }
    }

    /*在本地插入用户的头像
    *SaveUserIcon
    *@param Bitmap bitmap 用户头像
    * @param String useremail 用户邮箱
     */
    public void SaveUserIcon(String useremail,Bitmap bitmap) {
        //判断文件目录是否形成
        if (!(new File(filedirectorypath + "/" + useremail)).exists()) {
            InitFile(useremail);
        }
        try {
            String filefatherpath = filedirectorypath + "/" + useremail + "/" + usericon + "/";
            File filefather = new File(filefatherpath);
            if (!filefather.exists()) {
                filefather.mkdirs();
            }
            String filepath = filefatherpath + useremail + ".jpg";
            //输出流
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*在本地缓插入用户的自定义协议模板，同时给协议目录增加相应模板
    *SaveUserProtocelMould
     * @param String useremail 用户邮箱
    *@param protocelmouldname 模板名
    *@param protocelmouldcontent 模板体
     */
    public void SaveUserProtocelMould(String useremail,String protocelmouldname,String protocelmouldcontent){
        //判断文件目录是否形成
        if(!(new File(filedirectorypath+"/"+useremail)).exists()){
            InitFile(useremail);
        }
        try {
            String filefatherpath=filedirectorypath+"/"+useremail+"/"+protocelmould;
            File filefather=new File(filefatherpath);
            if(!filefather.exists()){
                filefather.mkdirs();
            }
            String filepath=filefatherpath+"/"+protocelmouldname+".txt";
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            //将协议体写入到文件夹
            bufferedOutputStream.write(protocelmouldcontent.getBytes());
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*在本地缓插入用户的自定义协议，同时给协议目录增加相应
     *SaveUserProtocel
      * @param String useremail 用户邮箱
     *@param String protocelmouldname 模板名
     *@param String date 日期时间
     * @param Bitmap protocelimage 协议截图
      */
    public void SaveUserProtocel(String useremail,String protocelmouldname,String date,Bitmap protocelimage) {
        //判断文件目录是否形成
        if(!(new File(filedirectorypath+"/"+useremail)).exists()){
            InitFile(useremail);
        }
        try {
            String filefatherpath = filedirectorypath + "/" + useremail + "/" + protocels + "/"+ protocelmouldname;
            File filefather = new File(filefatherpath);
            if (!filefather.exists()) {
                filefather.mkdirs();
            }
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(new File(filefatherpath+"/"+date+".jpg")));
            protocelimage.compress(Bitmap.CompressFormat.JPEG,100,bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*这个方法用来加载所有用户的网络数据，以免每次用户都要从网络获取数据，提高效率，减少流量的使用
    *LoadUserData
    * @param String useremail
     */
    public void LoadUserData(String useremail){
        //先判断用户是否已经有数据在本地
        File directory=new File(Environment.getExternalStorageDirectory().toString());
        //列出sd卡所有文件
        File[] subfiles=directory.listFiles();
        //判断是否有当前用户文件
        for(File file:subfiles){
            if(useremail.equals(file.getName().toString())){
                //已经存在文件夹
                return;
            }
        }
        //不存在用户缓存数据，加载用户缓存
        //参数
        ArrayList<String> list_real_param=new ArrayList<String>();
        list_real_param.add(useremail);
        //   (new SSelectUserIconThread(context, myBaseHandler,SELECT_USERICON_SUCCESS,list_real_param)).start();
    }

    /*将用户的头像加载到本地
    *LoadUserIconToLocal
    * @param String useremail
    * @param Arraylist list 头像String流
     */
    public void LoadUserIconToLocal(String useremail,ArrayList<String> list){
        try {
            //检错
            if(list.size()==0||list==null){
                return;
            }
            String filepath=filedirectorypath+"/"+useremail+"/"+usericon;
            File file=IsExitedAndCreate(filepath);
            FileOutputStream fileOutputStream=new FileOutputStream(new File(file,"usericon.jpg"));
            byte[] bytes= Base64.decode(list.get(0),Base64.DEFAULT);
            //获取bitmap
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            //设置当前用户的头像
            Staticbean.userMsg.setUsericon(bitmap);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*将用户协议模板加载到本地
   *LoadUserProtocelMouldToLocal
    * @param String useremail
    * @param Arraylist list 头像String流
     */
    public void LoadUserProtocelMouldToLocal(String useremail,ArrayList<String> list){
        try {
            String filepath=filedirectorypath+"/"+useremail+"/"+protocelmould;
            //如果文件已经不存在则创建
            File file=IsExitedAndCreate(filepath);
            FileOutputStream fileOutputStream;
            String protocelname=null;
            String protocelcontent=null;
            for(int i=0;i<list.size();i++){
                protocelname=list.get(i);
                i++;
                protocelcontent=list.get(i);
                fileOutputStream=new FileOutputStream(new File(file,protocelname+".txt"));
                //写入协议模板
                fileOutputStream.write(protocelcontent.getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*将用户协议加载到本地
    *LoadUserProtocelToLocal
     * @param String useremail
     * @param Arraylist list 头像String流
      */
    public void LoadUserProtocelToLocal(String useremail,ArrayList<String> list){
        String filepath=filedirectorypath+"/"+useremail+"/"+protocels;
        File file=IsExitedAndCreate(filepath);
        //所有协议的目录
        File protocelsfile=null;
        FileOutputStream fileOutputStream=null;
        String protocelmouldname=null;
        String protocelimagestring=null;
        String protocelname=null;
        try {
            for (int i = 0; i < list.size(); i++) {
                if ("1".equals(list.get(i))) {
                    System.out.println("次数");
                    protocelmouldname = list.get(i + 1);
                    protocelsfile = new File(file, protocelmouldname);
                    //文件不存在
                    if (!protocelsfile.exists()) {
                        protocelsfile.mkdirs();
                    }
                    //文件存在
                    else if (protocelsfile.exists()) {
                        for (File file1 : protocelsfile.listFiles()) {
                            System.out.println("delete");
                            System.out.println("delete");
                            System.out.println("delete");
                            file1.delete();
                        }
                    }


                    //一种模板下的所有协议
                    for (int j = i + 2; j < list.size(); j = j + 2) {
                        if ("2".equals(list.get(j))) {
                            System.out.println("次数22");
                            break;
                        }


                        fileOutputStream = new FileOutputStream(new File(protocelsfile, list.get(j) + ".jpg"));
                        protocelimagestring = list.get(j + 1);
                        byte[] bytes = Base64.decode(protocelimagestring, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        System.out.println("次数3");
                        System.out.println("输出" + bitmap);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        i=j+1;
                    }



                }
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*获取用户的头像(直接从本地获取，没有考虑用户将本地文件删除，解决方法是重新登录)
    *GetUserIcon
    * @param useremail
    * @return bitmap
     */
    public Bitmap GetUserIcon(String useremail){
        String filepath=filedirectorypath+"/"+useremail+"/"+usericon+"/"+"usericon.jpg";
        Bitmap bitmap=null;
        bitmap=BitmapFactory.decodeFile(filepath);
        return bitmap;
    }


    /*获取用户所有协议模板名称
    *GetUserProtocelMouldName
    * @param useremail
     */
    public ArrayList<String> GetUserProtocelMouldName(String useremail) {
        ArrayList<String> list = new ArrayList<String>();
        String filepath = filedirectorypath + "/" + useremail + "/" + protocelmould;
        File[] files = (new File(filepath)).listFiles();
        String protocelmouldname = null;
        if (files != null && files.length != 0) {
            for (File file : files) {
                protocelmouldname = file.getName();
                list.add(protocelmouldname.substring(0, protocelmouldname.indexOf(".")));
            }
            if(myBaseHandler!=null) {
                //加载成功
                myBaseHandler.sendEmptyMessage(EXIT_PICTURE);
            }
            return list;
        }
        if(myBaseHandler!=null) {
            //加载失败
            myBaseHandler.sendEmptyMessage(NO_PICTURE);
        }
        return null;
    }

    /*获取用户的协议模板（依然从本地加载）
    *GetUserProtocelMould
    * @param useremail
    * return ArrayList<String>
     */
    public ArrayList<String> GetUserProtocelMould(String useremail){

        ArrayList<String> list=new ArrayList<String>();
        try {
            String filepath=filedirectorypath+"/"+useremail+"/"+protocelmould+"/";
            File file=new File(filepath);
            //模板名(即txt文件名)
            String mouldname=null;
            String readline=null;
            StringBuffer stringBuffer=null;
            //所有模板对应的txt文件
            File[] subfiles=file.listFiles();
            for(File subfile:subfiles){
                mouldname=subfile.getName();
                //获取模板名
                list.add(mouldname.substring(0,mouldname.indexOf(".")));
                //协议模板内容
                BufferedReader bufferedReader=new BufferedReader(new FileReader(subfile));
                while((readline=bufferedReader.readLine())!=null){
                    stringBuffer.append(readline);
                }
                list.add(stringBuffer.toString());
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*获取相应模板用户的协议（依然从本地加载）
    *GetUserProtocel
    * @param useremail
    * @param protocelmouldname
    * return ArrayList<String>
     */
    public ArrayList<String> GetUserProtocel(String useremail,String protocelmouldname){
        ArrayList<String> list=new ArrayList<String>();
        try {
            String filepath=filedirectorypath+"/"+useremail+"/"+protocels+"/"+protocelmouldname;
            File file=new File(filepath);
            if(!file.exists()){
                file.mkdirs();
                return null;
            }
            //模板名(即txt文件名)
            String protocelname=null;
            //协议图片
            Bitmap protocel=null;
            //将协议图片转换为string存储
            String protocelstring=null;
            //所有模板对应的txt文件
            File[] subfiles=file.listFiles();
            if(subfiles!=null&subfiles.length!=0){
                for(File subfile:subfiles){
                    protocelname=subfile.getName();
                    list.add(protocelname);
                    //协议图片文件
                    //协议内容
                    protocel=BitmapFactory.decodeFile(subfile.getCanonicalPath());
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    protocel.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] bytes=byteArrayOutputStream.toByteArray();
                    protocelstring=Base64.encodeToString(bytes,Base64.DEFAULT);
                    list.add(protocelstring);
                }
                return list;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*获取指定模板名的所有协议
       *GetAllProtocelInMould
       * @param protocelmouldname
       * @param useremail
        */
    public ArrayList<Object> GetAllProtocelInMould(String protocelmouldname,String useremail){
        String filepath=filedirectorypath+"/"+useremail+"/"+protocels+"/"+protocelmouldname;
        //协议图片
        Bitmap bitmap=null;
        ArrayList<Object> list=new ArrayList<Object>();
        File file=new File(filepath);
        if(!file.exists()){
            //文件被删
            file.mkdirs();
            return null;
        }
        File[] files=file.listFiles();
        if(files.length==0){
            //该模板下没有文件
            return null;
        }
        for(File file1:files){
            try {
                bitmap=BitmapFactory.decodeFile(file1.getCanonicalPath());
                list.add(file1.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /*删掉用户所有的本地数据
    *DeleteAllUserData
    * @param useremail
     */
    public void DeleteAllUserData(String useremail){
        String usericonfilepath=filedirectorypath+"/"+useremail;

    }

    /*判断一个文件是否存在，不存在则创建
    *IsExitedAndCreate
    * @param String filepath
    * @return file
     */
    public File IsExitedAndCreate(String filepath){
        File file=new File(filepath);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }

    /*删除一个目录下的所有文件
    *DeleteAllFile
    * @param String filepath 目录的路径
     */
    public void DeleteAllFile(String filepath){
        File file=new File(filepath);
        File[] files=file.listFiles();
        for(File subfile:files){
            subfile.delete();
        }
    }

    /*根据模板名获取模板体
    *GetMouldContent
    * @param String useremail
    * @param String protocelmouldname
     *@return String
      */
    public String GetMouldContent(String useremail,String protocelmouldname){
        String line=null;
        StringBuffer stringBuffer=new StringBuffer();
        try {
            String path=filedirectorypath+"/"+useremail+"/"+protocelmould+"/"+protocelmouldname+".txt";
            BufferedReader bufferedReader=new BufferedReader(new FileReader(new File(path)));
            while((line=bufferedReader.readLine())!=null){
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*将用户接收到的消息保存到本地
   *SaveTranslateMsgToLocal
   * @param String useremail
   * @param String filename
   * @param String translatemsgcontent
     */
    public void SaveTranslateMsgToLocal(String useremail,String filename,String usertranslatecontent){
        String filepath=filedirectorypath+"/"+useremail+"/"+usertranslatemsg;
        File file=new File(filepath);
        if(!file.exists()){
            file.mkdirs();
        }
        File file1=new File(file,filename);
        try {
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(file1));
            byte[] bytes=Base64.decode(usertranslatecontent,Base64.DEFAULT);
            bufferedOutputStream.write(bytes);
            bufferedOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*删除某一个协议
    *@name DeleteMyProtocel
    * @param userEmail  用户邮箱
    * @param protocelMouldName 协议对应的模板名称
    * @param protocelName 对应的协议名称
     */
    public void DeleteMyProtocel(String userEmail,String protocelMouldName,String protocelName) {
        String filePath = filedirectorypath + "/" + userEmail + "/" + protocels + "/" + protocelMouldName;
        File file = new File(filePath);
        File[] files = file.listFiles();
        if (!file.exists()) {
            myBaseHandler.sendEmptyMessage(0);
        }
        for (File file1 : files) {
            if (protocelName.equals(file1.getName())) {
                file1.delete();
                myBaseHandler.sendEmptyMessage(3);
            }
        }
    }

    /**将字符串写进指定名字的文件
     *
     * @param userEmail
     * @param fileName
     * @param content
     */
    public void WriteContentToFile(String userEmail,String fileName,String content){
        String filePath=filedirectorypath + "/" + userEmail + "/" +userotherfile;
        File file=new File(filePath);
        IsExitedAndCreate(filePath);
        try {
            File file1=new File(file,fileName);
            //输出流
            FileOutputStream fileOutputStream=new FileOutputStream(file1);
            byte[] bytes=content.getBytes();
            //将数据写入文件
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param userEmail
     * @param filename
     * @return
     */
    public String ReadFileContent(String userEmail,String filename){
        String filePath=filedirectorypath + "/" + userEmail + "/" +userotherfile+"/"+filename;
        File file=new File(filePath);
        if(!file.exists()){
            return null;
        }
        else{
            try {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuffer stringBuffer=new StringBuffer();
                String readLine=null;
                while((readLine=bufferedReader.readLine())!=null){
                    stringBuffer.append(readLine);
                }
                return stringBuffer.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**删除banner的缓存信息
     * @param userEmail
     */
    public void DeleteBannerUrlFile(String userEmail){
        File targetFile=new File(filedirectorypath+"/"+userEmail+"/"+userotherfile+"/"+"bannerurl.txt");
        if(!targetFile.exists()){
            return;
        }
        else {
            if(targetFile.isDirectory()){
                return;
            }
            targetFile.delete();
        }
    }
}
