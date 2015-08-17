package uambition.ares.ywq.uambition.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import uambition.ares.ywq.uambition.Activity.MainActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ThemeColorUtil;
import uambition.ares.ywq.uambition.Util.Util;
import uambition.ares.ywq.uambition.view.holocolorpicker.ColorPicker;
import uambition.ares.ywq.uambition.view.holocolorpicker.OpacityBar;
import uambition.ares.ywq.uambition.view.holocolorpicker.SVBar;
import uambition.ares.ywq.uambition.view.holocolorpicker.SaturationBar;
import uambition.ares.ywq.uambition.view.holocolorpicker.ValueBar;
import uambition.ares.ywq.uambition.view.jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by ares on 15/7/22.
 */
public class SettingFragment extends Fragment {


    private MainActivity activity;
    private ColorPicker picker;
    private  SVBar svBar;
    private OpacityBar opacityBar;
    private SaturationBar saturationBar;
    private ValueBar valueBar;
    private  RelativeLayout layout;
    private RadioButton orangeBtn,colorBtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_color_setting, container, false);

        activity=(MainActivity)getActivity();
        initUI(view);
        return view;
    }


    public void initUI( View view){

        layout=(RelativeLayout)view.findViewById(R.id.title_bar);
          picker = (ColorPicker)view. findViewById(R.id.color_picker);
         svBar = (SVBar)view.  findViewById(R.id.svbar);
          opacityBar = (OpacityBar) view. findViewById(R.id.opacitybar);
          saturationBar = (SaturationBar)view.  findViewById(R.id.saturationBar);
          valueBar = (ValueBar)view.  findViewById(R.id.valuebar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        orangeBtn=(RadioButton)view.findViewById(R.id.theme_color_btn);
        colorBtn=(RadioButton)view.findViewById(R.id.diy_color_btn);

        orangeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                } else {

                }

            }
        });
        colorBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });
//To get the color
        picker.getColor();
        picker.setColor(activity.getThemeColor());
//To set the old selected color u can do it like this
        picker.setOldCenterColor(activity.getThemeColor());
        //picker.setOldCenterColor(picker.getColor());
// adds listener to the colorpicker which is implemented
//in the activity
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                Util.setBarColor(getActivity(),color);
//                Log.i("YWQ", color + "颜色");
//                Log.i("YWQ", activity.getResources().getColor(R.color.theme_color) + "");

                ThemeColorUtil.saveColor(activity, "ambition", "theme_color", color + "");
                activity.getTitleBar().setBackgroundColor(color);
                activity.getMenuTitleView().setBackgroundColor(color);
                activity.getFloatingActionButton().setRippleColor(color);
                //动态更改菜单的点击项背景颜色
                activity.getListAdapter().setSelectItem(activity.getCurrentPosition());
                activity.getListAdapter().notifyDataSetInvalidated();

                if(activity.getMoreFragment()!=null){
                    View rootView =  activity.getMoreFragment().getRootView();

                    if(rootView!=null){
                        rootView.setBackgroundColor(color);
                    }

                }
                if(activity.getBbsFragment()!=null){
                    WaveSwipeRefreshLayout layout = activity.getBbsFragment().getWaveRefreshLayout();
                    if(layout!=null){
                        layout.setWaveColor(color);
                    }

                }


                // TitleBarUtil.setBarColor(getActivity(),color);
            }
        });

//to turn of showing the old color
        picker.setShowOldCenterColor(false);

//adding onChangeListeners to bars

        opacityBar.setOnOpacityChangedListener(new OpacityBar.OnOpacityChangedListener() {
            @Override
            public void onOpacityChanged(int opacity) {

            }
        });
        valueBar.setOnValueChangedListener(new ValueBar.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {

            }
        });
        saturationBar.setOnSaturationChangedListener(new SaturationBar.OnSaturationChangedListener() {
            @Override
            public void onSaturationChanged(int saturation) {

            }
        });

    }

}
