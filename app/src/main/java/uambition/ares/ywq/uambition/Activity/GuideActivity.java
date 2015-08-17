package uambition.ares.ywq.uambition.Activity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import uambition.ares.ywq.uambition.R;

/**
 * Created by ares on 15/8/12.
 */
public class GuideActivity extends AppIntro {
    @Override
    public void init(Bundle bundle) {
        addSlide(AppIntroFragment.newInstance("创建你的目标1","fight!!!", R.drawable.color_setting, Color.YELLOW));
        addSlide(AppIntroFragment.newInstance("创建你的目标2","fight!!!", R.drawable.color_setting, Color.BLACK));
        addSlide(AppIntroFragment.newInstance("创建你的目标3","fight!!!", R.drawable.color_setting, Color.BLUE));
        addSlide(AppIntroFragment.newInstance("创建你的目标4","fight!!!", R.drawable.color_setting, Color.RED));

        // OPTIONAL METHODS
        // Override bar/separator color
//        setBarColor(getResources().getColor(R.color.theme_color));
//        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {

    }

    @Override
    public void onDonePressed() {

    }
}
