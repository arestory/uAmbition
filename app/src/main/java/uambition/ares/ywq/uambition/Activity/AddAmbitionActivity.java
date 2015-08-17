package uambition.ares.ywq.uambition.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.Util;
import uambition.ares.ywq.uambition.Util.slidr.Slidr;
import uambition.ares.ywq.uambition.bean.Ambition;
import uambition.ares.ywq.uambition.bean.AmbitionDate;
import uambition.ares.ywq.uambition.bean.User;
import uambition.ares.ywq.uambition.view.AmbitionDialog;
import uambition.ares.ywq.uambition.view.datepicker.date.DatePickerDialog;

/**
 * Created by ares on 15/7/24.
 */
public class AddAmbitionActivity extends BaseActivity {

    private DatePickerDialog datePickerBegin;
    private DatePickerDialog datePickerEnd;
    private final Calendar mCalendar = Calendar.getInstance();
    private EditText ambitionText;
    private TextView beginText;
    private TextView endText;
    private TextView titleView;
    private CheckBox privateCheckBox;
    private AmbitionDate beginDate;
    private AmbitionDate endDate;
    private int MODE=1;//0为查看详情，1为添加模式，2为修改模式

    private Ambition newAmbition;//新目标对象
    private User user;
    private void initDatePicker() {


        datePickerBegin = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePickerDialog datePickerDialog,
                                          int year, int month, int day) {
                        beginDate=new AmbitionDate();
                        beginDate.setDate(year, month + 1, day);
                       // beginText.setText("开始日期："+beginDate.getDateStr());
                        //证明曾选择结束日期
                        if(endDate!=null){
                            if(!AmbitionDate.compareIsRightDates(beginDate, endDate)){
                                ToastUtil.showMessage(AddAmbitionActivity.this,"亲,开始日期不能比结束时间晚哦");
                                return;
                            }
                            datePickerBegin.dismiss();
                            beginText.setText("开始日期："+beginDate.getDateStr());
                        }else{
                            datePickerBegin.dismiss();
                            beginText.setText("开始日期："+beginDate.getDateStr());
                        }


                    }

                    @Override
                    public void onDismiss() {
                        // TODO Auto-generated method stub

                    }

                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerEnd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePickerDialog datePickerDialog,
                                          int year, int month, int day) {

                        endDate=new AmbitionDate();
                        endDate.setDate(year, month + 1, day);
                       // endText.setText("结束日期："+endDate.getDateStr());
                        //证明曾选择结束日期

                            if(!AmbitionDate.compareIsRightDates(beginDate, endDate)){
                                ToastUtil.showMessage(AddAmbitionActivity.this,"亲,结束日期不能比开始日期早哦");
                                return;

                        }else{
                                datePickerEnd.dismiss();
                                endText.setText("结束日期："+endDate.getDateStr());
                        }

                    }

                    @Override
                    public void onDismiss() {
                        // TODO Auto-generated method stub

                    }

                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        // timePickerDialog24h.show(activity.getManager(), "");
    }



    @Override
    public void setContentView() {
        final Intent intent=getIntent();
        //设置滑动退出
        Slidr.attach(this);
        if(intent!=null){

            newAmbition = (Ambition)intent.getSerializableExtra("ambition");
            if(newAmbition ==null){

                newAmbition =new Ambition();
            }
        }
        user=BmobUser.getCurrentUser(this,User.class);
        setContentView(R.layout.activity_add_ambition);
        initDatePicker();


        final ProgressDialog dialog=new ProgressDialog(AddAmbitionActivity.this);
        dialog.setMessage("正在提交...");

        LinearLayout author_layout = (LinearLayout)findViewById(R.id.author_layout);
        author_layout.setVisibility(View.GONE);
        TitleBarUtil.initTitleBarWithRightBtn(this, true, "新目标", new TitleBarUtil.TitleBarCallBack() {
            @Override
            public void leftBtnClickLister() {
                finish();
            }

            @Override
            public void rightBtnClickLister() {

                if(endDate!=null){
                    final AmbitionDialog alertDialog=Util.showDialog(AddAmbitionActivity.this,"正在提交");
                    //dialog.show();
                    String content = ambitionText.getText().toString();
                    newAmbition.setPersonal(privateCheckBox.isChecked());
                    newAmbition.setTitle(content);
                     newAmbition.setBeginTime(beginDate.getDateStr());
                    newAmbition.setEndTime(endDate.getDateStr());
                    newAmbition.setFavorite_num(0);
                    newAmbition.setBetweenDay(AmbitionDate.getBetweenDays(beginDate.getDateStr(), endDate.getDateStr()));
                    String id = user.getObjectId();

                    newAmbition.setAuthor(user);
                    newAmbition.save(AddAmbitionActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            Util.cancelDialog(alertDialog);
                            Intent intent1 = new Intent(AddAmbitionActivity.this, MainActivity.class);
                            intent1.putExtra("ambition", newAmbition);

                            setResult(1000, intent1);
                            ToastUtil.showMessage(AddAmbitionActivity.this, "提交成功");
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            dialog.dismiss();
                            Util.cancelDialog(alertDialog);
                            ToastUtil.showMessage(AddAmbitionActivity.this, "提交失败" + s + ":" + i);

                        }
                    });

                }else{
                    ToastUtil.showMessage(AddAmbitionActivity.this,"请填写结束日期");
                }

            }
        });


    }
    public void showEndDatePicker(View v){
        datePickerEnd.show(getFragmentManager(), "");

    }

    public void showBeginDatePicker(View v){
        datePickerBegin.show(getFragmentManager(),"");

    }

    @Override
    public void findViews() {

        ambitionText=(EditText)findViewById(R.id.ambition);
        ambitionText.setBackgroundColor(getThemeColor());
        beginText=(TextView)findViewById(R.id.begin_date_text);
        endText=(TextView)findViewById(R.id.end_date_text);
        titleView=TitleBarUtil.getTitleView(this);
        privateCheckBox=(CheckBox)findViewById(R.id.isPrivate);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        Date    curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    str    =    simpleDateFormat.format(curDate);

        beginText.setText("开始时间：" + str);

        //初始化开始日期
       beginDate= AmbitionDate.getDateFromStr(str);
      //  endDate=beginDate;//默认相同日期

    }

    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }
}
