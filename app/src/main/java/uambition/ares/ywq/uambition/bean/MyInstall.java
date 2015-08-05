package uambition.ares.ywq.uambition.bean;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by ares on 15/8/4.
 */
public class MyInstall extends BmobInstallation {


    public MyInstall(Context context) {
        super(context);
    }



    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    private String receiverId;

    private User receiver;

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
