package uambition.ares.ywq.uambition.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.Util;
import uambition.ares.ywq.uambition.bean.Ambition;

/**
 * Created by ares on 15/7/25.
 */
public class PhoneActivity extends BaseActivity {


    private EditText phoneET;
    private EditText codeET;
    private Button getCodeBtn;
    private Button nextBtn;
    private String currentPhoneNum;

    public static Activity activity;
    @Override
    public void setContentView() {
        activity=this;
        setContentView(R.layout.activity_register_phone);
        TitleBarUtil.initTitleBar(this, "输入手机号码", new TitleBarUtil.TitleBarCallBack() {
            @Override
            public void leftBtnClickLister() {
                finish();
                ;
            }

            @Override
            public void rightBtnClickLister() {

            }
        });

//        Intent intent = getIntent();
//        Ambition ambition = (Ambition)intent.getSerializableExtra("ambition");
//        if(ambition!=null){
//            ToastUtil.showMessage(this,"ambition==null"+ambition.getTitle()+(ambition.getBeginTime()));
//
//            return;
//        }
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
        getCodeBtn=(Button)findViewById(R.id.get_code);
        nextBtn=(Button)findViewById(R.id.next);
        getCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum=phoneET.getText().toString();
                    if (Util.isMobileNUM(phoneNum)){
                        startTimer();
                        getCodeBtn.setClickable(false);


                         BmobSMS.requestSMSCode(PhoneActivity.this, phoneNum, "注册验证码", new RequestSMSCodeListener() {
                             @Override
                             public void done(Integer integer, BmobException e) {
                                  if(e!=null){
                                      ToastUtil.showMessage(PhoneActivity.this,"获取验证码失败,请重新获取");

                                      second=0;

                                  }
                             }
                         });

                    }else{

                        ToastUtil.showMessage(PhoneActivity.this, "请输入正确的手机号码");
                    }


            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNum=phoneET.getText().toString();

                String code = codeET.getText().toString();

                if (Util.isMobileNUM(phoneNum)){
                    BmobSMS.verifySmsCode(PhoneActivity.this, phoneNum, code, new VerifySMSCodeListener() {
                        @Override
                        public void done(BmobException e) {

                            if(e==null){//短信验证码已验证成功

                                ToastUtil.showMessage(PhoneActivity.this, "验证成功");

                                Intent intent=new Intent();
                                intent.setClass(PhoneActivity.this,RegisterActivity.class);
                                intent.putExtra("phone",phoneNum);//传递手机号码
                                startActivity(intent);

                            }else{

                                ToastUtil.showMessage(PhoneActivity.this,"验证码有误");

                            }

                        }
                    });
                }else{
                    ToastUtil.showMessage(PhoneActivity.this,"请输入正确的手机号码");


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
