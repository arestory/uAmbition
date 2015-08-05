package uambition.ares.ywq.uambition.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.bean.User;

/**
 * Created by ares on 15/7/24.
 */
public class RegisterActivity extends BaseActivity {
    private EditText nameText;
    private EditText emailText;
    private EditText pswText;
    private Button registerBtn;
    private User user;

    private String phoneNum;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_register);
        Intent intent=getIntent();
        if(intent!=null){
            phoneNum=getIntent().getStringExtra("phone");
        }

    }

    @Override
    public void findViews() {



        nameText=(EditText)findViewById(R.id.user_name);
        emailText=(EditText)findViewById(R.id.user_email);
        pswText=(EditText)findViewById(R.id.password);
        registerBtn=(Button)findViewById(R.id.register);
        user=new User();
        //初始化标题栏
        TitleBarUtil.initTitleBar(this, "注册", new TitleBarUtil.TitleBarCallBack() {
            @Override
            public void leftBtnClickLister() {
                finish();
            }

            @Override
            public void rightBtnClickLister() {

            }
        });
    }

    @Override
    public void getData() {
        final ProgressDialog dialog=new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("正在提交...");


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameText.getText().toString();
                final String psw= pswText.getText().toString();
                final  String email = emailText.getText().toString();

                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                boolean isMatched = matcher.matches();

                if(TextUtils.isEmpty(name)){
                    ToastUtil.showMessage(RegisterActivity.this,"昵称不能为空");
                    return;
                }
                if(!isMatched){

                    ToastUtil.showMessage(RegisterActivity.this,"请输入正确的邮箱地址");
                    return;
                    //Toast.makeText(RegisterActivity.this,"请输入正确的邮箱地址",Toast.LENGTH_LONG).show();
                }
                if(TextUtils.isEmpty(psw)){
                    ToastUtil.showMessage(RegisterActivity.this,"密码不能为空");
                    return;
                }
                user.setUsername(phoneNum);
                user.setPhoneNumber(phoneNum);
                user.setMobilePhoneNumber(phoneNum);
                user.setEmail(email);
                user.setNickName(name);
                user.setPassword(psw);


                dialog.show();
                user.signUp(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {

                        dialog.cancel();
                        ToastUtil.showMessage(RegisterActivity.this, "注册成功！");
                        PhoneActivity.activity.finish();
                        final ProgressDialog dialog2=new ProgressDialog(RegisterActivity.this);
                        dialog2.setMessage("正在进行登录跳转...");

                        dialog2.show();
                        BmobUser.loginByAccount(RegisterActivity.this, phoneNum, psw, new LogInListener<User>() {

                            @Override
                            public void done(User user, BmobException e) {
                                // TODO Auto-generated method stub
                                if (user != null) {

                                    ToastUtil.showMessage(RegisterActivity.this, "登录成功");
                                    Intent intent = new Intent();
                                    intent.setClass(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    dialog2.dismiss();
                                    MainActivity.activity.finish();
                                    finish();
                                } else {

                                    dialog2.dismiss();

                                    finish();

                                }
                            }
                        });


                    }

                    @Override
                    public void onFailure(int i, String s) {
                        dialog.cancel();
                        switch (i){
                            case 202:

                                ToastUtil.showMessage(RegisterActivity.this,"该手机号码已被注册");
                                break;


                            default:
                                ToastUtil.showMessage(RegisterActivity.this,"注册失败！"+"失败码："+i+","+s);
                                break;

                        }

                    }
                });



            }
        });

    }

    @Override
    public void showContent() {

    }
}
