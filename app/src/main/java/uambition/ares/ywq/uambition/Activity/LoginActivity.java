package uambition.ares.ywq.uambition.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.waps.AppConnect;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ThemeColorUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.Util;
import uambition.ares.ywq.uambition.bean.MyInstall;
import uambition.ares.ywq.uambition.bean.User;

/**
 * Created by ares on 15/7/18.
 */
public class LoginActivity  extends  BaseActivity{

    private EditText accountET;
    private EditText  pswET;
    private Button loginBtn;
    private Button registerBtn;
    private RelativeLayout layout;
    private RelativeLayout barlayout;

    private String userAccount;
    private String userPsw;

    private TextView registerTextView;
    private TextView forgetPSWTextView;
    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void setContentView() {
        AppConnect.getInstance("4fc70c42c2ddaef8bae10a92e54dcd17","lenovo",this);

        //用于WAPS 打包
//        AppConnect.getInstance(this);
           User user=BmobUser.getCurrentUser(this,User.class);
        if (user!=null){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);


    }

    @Override
    public void findViews() {
        //初始化Bmob SDK
        Bmob.initialize(this, getResources().getString(R.string.bmob_sdk_key));

        registerTextView=(TextView)findViewById(R.id.titlebar_right_button);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, PhoneActivity.class);
                startActivity(intent);
            }
        });
        forgetPSWTextView=(TextView)findViewById(R.id.forget_psw);
        forgetPSWTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, FirstPSWActivity.class);
                startActivity(intent);
            }
        });
        //-1086464
        layout=(RelativeLayout)findViewById(R.id.login_layout);
        layout.setBackgroundColor(getThemeColor());
        barlayout=(RelativeLayout)findViewById(R.id.title_bar);
        barlayout.setBackgroundColor(getThemeColor());
        accountET = (EditText)findViewById(R.id.user_name);
        pswET=(EditText)findViewById(R.id.password);

        Drawable drawable= getResources().getDrawable(R.drawable.avatar);
/// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, 100, 100);
        accountET.setCompoundDrawables(drawable,null,null,null);

        Drawable drawable2= getResources().getDrawable(R.drawable.password);
/// 这一步必须要做,否则不会显示.
        drawable2.setBounds(0, 0, 100, 100);
        pswET.setCompoundDrawables(drawable2, null, null, null);


        loginBtn=(Button)findViewById(R.id.login);
        loginBtn.setBackgroundColor(getThemeColor());
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAccount=accountET.getText().toString();
                userPsw=pswET.getText().toString();
                if(TextUtils.isEmpty(userAccount)){
                    ToastUtil.showMessage(LoginActivity.this,"手机号码不能为空");
                return;
                }
                if(TextUtils.isEmpty(userPsw)){
                    ToastUtil.showMessage(LoginActivity.this,"密码不能为空");
                    return;
                }
                final ProgressDialog dialog=new ProgressDialog(LoginActivity.this);
                dialog.setMessage("正在登录...");

                dialog.show();

                BmobUser.loginByAccount(LoginActivity.this, userAccount, userPsw, new LogInListener<User>() {

                    @Override
                    public void done(User user, BmobException e) {
                        // TODO Auto-generated method stub
                        if (user != null) {

                            ToastUtil.showMessage(LoginActivity.this, "登录成功");
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        } else {

                            dialog.dismiss();
                            ToastUtil.showMessage(LoginActivity.this, "手机号或密码有误");
                        }
                    }
                });

            }
        });
        registerBtn=(Button)findViewById(R.id.register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, PhoneActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }
}
