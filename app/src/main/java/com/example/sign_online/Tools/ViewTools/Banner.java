package com.example.sign_online.Tools.ViewTools;

/**这个工具类主要用来控制广告条轮放的效果
 * Created by 曾志强 on 2016/1/22.
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sign_online.Baseclass.AdDomain;
import com.example.sign_online.Baseclass.MyBaseHandler;
import com.example.sign_online.R;
import com.example.sign_online.Thread.SSelectBannerPictureUrlThread;
import com.example.sign_online.Tools.DataCash.Fileoperation;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
public class Banner {

    public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 鍥剧墖缂撳瓨璺緞

    private ViewPager adViewPager;
    private List<ImageView> imageViews;// 婊戝姩鐨勫浘鐗囬泦鍚�

    private List<View> dots; // 鍥剧墖鏍囬姝ｆ枃鐨勯偅浜涚偣
    private List<View> dotList;

    private TextView tv_date;
    private TextView tv_title;
    private TextView tv_topic_from;
    private TextView tv_topic;
    private int currentItem = 0; // 褰撳墠鍥剧墖鐨勭储寮曞彿
    // 瀹氫箟鐨勪簲涓寚绀虹偣
    private View dot0;
    private View dot1;
    private View dot2;
    private View dot3;
    private View dot4;
    //查询成功
    private final static int SELECT_BANNERPICTUREURL_SUCCESS=1;
    //对应四张图片的地址
    private static ArrayList<String> listUrl;
    //对应的布局
    private View view=null;
    //Context
    private Context context;
    private ScheduledExecutorService scheduledExecutorService;

    // 寮傛鍔犺浇鍥剧墖
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;

    // 杞挱banner鐨勬暟鎹�
    private List<AdDomain> adList;
    //用户邮箱
    private String userEmail;
    private MyBaseHandler handler = new MyBaseHandler(context) {
        public void handleMessage(android.os.Message msg) {
            if(msg.what==2) {
                adViewPager.setCurrentItem(currentItem);
            }
            else if(msg.what==1){
                listUrl=new ArrayList<String>();
                listUrl=msg.getData().getStringArrayList("data");
                String bannerUrlData=listUrl.get(0)+" "+listUrl.get(1)+" "+listUrl.get(2)+" "+listUrl.get(3);
                (new Fileoperation(context,null)).WriteContentToFile(userEmail, "bannerurl.txt", bannerUrlData);
                initAdData();
                startAd();
            }
            else if(msg.what==0){
                listUrl=new ArrayList<String>();
                String bannerUrl=(new Fileoperation(context,null)).ReadFileContent(userEmail,"bannerurl.txt");
                if(bannerUrl!=null) {
                    //直接从本地加载数据
                    String[] urlArray = bannerUrl.split(" ");
                    listUrl.add(urlArray[0]);
                    listUrl.add(urlArray[1]);
                    listUrl.add(urlArray[2]);
                    listUrl.add(urlArray[3]);
                    initAdData();
                    startAd();
                }
                Toast.makeText(context,"网络或者服务错误",Toast.LENGTH_SHORT).show();
            }
        };
    };

    public Banner(Context context,View view){
        this.context=context;
        this.view=view;
        userEmail=context.getSharedPreferences("use_for_login", context.MODE_PRIVATE).getString("username1",null);
    }

    /*start()
     * 开始执行广告轮转
     */
    public void start(){
        // 浣跨敤ImageLoader涔嬪墠鍒濆鍖�
        initImageLoader();
        // 鑾峰彇鍥剧墖鍔犺浇瀹炰緥
        mImageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.top_banner_android)
                .showImageForEmptyUri(R.mipmap.top_banner_android)
                .showImageOnFail(R.mipmap.top_banner_android)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();
        listUrl=new ArrayList<String>();
        String bannerUrl=(new Fileoperation(context,null)).ReadFileContent(userEmail,"bannerurl.txt");
        if(bannerUrl!=null) {
            //直接从本地加载数据
            String[] urlArray = bannerUrl.split(" ");
            listUrl.add(urlArray[0]);
            listUrl.add(urlArray[1]);
            listUrl.add(urlArray[2]);
            listUrl.add(urlArray[3]);
            initAdData();
            startAd();
        }
        else {
            ((new SSelectBannerPictureUrlThread(context, handler, SELECT_BANNERPICTUREURL_SUCCESS, null))).start();
        }
    }

    private void initImageLoader() {
        File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
                .getOwnCacheDirectory(context.getApplicationContext(),
                        IMAGE_CACHE_PATH);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(12 * 1024 * 1024))
                .memoryCacheSize(12 * 1024 * 1024)
                .discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);
    }

    private void initAdData() {
        // 骞垮憡鏁版嵁
        adList = getBannerAd();
        imageViews = new ArrayList<ImageView>();
        // 鐐�
        dots = new ArrayList<View>();
        dotList = new ArrayList<View>();
        dot0 = view.findViewById(R.id.v_dot0);
        dot1 = view.findViewById(R.id.v_dot1);
        dot2 = view.findViewById(R.id.v_dot2);
        dot3 = view.findViewById(R.id.v_dot3);
        dot4 = view.findViewById(R.id.v_dot4);
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        dots.add(dot3);
        dots.add(dot4);


        /*
        *现在不启用广告文字功能，直接由服务端发送广告图片
         */
//        tv_date = (TextView) view.findViewById(R.id.tv_date);
//        tv_title = (TextView) view.findViewById(R.id.tv_title);
//        tv_topic_from = (TextView) view.findViewById(R.id.tv_topic_from);
//        tv_topic = (TextView) view.findViewById(R.id.tv_topic);

        adViewPager = (ViewPager) view.findViewById(R.id.vp);
        addDynamicView();
        adViewPager.setAdapter(new MyAdapter());// 璁剧疆濉厖ViewPager椤甸潰鐨勯�閰嶅櫒
        // 璁剧疆涓�釜鐩戝惉鍣紝褰揤iewPager涓殑椤甸潰鏀瑰彉鏃惰皟鐢�
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private void addDynamicView() {
        // 鍔ㄦ�娣诲姞鍥剧墖鍜屼笅闈㈡寚绀虹殑鍦嗙偣
        // 鍒濆鍖栧浘鐗囪祫婧�
        for (int i = 0; i < adList.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ScaleType.CENTER_INSIDE);
            // 寮傛鍔犺浇鍥剧墖
            mImageLoader.displayImage(adList.get(i).getImgUrl(), imageView,
                    options);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageViews.add(imageView);
            dots.get(i).setVisibility(View.VISIBLE);
            dotList.add(dots.get(i));
        }
    }


    private void startAd() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 褰揂ctivity鏄剧ず鍑烘潵鍚庯紝姣忎袱绉掑垏鎹竴娆″浘鐗囨樉绀�
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
                TimeUnit.SECONDS);
    }

    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (adViewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                Message message=handler.obtainMessage();
                message.what=2;
                message.sendToTarget();
            }
        }
    }

    /*
     * 停止广告轮转
     */
    public void Stop() {
        // 褰揂ctivity涓嶅彲瑙佺殑鏃跺�鍋滄鍒囨崲
        if(scheduledExecutorService!=null) {
            scheduledExecutorService.shutdown();
        }
    }

    private class MyPageChangeListener implements OnPageChangeListener {

        private int oldPosition = 0;

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            AdDomain adDomain = adList.get(position);
//            tv_title.setText(adDomain.getTitle()); // 璁剧疆鏍囬
//            tv_date.setText(adDomain.getDate());
//            tv_topic_from.setText(adDomain.getTopicFrom());
//            tv_topic.setText(adDomain.getTopic());
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return adList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imageViews.get(position);
            ((ViewPager) container).addView(iv);
            final AdDomain adDomain = adList.get(position);
            // 鍦ㄨ繖涓柟娉曢噷闈㈣缃浘鐗囩殑鐐瑰嚮浜嬩欢
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 澶勭悊璺宠浆閫昏緫
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }


    /**
     * 杞挱骞挎挱妯℃嫙鏁版嵁
     *
     * @return
     */
    public static List<AdDomain> getBannerAd() {
        List<AdDomain> adList = new ArrayList<AdDomain>();
        AdDomain adDomain = new AdDomain();
//        adDomain.setId("1");
//        adDomain.setDate("1");
//        adDomain.setTitle("1");
//        adDomain.setTopicFrom("1");
//        adDomain.setTopic("1");
        adDomain.setImgUrl(listUrl.get(0));
        adList.add(adDomain);

        AdDomain adDomain2 = new AdDomain();
//        adDomain2.setId("");
//        adDomain2.setDate("");
//        adDomain2.setTitle("");
//        adDomain2.setTopicFrom("");
//        adDomain2.setTopic("");
        adDomain2.setImgUrl(listUrl.get(1));

        adList.add(adDomain2);

        AdDomain adDomain3 = new AdDomain();
//        adDomain3.setId("");
//        adDomain3.setDate("");
//        adDomain3.setTitle("");
//        adDomain3.setTopicFrom("");
        adDomain3.setImgUrl(listUrl.get(2));
//        adDomain3.setTopic("");
        adList.add(adDomain3);
        AdDomain adDomain4 = new AdDomain();
//        adDomain4.setId("");
//        adDomain4.setDate("");
//        adDomain4.setTitle("");
        adDomain4.setImgUrl(listUrl.get(3));
//        adDomain4.setTopicFrom("");
//        adDomain4.setTopic("");
        adList.add(adDomain4);

        return adList;
    }

}

