package uambition.ares.ywq.uambition.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

/**
 * Created by ares on 15/7/24.
 */
public class User extends BmobUser{

     public  User(){

         super();
     }


    public BmobInstallation getReceiver() {
        return receiver;
    }

    public void setReceiver(BmobInstallation receiver) {
        this.receiver = receiver;
    }

    private BmobInstallation receiver;
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    private String user_img_url;

    public String getUser_img_url() {
        return user_img_url;
    }
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUser_img_url(String user_img_url) {
        this.user_img_url = user_img_url;
    }




}
