package com.example.sign_online.Tools.ViewTools;

import java.lang.ref.SoftReference;
import java.util.Hashtable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class ImageUtil {

	private static final String TAG = "ImageUtil";
	/** 缂撳瓨闆嗗悎 */
	private static Hashtable<Integer, SoftReference<Bitmap>> mImageCache //
	= new Hashtable<Integer, SoftReference<Bitmap>>();

	/**
	 * 鏍规嵁id杩斿洖涓�釜澶勭悊鍚庣殑鍥剧墖
	 * 
	 * @param res
	 * @param resID
	 * @return
	 */
	public static Bitmap getImageBitmap(Resources res, int resID) {
		// 鍏堝幓闆嗗悎涓彇褰撳墠resID鏄惁宸茬粡鎷胯繃鍥剧墖锛屽鏋滈泦鍚堜腑鏈夛紝璇存槑宸茬粡鎷胯繃锛岀洿鎺ヤ娇鐢ㄩ泦鍚堜腑鐨勫浘鐗囪繑鍥�
		SoftReference<Bitmap> reference = mImageCache.get(resID);
		if (reference != null) {
			Bitmap bitmap = reference.get();
			if (bitmap != null) {// 浠庡唴瀛樹腑鍙�
				Log.i(TAG, "浠庡唴瀛樹腑鍙�");
				return bitmap;
			}
		}
		// 濡傛灉闆嗗悎涓病鏈夛紝灏辫皟鐢╣etInvertImage寰楀埌涓�釜鍥剧墖锛岄渶瑕佸悜闆嗗悎涓繚鐣欎竴寮狅紝鏈�悗杩斿洖褰撳墠鍥剧墖
		Log.i(TAG, "閲嶆柊鍔犺浇");
		Bitmap invertBitmap = getInvertBitmap(res, resID);
		// 鍦ㄩ泦鍚堜腑淇濆瓨涓�唤锛屼究浜庝笅娆¤幏鍙栨椂鐩存帴鍦ㄩ泦鍚堜腑鑾峰彇
		mImageCache.put(resID, new SoftReference<Bitmap>(invertBitmap));
		return invertBitmap;
	}

	/**
	 * 鏍规嵁鍥剧墖鐨刬d锛岃幏鍙栧埌澶勭悊涔嬪悗鐨勫浘鐗�
	 * 
	 * @param resID
	 * @return
	 */
	public static Bitmap getInvertBitmap(Resources res, int resID) {
		// 1.鑾峰彇鍘熷浘
		Bitmap sourceBitmap = BitmapFactory.decodeResource(res, resID);

		// 2.鐢熸垚鍊掑奖鍥剧墖
		Matrix m = new Matrix(); // 鍥剧墖鐭╅樀
		m.setScale(1.0f, -1.0f); // 璁╁浘鐗囨寜鐓х煩闃佃繘琛屽弽杞�
		Bitmap invertBitmap = Bitmap.createBitmap(sourceBitmap, 0,
				sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(),
				sourceBitmap.getHeight() / 2, m, false);

		// 3.涓ゅ紶鍥剧墖鍚堟垚涓�紶鍥剧墖
		Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
				(int) (sourceBitmap.getHeight() * 1.5 + 5), Config.ARGB_8888);
		Canvas canvas = new Canvas(resultBitmap); // 涓哄悎鎴愬浘鐗囨寚瀹氫竴涓敾鏉�
		canvas.drawBitmap(sourceBitmap, 0f, 0f, null); // 灏嗗師鍥剧墖鐢诲湪鐢诲竷鐨勪笂鏂�
		canvas.drawBitmap(invertBitmap, 0f, sourceBitmap.getHeight() + 5, null); // 灏嗗�褰卞浘鐗囩敾鍦ㄧ敾甯冪殑涓嬫柟

		// 4.娣诲姞閬僵鏁堟灉
		Paint paint = new Paint();
		// 璁剧疆閬僵鐨勯鑹诧紝杩欓噷浣跨敤鐨勬槸绾挎�姊害
		LinearGradient shader = new LinearGradient(0,
				sourceBitmap.getHeight() + 5, 0, resultBitmap.getHeight(),
				0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// 璁剧疆妯″紡涓猴細閬僵锛屽彇浜ら泦
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, sourceBitmap.getHeight() + 5,
				sourceBitmap.getWidth(), resultBitmap.getHeight(), paint);

		return resultBitmap;
	}
}
