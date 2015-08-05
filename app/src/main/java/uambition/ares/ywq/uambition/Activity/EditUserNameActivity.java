package uambition.ares.ywq.uambition.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.bean.User;

/**
 * Created by ares on 15/7/25.
 */
public class EditUserNameActivity extends BaseActivity {


    private EditText nameET;
    private Button confirmBtn;
    private User user;

    private ProgressDialog dialog;
    private static final int TO_EDIT_USER_NAME=1002;
    @Override
    public void setContentView() {

        setContentView(R.layout.activity_edit_username);
        user= BmobUser.getCurrentUser(this,User.class);
        dialog=new ProgressDialog(this);
        dialog.setMessage("正在修改昵称");
        TitleBarUtil.initTitleBar(this, "修改昵称", new TitleBarUtil.TitleBarCallBack() {
            @Override
            public void leftBtnClickLister() {
                finish();
                ;
            }

            @Override
            public void rightBtnClickLister() {

            }
        });
    }






    @Override
    public void findViews() {

        nameET =(EditText)findViewById(R.id.user_name);
         confirmBtn =(Button)findViewById(R.id.confirm);



        confirmBtn.setBackgroundColor(getThemeColor());
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String new_name = nameET.getText().toString();



                if(TextUtils.isEmpty(new_name)){

                    ToastUtil.showMessage(EditUserNameActivity.this,"昵称不能为空");
                    return;
                }
                dialog.show();
                user.setNickName(new_name );
                user.update(EditUserNameActivity.this, user.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.showMessage(EditUserNameActivity.this,"修改成功");
                        setResult(TO_EDIT_USER_NAME);
                        finish();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtil.showMessage(EditUserNameActivity.this,"修改失败，请检查网络");
                        dialog.dismiss();
                    }
                });


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
