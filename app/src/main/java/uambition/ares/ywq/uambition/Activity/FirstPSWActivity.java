package uambition.ares.ywq.uambition.Activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.Util;

/**
 * Created by ares on 15/7/25.
 */
public class FirstPSWActivity extends BaseActivity {


    private EditText phoneET;
    private EditText codeET;
    private EditText newpswET;
    private Button getCodeBtn;
    private Button nextBtn;
    private String currentPhoneNum;
    @Override
    public void setContentView() {

        setContentView(R.layout.activity_psw_phone);
        RelativeLayout layout= (RelativeLayout)findViewById(R.id.layout);
        //layout.setBackgroundColor(getThemeColor());
        TitleBarUtil.initTitleBar(this, "重置密码", new TitleBarUtil.TitleBarCallBack() {
            @Override
            public void leftBtnClickLister() {
                finish();;
            }

            @Override
            public void rightBtnClickLister() {

            }
        });
    }



    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (second > 0) {
                getCodeBtn.setText(second + "秒后重发");
            } else {
                second = 60;
                getCodeBtn.setText("重新发送");
                getCodeBtn.setClickable(true);
                if (timer != null) {
                    timer.cancel();
                }
            }
        };
    };

    private Timer timer;
    private int second = 60;

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                second--;
                handler.sendEmptyMessage(-100);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 1000, 1000);
    }


    @Override
    public void findViews() {

        phoneET=(EditText)findViewById(R.id.user_phone);
        codeET=(EditText)findViewById(R.id.code);
        newpswET=(EditText)findViewById(R.id.newPSW);
        getCodeBtn=(Button)findViewById(R.id.get_code);
        nextBtn=(Button)findViewById(R.id.next);
        nextBtn.setText("重置密码");
        nextBtn.setBackgroundColor(getThemeColor());
        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum=phoneET.getText().toString();
                    if (Util.isMobileNUM(phoneNum)){
                        startTimer();
                        getCodeBtn.setClickable(false);

                         BmobSMS.requestSMSCode(FirstPSWActivity.this, phoneNum, "注册验证码", new RequestSMSCodeListener() {
                             @Override
                             public void done(Integer integer, BmobException e) {
                                  if(e!=null){
                                      ToastUtil.showMessage(FirstPSWActivity.this,"获取验证码失败,请重新获取");

                                      second=0;
                                  }
                             }
                         });

                    }else{

                        ToastUtil.showMessage(FirstPSWActivity.this, "请输入正确的手机号码");
                    }


            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNum=phoneET.getText().toString();

                String code = codeET.getText().toString();
                String newPassword = newpswET.getText().toString();

                if(TextUtils.isEmpty(code)){
                    ToastUtil.showMessage(FirstPSWActivity.this, "请输入验证码");
                    return;
                }else if(TextUtils.isEmpty(newPassword)){
                    ToastUtil.showMessage(FirstPSWActivity.this, "请输入新密码");
                    return;
                }
                if (Util.isMobileNUM(phoneNum)){

                    BmobUser.resetPasswordBySMSCode(FirstPSWActivity.this, code, newPassword, new ResetPasswordByCodeListener() {
                        @Override
                        public void done(BmobException ex) {
                            if (ex == null) {
                                ToastUtil.showMessage(FirstPSWActivity.this, "重置密码成功");

                                finish();

                            } else {
                                ToastUtil.showMessage(FirstPSWActivity.this, "重置失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                            }
                        }
                    });

                }else{
                    ToastUtil.showMessage(FirstPSWActivity.this,"请输入正确的手机号码");


                }


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
