package uambition.ares.ywq.uambition.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by ares on 15/8/14.
 */
//用于记录奔溃情况
public class CrashLog  extends BmobObject{

    private String userName;
    private String userPhone;
    private String crashDetail;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCrashDetail() {
        return crashDetail;
    }

    public void setCrashDetail(String crashDetail) {
        this.crashDetail = crashDetail;
    }
}
