package uambition.ares.ywq.uambition.App;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.CrashHandler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import uambition.ares.ywq.uambition.bean.Ambition;

/**
 * Created by ares on 15/7/30.
 */
public class AmbitionAPP extends Application {

private static uambition.ares.ywq.uambition.Util.ImageLoader imageLoader;
private static  AmbitionAPP ambitionAPP;
    private  static Context applicationContext;



    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        ambitionAPP = this;

        applicationContext=this;

        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.drawable.avatar)
                .showImageOnFail(R.drawable.avatar)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);



    }

    public static AmbitionAPP getInstance() {
        if (ambitionAPP == null) {
            ambitionAPP = new AmbitionAPP();
        }
        return ambitionAPP;
    }
    public uambition.ares.ywq.uambition.Util.ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new  uambition.ares.ywq.uambition.Util.ImageLoader(getApplicationContext(), R.drawable.avatar);
        }
        return imageLoader;
    }

    public void setImageLoader(uambition.ares.ywq.uambition.Util.ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public static void setInstance(AmbitionAPP instance) {
        AmbitionAPP.ambitionAPP = instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
