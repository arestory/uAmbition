package uambition.ares.ywq.uambition.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import B.The;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.waps.AppConnect;
import uambition.ares.ywq.uambition.App.AmbitionAPP;
import uambition.ares.ywq.uambition.Fragment.MainFragment;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ImageLoader;
import uambition.ares.ywq.uambition.Util.ThemeColorUtil;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.slidr.Slidr;
import uambition.ares.ywq.uambition.adapter.CommentAdapter;
import uambition.ares.ywq.uambition.bean.Ambition;
import uambition.ares.ywq.uambition.bean.AmbitionDate;
import uambition.ares.ywq.uambition.bean.Comment;
import uambition.ares.ywq.uambition.bean.User;
import uambition.ares.ywq.uambition.view.CircularImageView;
import uambition.ares.ywq.uambition.view.datepicker.date.DatePickerDialog;

/**
 * Created by ares on 15/8/6.
 */
public class AmbitionDetailActivity extends  BaseActivity{


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
    private List<Comment> comments;
    private ListView comment_listview;
    private CommentAdapter adapter;
    private EditText commentEditText;
    private Button sendBtn;


    private int MODE=1;//1为查看详情，0为修改模式

    private Ambition currentAmbition;//目标对象
    private User author;//作者
    private User commenter;//评论人
    private User toCommenter; //@人

    private CircularImageView authorImg;
    private TextView authorName;
    private LinearLayout authorLayout;
    private LinearLayout miniLayout;
    private LinearLayout progress_layout;
    private TextView getAgain;
    private String openOrCloseADV;
    private int comment_Length;//评论内容长度
    private String commentHeadString;//评论内容的头内容
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
                                ToastUtil.showMessage(AmbitionDetailActivity.this, "亲,开始日期不能比结束时间晚哦");
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
                            ToastUtil.showMessage(AmbitionDetailActivity.this,"亲,结束日期不能比开始日期早哦");
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_ambition_detail);
        //设置滑动退出
        Slidr.attach(this);
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
       // currentAmbition =(Ambition)bundle.getSerializable("ambition");

        //currentAmbition = (Ambition)intent.getSerializableExtra("ambition");
       // String id = intent.getStringExtra("ambitionID");
        String id =   ThemeColorUtil.getData(this,"ambition","ambitionID");
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("正在查询");
        dialog.show();


        BmobQuery<Ambition> query = new BmobQuery<Ambition>();
        query.include("author");
        query.getObject(this, id, new GetListener<Ambition>() {
            @Override
            public void onSuccess(Ambition ambition) {
                currentAmbition=ambition;
                //更新页面
                refreshData();
                dialog.dismiss();
            }

            @Override
            public void onFailure(int i, String s) {

                dialog.dismiss();
            }
        });


    }

    public void  refreshData(){

        final BmobQuery<Comment> query = new BmobQuery<Comment>();


        query.addWhereEqualTo("ambition", new BmobPointer(currentAmbition));
        query.include("commenter");
        //降序排序
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                comments.addAll(list);
                adapter = new CommentAdapter(AmbitionDetailActivity.this, comments);
                comment_listview.setAdapter(adapter);
                progress_layout.setVisibility(View.GONE);
                //  ToastUtil.showMessage(AmbitionDetailActivity.this,currentAmbition.getTitle());
                changeListViewHeight();
            }

            @Override
            public void onError(int i, String s) {
                getAgain.setText("获取评论失败，点击重新获取");
            }
        });


        if(currentAmbition!=null){

            ambitionText.setText(currentAmbition.getTitle());
            beginText.setText("开始日期：" + currentAmbition.getBeginTime());
            endText.setText("结束日期："+currentAmbition.getEndTime());
            privateCheckBox.setChecked((boolean) currentAmbition.isPersonal());
            author = currentAmbition.getAuthor();
            commenter=BmobUser.getCurrentUser(this,User.class);
            AmbitionAPP.getInstance().getImageLoader().loadBigPic(author.getUser_img_url(), 100, new ImageLoader.BigImageCallback() {
                @Override
                public void imageLoaded(String url, final Bitmap bmp) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            authorImg.setImageBitmap(bmp);
                        }
                    });


                }
            });
            authorName.setText(author.getNickName());
            if (isUserSAmbition()) {
                sendBtn.setText("签到");
            } else {
                sendBtn.setText("评论");
            }

        }
        if(currentAmbition!=null){
            beginDate=AmbitionDate.getDateFromStr(currentAmbition.getBeginTime());
            endDate=AmbitionDate.getDateFromStr(currentAmbition.getEndTime());

        }

    }



    @Override
    public void findViews() {
        ambitionText=(EditText)findViewById(R.id.ambition);
        authorLayout=(LinearLayout)findViewById(R.id.author_layout);
        progress_layout=(LinearLayout)findViewById(R.id.progress_layout);
        getAgain=(TextView)findViewById(R.id.get);
        authorLayout.setBackgroundColor(getThemeColor());
        ambitionText.setBackgroundColor(getThemeColor());
        beginText=(TextView)findViewById(R.id.begin_date_text);
        endText=(TextView)findViewById(R.id.end_date_text);
        authorImg=(CircularImageView)findViewById(R.id.author_img);
        authorName=(TextView)findViewById(R.id.author_name);

        titleView= TitleBarUtil.getTitleView(this);
        privateCheckBox=(CheckBox)findViewById(R.id.isPrivate);
        //评论
        commentEditText=(EditText)findViewById(R.id.edit_comment);

        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()>0){

                    sendBtn.setClickable(true);
                    sendBtn.setEnabled(true);

                    if (commentHeadString != null) {
                        //此操作可将@的人去掉
                        if (s.length() == commentHeadString.length() - 1) {

                            if (s.toString().contains(commentHeadString.replace(" ", ""))) {

                                commentEditText.setText("");
                                toCommenter = null;
                            }
                        }
                    }

                }else{

                    if (isUserSAmbition()) {
                        sendBtn.setText("签到");
                    } else {
                        sendBtn.setText("评论");
                    }

                    //重置
                    toCommenter = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        sendBtn=(Button)findViewById(R.id.send_comment);
        sendBtn.setBackgroundColor(getThemeColor());
        comments=new ArrayList<Comment>();
        comment_listview=(ListView)findViewById(R.id.comment_listview);

        comment_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = (Comment)adapter.getItem(position);
                if(!comment.getCommenter().getObjectId().equals(commenter.getObjectId())){
                    toCommenter=comment.getCommenter();
                    commentHeadString="回复:"+"@"+toCommenter.getNickName()+" ";
                    commentEditText.setText(commentHeadString);
                    comment_Length=commentHeadString.length();
                    sendBtn.setText("回复");
                    commentEditText.requestFocus();
                    showInput();
                }else{
                    toCommenter=null;
                }

            }
        });




        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    str    =    simpleDateFormat.format(curDate);

        initAVD();


//        final BmobQuery<Comment> query = new BmobQuery<Comment>();
//
//
//        query.addWhereEqualTo("ambition", new BmobPointer(currentAmbition));
//        query.include("commenter");
//        //降序排序
//        query.order("-createdAt");
//        query.findObjects(this, new FindListener<Comment>() {
//            @Override
//            public void onSuccess(List<Comment> list) {
//                comments.addAll(list);
//                adapter=new CommentAdapter(AmbitionDetailActivity.this,comments);
//                comment_listview.setAdapter(adapter);
//                progress_layout.setVisibility(View.GONE);
//                changeListViewHeight();
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                getAgain.setText("获取评论失败，点击重新获取");
//            }
//        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String content=commentEditText.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ToastUtil.showMessage(AmbitionDetailActivity.this,"评论不能为空");
                    return;
                }
                final Comment comment = new Comment();
                comment.setAmbition(currentAmbition);

                comment.setContent(content);
                comment.setCommenter(BmobUser.getCurrentUser(AmbitionDetailActivity.this, User.class));

                hindInput();

                //更新数据
                comments.add(0, comment);
                adapter.notifyDataSetChanged();
                changeListViewHeight();
                commentEditText.setText("");

                if(isUserSAmbition()){
                    sendBtn.setText("签到");
                }else{
                    sendBtn.setText("评论");
                }


                final BmobPushManager bmobPushManager = new BmobPushManager(AmbitionDetailActivity.this);
                //先保存评论数据再进行关联
                comment.save(AmbitionDetailActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        //ToastUtil.showMessage(AmbitionDetailActivity.this, "提交成功");
                        BmobRelation relation = new BmobRelation();

                        relation.add(comment);
                        currentAmbition.setCommentList(relation);
                        currentAmbition.update(AmbitionDetailActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                ToastUtil.showMessage(AmbitionDetailActivity.this, "评论成功");
                                BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();

                                String commentContent="";
                                String type="";
                                if(toCommenter!=null){
                                    query.addWhereEqualTo("receiverId", toCommenter.getObjectId());
                                    commentContent="有人在评论中@了你："+comment.getContent();
                                    type="talkAbout";

                                }else{
                                    query.addWhereEqualTo("receiverId", author.getObjectId());
                                    commentContent="有人评论了你的目标："+comment.getContent();
                                    type="comment";
                                }

                                JSONObject pushObject= new JSONObject();
                                try {
                                    //评论人
                                    pushObject.put("commenter", commenter.getNickName());
                                    //评论的目标
                                    pushObject.put("ambition", currentAmbition.getTitle());
                                    //评论的目标id
                                    pushObject.put("ambitionID", currentAmbition.getObjectId());
                                    //评论内容
                                    pushObject.put("detail", comment.getContent());
                                    //评论类型 回复还是评论
                                    pushObject.put("type", type);

                                    ThemeColorUtil.saveColor(AmbitionDetailActivity.this, "ambition", "ambitionID", currentAmbition.getObjectId());


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                bmobPushManager.setQuery(query);

                                //
                                //如果是自己的目标，评论则不推送
                                if(!commenter.getObjectId().equals(author.getObjectId())){
                                    bmobPushManager.pushMessage(pushObject, new PushListener() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {

                                            ToastUtil.showMessage(AmbitionDetailActivity.this,"推送失败"+s);
                                        }
                                    });
                                 }

                                toCommenter=null;
                                commentEditText.setText("");
                                if(isUserSAmbition()){
                                    sendBtn.setText("签到");
                                }else{
                                    sendBtn.setText("评论");
                                }
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                comments.remove(0);
                                adapter.notifyDataSetChanged();
                                changeListViewHeight();
                                ToastUtil.showMessage(AmbitionDetailActivity.this, "提交失败" + s);

                            }
                        });
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtil.showMessage(AmbitionDetailActivity.this, "提交失败" + s);
                    }
                });



            }
        });

        if(MODE==1){
            authorLayout.setVisibility(View.VISIBLE);
        }else{
            authorLayout.setVisibility(View.GONE);

        }
        if(currentAmbition!=null){
            beginDate=AmbitionDate.getDateFromStr(currentAmbition.getBeginTime());
            endDate=AmbitionDate.getDateFromStr(currentAmbition.getEndTime());

        }


        switch (MODE){

            case 0:


                break;
            //查看模式
            case 1:
                ambitionText.setEnabled(false);
                ambitionText.setGravity(Gravity.CENTER);
                privateCheckBox.setEnabled(false);

                break;
        }
    }
    //判断是自己的目标还是别人的目标，显示是签到还是回复
    public boolean isUserSAmbition(){
        boolean flag=false;
        //如果是
        if(commenter!=null&&author!=null){
            if(commenter.getObjectId().equals(author.getObjectId())){
                flag=true;
            }else{


                flag=false;
            }
        }

        return  flag;
    }
    //重新获取评论列表
    public void getCommentData(View v ){

        getAgain.setText("正在获取评论");

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("ambition", new BmobPointer(currentAmbition));
        query.include("commenter");
        //降序排序
        query.order("-createdAt");
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                progress_layout.setVisibility(View.GONE);
                comments.addAll(list);
                adapter=new CommentAdapter(AmbitionDetailActivity.this,comments);
                comment_listview.setAdapter(adapter);
//                progress_layout.setVisibility(View.GONE);
                changeListViewHeight();

            }

            @Override
            public void onError(int i, String s) {
                progress_layout.setVisibility(View.VISIBLE);
                getAgain.setText("获取评论失败，点击重新获取");

            }
        });




    }

    //弹出键盘
    private void showInput(){
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(commentEditText, 0);
    }

    //隐藏键盘
    private void hindInput(){
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
//    	imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void changeListViewHeight(){


        int totalHeight=0;
        for(int i=0;i<adapter.getCount();i++){
            View listItem = adapter.getView(i, null, comment_listview);
            listItem.measure(0, 0);
            totalHeight+=listItem.getMeasuredHeight();
        }
      final   ViewGroup.LayoutParams params = comment_listview.getLayoutParams();
        params.height = totalHeight+(comment_listview.getDividerHeight()*adapter.getCount());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                comment_listview.setLayoutParams(params);
            }
        });

    }



    /**
     * 初始化广告
     */
    private void initAVD(){
        //设置迷你广告背景颜色
        AppConnect.getInstance(this).setAdBackColor(getThemeColor());
        //设置迷你广告广告语颜色 AppConnect.getInstance(this).setAdForeColor(Color.YELLOW); //若未设置以上两个颜色,则默认为黑底白字
        miniLayout =(LinearLayout)findViewById(R.id.miniAdLinearLayout);
        openOrCloseADV=AppConnect.getInstance(this).getConfig("openADV", "open");
        String spOpenOrClose = ThemeColorUtil.getData(this, "ambition", "isOpenADV");



        if(spOpenOrClose!=null){
            if(!TextUtils.isEmpty(spOpenOrClose)){
                if(!spOpenOrClose.equals("close")){

                    openADV();
                }
            }else{
                openADV();
            }
        }else{

            openADV();


        }




    }


    public  void openADV(){
        if(openOrCloseADV.equals("open")){
            AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10); //默认 10 秒切换一次广告

        }else{
            miniLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getData() {
//        if(isUserSAmbition()){
//            sendBtn.setText("签到");
//        }else{
//
//            sendBtn.setText("评论");
//        }

//        if(currentAmbition!=null){
//
//            ambitionText.setText(currentAmbition.getTitle());
//            beginText.setText("开始日期：" + currentAmbition.getBeginTime());
//            endText.setText("结束日期："+currentAmbition.getEndTime());
//            privateCheckBox.setChecked((boolean) currentAmbition.isPersonal());
//            author = currentAmbition.getAuthor();
//            commenter=BmobUser.getCurrentUser(this,User.class);
//            AmbitionAPP.getInstance().getImageLoader().loadBigPic(author.getUser_img_url(), 100, new ImageLoader.BigImageCallback() {
//                @Override
//                public void imageLoaded(String url, final Bitmap bmp) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            authorImg.setImageBitmap(bmp);
//                        }
//                    });
//
//
//                }
//            });
//            authorName.setText(author.getNickName());
//
//        }







    }

    @Override
    public void showContent() {

        boolean isRightBtnVisible=true;
        String title ="";
        if (MODE==0){
            isRightBtnVisible=true;
            title="修改";
        }else if (MODE==1){
            isRightBtnVisible=false;
            title="详情";

        }
        initDatePicker();
        final ProgressDialog dialog=new ProgressDialog(AmbitionDetailActivity.this);
        dialog.setMessage("正在提交...");

        TitleBarUtil.initTitleBarWithRightBtn(this, isRightBtnVisible, title, new TitleBarUtil.TitleBarCallBack() {
            @Override
            public void leftBtnClickLister() {
                finish();
            }

            @Override
            public void rightBtnClickLister() {

                if(endDate!=null){
                    dialog.show();
                    String content = ambitionText.getText().toString();
                    currentAmbition.setPersonal(privateCheckBox.isChecked());
                    currentAmbition.setTitle(content);
                    currentAmbition.setBeginTime(beginDate.getDateStr());
                    currentAmbition.setEndTime(endDate.getDateStr());
                    currentAmbition.setBetweenDay(AmbitionDate.getBetweenDays(beginDate.getDateStr(), endDate.getDateStr()));
                    String id = author.getObjectId();

                    currentAmbition.setAuthor(author);

                    currentAmbition.update(AmbitionDetailActivity.this, currentAmbition.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            Intent intent1 = new Intent(AmbitionDetailActivity.this, MainActivity.class);
                            intent1.putExtra("ambition", currentAmbition);
                            if(getIntent()!=null){
                                int position= getIntent() .getIntExtra("position", 0);
                                intent1.putExtra("position", position);
                            }
                            setResult(MainFragment.EDIT_AMBITION, intent1);
                            ToastUtil.showMessage(AmbitionDetailActivity.this, "修改成功");
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            dialog.dismiss();
                            ToastUtil.showMessage(AmbitionDetailActivity.this, "修改失败，请检查网络" + s + ":" + i);

                        }
                    });
                }else{
                    ToastUtil.showMessage(AmbitionDetailActivity.this,"请填写结束日期");
                }

            }
        });






    }

    public void showEndDatePicker(View v){
        if(MODE==0){
            datePickerEnd.show(getFragmentManager(), "");
        }


    }

    public void showBeginDatePicker(View v){
        if(MODE==0){
            datePickerBegin.show(getFragmentManager(),"");}

    }

}
