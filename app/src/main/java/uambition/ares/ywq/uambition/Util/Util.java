package uambition.ares.ywq.uambition.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uambition.ares.ywq.uambition.Activity.BaseActivity;
import uambition.ares.ywq.uambition.Activity.MainActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.view.AmbitionDialog;
import uambition.ares.ywq.uambition.view.monindicator.MonIndicator;

/**
 * Created by ares on 15/7/18.
 */
public class Util {

private Context context;

    public  Util(Context context){

        this.context=context;
    }



    /**
     * 获取activity 所在的view
     *
     * @param context
     * @return
     */
    private static View getRootView(Activity context)
         {
               return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
           }

    /**
     *
     *
     * @param context 上下文
     */
    public static  void setBar(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true,context);
            SystemBarTintManager mTintManager = new SystemBarTintManager((Activity)context);

            mTintManager.setStatusBarTintEnabled(true);

            int color = getThemeColor(context);

            mTintManager.setTintColor(color);
            SystemBarTintManager.SystemBarConfig config = mTintManager.getConfig();
            getRootView((Activity)context).setPadding(0, config.getPixelInsetTop(false
            ), config.getPixelInsetRight(), config.getPixelInsetBottom());
//            getRootView((Activity)context).setPadding(0, config.getPixelInsetTop(false
//            ), config.getPixelInsetRight(), config.getPixelInsetBottom());
//	            mTintManager.setStatusBarAlpha(0);
            //mTintManager.setTintAlpha(100);

        }

    }
    public static int getThemeColor(Context context){
        String colorStr = ThemeColorUtil.getThemeColor(context);

        int color=0;
        if(colorStr!=null){
            if(!colorStr.equals("")){
                try{
                    color= Integer.parseInt(colorStr);

                }catch (Exception e ){
                    color=context.getResources().getColor(R.color.theme_color);
                }
            }else{
                color=context.getResources().getColor(R.color.theme_color);

            }

        }else{
            color=context.getResources().getColor(R.color.theme_color);

        }

        return  color;




    }

    /**
     *
     * @param context     上下文
     * @param colorResId 颜色资源ID
     */
    public static  void setBarWithColor(Context context,int colorResId){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, context);
            SystemBarTintManager mTintManager = new SystemBarTintManager((Activity)context);

            mTintManager.setStatusBarTintEnabled(true);
            mTintManager.setTintColor(colorResId);
            SystemBarTintManager.SystemBarConfig config = mTintManager.getConfig();

            getRootView((Activity)context).setPadding(0, config.getPixelInsetTop(false
            ), config.getPixelInsetRight(), config.getPixelInsetBottom());
//	            mTintManager.setStatusBarAlpha(0);
            //mTintManager.setTintAlpha(100);

        }

    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *
     * @param context
     * @param color
     */
    public  static  void setBarColor(Context context,int color){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity)context).getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//		                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//		                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
              window.setStatusBarColor(color);

            window.setNavigationBarColor(color);
        }else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true, context);
                SystemBarTintManager mTintManager = new SystemBarTintManager((Activity)context);

                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintColor(color);
                SystemBarTintManager.SystemBarConfig config = mTintManager.getConfig();
                getRootView((Activity)context).setPadding(0, config.getPixelInsetTop(false
                ), config.getPixelInsetRight(), config.getPixelInsetBottom());


            }


        }


    }


    protected static void setTranslucentStatus(boolean on,Context context) {

        Window win =((Activity)context).getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {

            winParams.flags |= bits;

        } else {

            winParams.flags &= ~bits;

        }

        win.setAttributes(winParams);

    }

    public static boolean isMobileNUM(String mobiles){

       Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);


        return m.matches();


    }


    /**
     * 检测网络是否可用
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static AmbitionDialog showDialog(Context context,String title) {
        if (context != null) {
            View view = ((Activity) context).getLayoutInflater().inflate(
                    R.layout.ambition_dialog, null);
            AmbitionDialog loadingDialog =new AmbitionDialog(context,R.style.CustomDialog);
            loadingDialog.setCancelable(false);
            loadingDialog.setContentView(view);
            BaseActivity activity = (BaseActivity)context;
            MonIndicator monIndicator =(MonIndicator)view.findViewById(R.id.monIndicator);
            TextView textView =(TextView)view.findViewById(R.id.msg);
            textView.setText(title);
            int color = activity.getThemeColor();

            monIndicator.setColors(new int[]{color, color, color, color, color});
            loadingDialog.show();
            return loadingDialog;
        }
        return null;
    }

//    public static AlertDialog showDialog(Context context,String title) {
//        if (context != null) {
//            View view = ((Activity) context).getLayoutInflater().inflate(
//                    R.layout.ambition_dialog, null);
//            AlertDialog loadingDialog = new AlertDialog.Builder(context)
//                    .setView(view).create();
//            loadingDialog.setCancelable(false);
//            BaseActivity activity = (BaseActivity)context;
//            MonIndicator monIndicator =(MonIndicator)view.findViewById(R.id.monIndicator);
//            TextView textView =(TextView)view.findViewById(R.id.msg);
//            textView.setText(title);
//            int color = activity.getThemeColor();
//
//            monIndicator.setColors(new int[]{color, color, color, color, color});
//            loadingDialog.show();
//            return loadingDialog;
//        }
//        return null;
//    }

    /**
     * 隐藏对话框
     *
     * @param dialog
     */
    public static void cancelDialog(AmbitionDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }


}
