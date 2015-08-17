package uambition.ares.ywq.uambition.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.slidr.Slidr;
import uambition.ares.ywq.uambition.bean.User;

/** 修改密码的Activity
 * Created by ares on 15/7/24.
 */
public class ResetPSWActivity extends BaseActivity {
    private EditText oldPSW;
    private EditText newPSW;
    private EditText newPSW2;
    private Button registerBtn;
    private User user;
    private static final int PHONE_IS_EXIST=1001;

    private static final int FIND_PSW_SUCCESS=1002;
    private static final int FIND_PSW_FAIL=1003;
    private static final int PHONE_IS_NOT_EXIST=1004;
    private static final int PSW_IS_WRONG=1005;
    private static final int WRONG_NET_WORK=1006;

    private String phoneNum;
    private FindPSWHandler handler = new FindPSWHandler();

    private String oldPSWStr;
    private  String newPSWStr;
    private String newPSWStr2;


    private ProgressDialog dialog;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_register);
        Intent intent=getIntent();
        if(intent!=null){
            phoneNum=getIntent().getStringExtra("phone");
        }

        //设置滑动退出
        Slidr.attach(this);
    }

    @Override
    public void findViews() {



        oldPSW =(EditText)findViewById(R.id.user_name);
        newPSW =(EditText)findViewById(R.id.user_email);
        newPSW2 =(EditText)findViewById(R.id.password);
        registerBtn=(Button)findViewById(R.id.register);


        //初始化标题栏
        TitleBarUtil.initTitleBar(this, "修改密码", new TitleBarUtil.TitleBarCallBack() {
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
       dialog=new ProgressDialog(ResetPSWActivity.this);
        dialog.setMessage("正在重置...");


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  oldPSWStr = oldPSW.getText().toString();
                 newPSWStr= newPSW2.getText().toString();
                 newPSWStr2 = newPSW.getText().toString();


                if(TextUtils.isEmpty(oldPSWStr)||TextUtils.isEmpty(newPSWStr)||TextUtils.isEmpty(newPSWStr2)){

                    ToastUtil.showMessage(ResetPSWActivity.this,"密码不能为空");
                    return;

                }else if(!newPSWStr.equals(newPSWStr2)){

                    ToastUtil.showMessage(ResetPSWActivity.this,"新密码前后不一致");
                    return;
                }


                dialog.show();
                BmobQuery<User> bmobQuery=new BmobQuery<User>();
                bmobQuery.addWhereEqualTo("phoneNumber",phoneNum);


                bmobQuery.findObjects(ResetPSWActivity.this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {

                        for (User u : list) {

                            if (u.getPhoneNumber().equals(phoneNum)) {

                                user = u;
                                Message msg = new Message();
                                msg.what=PHONE_IS_EXIST;
                                msg.obj=user;
                                if(user.getPassword().equals(oldPSWStr)){
                                    handler.sendMessage(msg);
                                    continue;
                                }else{
                                    handler.sendEmptyMessage(PSW_IS_WRONG);
                                }


                            }
                        }
                        if(list.size()==0){

                            handler.sendEmptyMessage(PHONE_IS_NOT_EXIST);
                        }

                    }

                    @Override
                    public void onError(int i, String s) {
                        handler.sendEmptyMessage(WRONG_NET_WORK);

                    }
                });







            }
        });

    }

    @Override
    public void showContent() {

    }

    private class FindPSWHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {



            super.handleMessage(msg);


            switch (msg.what){

                case PHONE_IS_EXIST:
                    User user = (User)msg.obj;
                    if(user!=null){

                        user.setPassword(newPSW.getText().toString());
                        user.update(ResetPSWActivity.this, user.getObjectId(), new UpdateListener() {
                            @Override
                            public void onSuccess() {

                                handler.sendEmptyMessage(FIND_PSW_SUCCESS);
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                handler.sendEmptyMessage(WRONG_NET_WORK);

                            }
                        });

                    }


                    break;

                case  PHONE_IS_NOT_EXIST:

                    dialog.dismiss();

                    ToastUtil.showMessage(ResetPSWActivity.this,"该号码未注册");
                    break;

                case PSW_IS_WRONG:
                    dialog.dismiss();
                    ToastUtil.showMessage(ResetPSWActivity.this,"旧密码不正确，请重新输入");

                    break;

                case FIND_PSW_SUCCESS:
                    dialog.dismiss();
                    ToastUtil.showMessage(ResetPSWActivity.this,"密码修改成功");

                    dialog.dismiss();
                    break;
                case WRONG_NET_WORK:
                    dialog.dismiss();
                    ToastUtil.showMessage(ResetPSWActivity.this,"网络出错，请检查网路");

                    break;
            }
        }
    }
}
