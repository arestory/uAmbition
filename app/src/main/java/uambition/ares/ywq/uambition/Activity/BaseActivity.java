package uambition.ares.ywq.uambition.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ThemeColorUtil;
import uambition.ares.ywq.uambition.Util.Util;

/**
 * Created by ares on 15/7/18.
 */
public  abstract  class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    public void init(){
        setContentView();
        findViews();
        getData();
        showContent();
        makeBar();
    }

    //沉浸式状态栏
    public  void makeBar(){


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//		                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//		                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //   window.setStatusBarColor(getResources().getColor(R.color.theme_color));

            int color = getThemeColor();
            window.setStatusBarColor(color);

            window.setNavigationBarColor(color);
        }else{
            Util.setBar(this);
        }

    }

    public int getThemeColor(){
        String colorStr = ThemeColorUtil.getThemeColor(BaseActivity.this);

        int color=0;
        if(colorStr!=null){
            if(!colorStr.equals("")){
                try{
                    color= Integer.parseInt(colorStr);

                }catch (Exception e ){
                    color=getResources().getColor(R.color.theme_color);
                }
            }else{
                color=getResources().getColor(R.color.theme_color);

            }

        }else{
            color=getResources().getColor(R.color.theme_color);

        }

        return  color;




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
        int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0
                    || index >= fm.getFragments().size()) {
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
                } else {
              //  handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }

    }
    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    public abstract void setContentView();
    public abstract void findViews();
    public abstract void getData();
    public abstract void showContent();
}
