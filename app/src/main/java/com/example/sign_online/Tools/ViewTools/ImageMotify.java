package com.example.sign_online.Tools.ViewTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

/**对图片的操作
 * Created by 曾志强 on 2016/4/7.
 */
public class ImageMotify {
    //需要改变的图片
    private Bitmap bitmap=null;
    private Context context=null;
    private int screenWidth=0;
    private int screenHeight=0;
    public ImageMotify(Context context, Bitmap bitmap, int screenWidth, int screenHeight){
        this.context=context;
        this.screenWidth=screenWidth;
        this.screenHeight=screenHeight;
        this.bitmap=bitmap;
    }

    /*改变mycourse图片的大小，使其显示完全
    *@motifyImageSize
    * @float scaleWidth
    * @float scaleHeight
     *@return Bitmap result
     */
    public Bitmap motifyImageSize(float scaleWidth,float scaleHeight){
        Bitmap result=null;
        Matrix matrix=new Matrix();
        scaleWidth=(screenWidth*scaleWidth)/(bitmap.getWidth());
        scaleHeight=(screenHeight*scaleHeight)/(bitmap.getHeight());
        matrix.postScale(scaleWidth, scaleHeight);
        result=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return result;
    }

}
