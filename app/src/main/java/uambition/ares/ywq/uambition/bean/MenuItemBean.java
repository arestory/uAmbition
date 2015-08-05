package uambition.ares.ywq.uambition.bean;

import android.support.v4.app.Fragment;

/**
 * Created by ares on 15/7/23.
 */
public class MenuItemBean {

    private String title;
    private Fragment fragment;
    private int imgResId;

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
