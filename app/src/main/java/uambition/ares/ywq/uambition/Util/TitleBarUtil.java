package uambition.ares.ywq.uambition.Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.PrivateKey;

import uambition.ares.ywq.uambition.R;

/**
 * Created by ares on 15/7/24.
 */
public class TitleBarUtil {

public interface TitleBarCallBack{


    public void leftBtnClickLister();
    public void rightBtnClickLister();
}

    public static int getThemeColor(Activity context){
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

    public static  void initTitleBar(Activity context,String titleString,final TitleBarCallBack callBack){

        View view = ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.title_bar);
        layout.setBackgroundColor(getThemeColor(context));
        ImageButton leftBtn =(ImageButton)view.findViewById(R.id.titlebar_left_button);
        ImageButton rightBtn =(ImageButton)view.findViewById(R.id.titlebar_right_button);
        TextView title = (TextView)view.findViewById(R.id.titlebar_title);
        title.setText(titleString);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.leftBtnClickLister();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.rightBtnClickLister();
            }
        });

    }

    public static  void initTitleBarWithRightBtn(Activity context,boolean visible,String titleString,final TitleBarCallBack callBack){

        View view = ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
        RelativeLayout layout = (RelativeLayout)view.findViewById(R.id.title_bar);
        layout.setBackgroundColor(getThemeColor(context));
        ImageButton leftBtn =(ImageButton)view.findViewById(R.id.titlebar_left_button);
        ImageButton rightBtn =(ImageButton)view.findViewById(R.id.titlebar_right_button);
        if(visible){
            rightBtn.setVisibility(View.VISIBLE);
        }
        rightBtn.setBackground(context.getResources().getDrawable(R.drawable.confrim));
        TextView title = (TextView)view.findViewById(R.id.titlebar_title);
        title.setText(titleString);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.leftBtnClickLister();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.rightBtnClickLister();
            }
        });

    }

    public static void setBarColor(Activity activity,int color){
        //View view = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        RelativeLayout layout = (RelativeLayout)activity.findViewById(R.id.title_bar);

        layout.setBackgroundColor(color);

    }


    public  static TextView getTitleView(Activity activity){

        View view = ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        TextView title = (TextView)view.findViewById(R.id.titlebar_title);
//        title.setText(titleString);
        return title;

    }

}
