package uambition.ares.ywq.uambition.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import uambition.ares.ywq.uambition.App.AmbitionAPP;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ImageLoader;
import uambition.ares.ywq.uambition.Util.TitleBarUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.Util;
import uambition.ares.ywq.uambition.Util.slidr.Slidr;
import uambition.ares.ywq.uambition.adapter.MenuListAdapter;
import uambition.ares.ywq.uambition.bean.MenuItemBean;
import uambition.ares.ywq.uambition.bean.User;
import uambition.ares.ywq.uambition.view.CircularImageView;
import uambition.ares.ywq.uambition.view.MaterialDialog;

/**
 * Created by ares on 15/7/30.
 */
public class UserDetailActivity extends  BaseActivity {

    private Uri currentPhotoUri;
    private CircularImageView userLogo;
    private String user_image_url;
    private TextView userName;
    private TextView userEmail;
    private TextView userPhone;
    private MaterialDialog  photoDialog;
    private User currentUser;

    private String imgFileName;//头像名字
    private String imgFilePath;//头像路径
    private String localPicPath;//头像本地路径

    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private Dialog dialog;
    private  UploadHandler handler=new UploadHandler();
    private static final int UPLOAD_SUCCESS=1001;
    private boolean isUserImageChange=false;

    private ListView userInfoList;
    private MenuListAdapter adapter;
    private static final int TO_EDIT_USER_NAME=1002;
    private List<MenuItemBean> itemS;

    private Activity activity;
    private boolean hadChangeUserName=false;//是否已修改用户名
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_user_detail);
        currentUser= BmobUser.getCurrentUser(this,User.class);
        activity=this;
        //设置滑动退出
        Slidr.attach(this);
    }

    @Override
    public void findViews() {
        userInfoList=(ListView)findViewById(R.id.user_info_list);
        userLogo=(CircularImageView)findViewById(R.id.avatar);
        userName=(TextView)findViewById(R.id.user_name);
        userEmail=(TextView)findViewById(R.id.user_email);
        userPhone=(TextView)findViewById(R.id.user_phone);
        userName.setVisibility(View.GONE);
        userEmail.setVisibility(View.GONE);
        userPhone.setVisibility(View.GONE);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在上传头像...");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);

        dialog=new Dialog(this);
        builder=new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage("更换头像");
        builder.setPositiveButton("从相册选取", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selectFromALUM();

            }
        });
        builder.setNegativeButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takeCamera();
                ;
            }
        });
        editUserPhoto();

        AmbitionAPP.getInstance().getImageLoader().loadBigPic(currentUser.getUser_img_url(), 100, new ImageLoader.BigImageCallback() {
            @Override
            public void imageLoaded(String url, final Bitmap bmp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userLogo.setImageBitmap(bmp);
                    }
                });

            }
        });
        initList();



    }

    private void initList(){
        itemS=new ArrayList<MenuItemBean>();
        MenuItemBean userName = new MenuItemBean();
        userName.setImgResId(R.drawable.avatar);
        userName.setTitle(currentUser.getNickName());

        MenuItemBean phoneItem = new MenuItemBean();
        phoneItem.setImgResId(R.drawable.phone);
        phoneItem.setTitle(currentUser.getPhoneNumber());

        MenuItemBean emailItem = new MenuItemBean();
        emailItem.setImgResId(R.drawable.email);
        emailItem.setTitle(currentUser.getEmail());

        MenuItemBean pswItem = new MenuItemBean();
        pswItem.setImgResId(R.drawable.password);
        pswItem.setTitle("重置密码");

        itemS.add(userName);
        itemS.add(phoneItem);
        itemS.add(emailItem);
        itemS.add(pswItem);
        adapter=new MenuListAdapter(this,itemS);
        userInfoList.setAdapter(adapter);


        userInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(itemS.get(position).getTitle().equals("重置密码")){
                    Intent intent = new Intent(UserDetailActivity.this,FirstPSWActivity.class);
                    startActivity(intent);
                }
                else if(position==0){
                    Intent intent = new Intent(UserDetailActivity.this,EditUserNameActivity.class);
                    startActivityForResult(intent, TO_EDIT_USER_NAME);
                    //startActivity(intent);


                }

            }
        });


    }

    @Override
    public void getData() {

        userLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // photoDialog.show();
                builder.show();
                ;
            }
        });
    }

    @Override
    public void showContent() {

        TitleBarUtil.initTitleBar(activity, "个人信息", new TitleBarUtil.TitleBarCallBack() {
            @Override
            public void leftBtnClickLister() {

                Intent intent = new Intent();
                intent.putExtra("logo_url",localPicPath);
                if(isUserImageChange){
                    setResult(2000,intent);
                }else if(hadChangeUserName){
                    setResult(TO_EDIT_USER_NAME,intent);
                }
                finish();
            }

            @Override
            public void rightBtnClickLister() {

            }
        });
        RelativeLayout layout=(RelativeLayout)findViewById(R.id.detail_layout);
        layout.setBackgroundColor(getThemeColor());

        if(currentUser!=null){
            userName.setText(currentUser.getNickName());
            userPhone.setText(currentUser.getMobilePhoneNumber());
            userEmail.setText(currentUser.getEmail());

        }
    }

    private void takeCamera(){
        // 创建保存图片的文件
        File photoFile = getOutputMediaFileUri();
        Uri imageUri = Uri.fromFile(photoFile);
        currentPhotoUri = imageUri;
        // 调用摄像头程序
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        // 设置图片文件名
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }
    private void selectFromALUM()
    {
        String IMAGE_TYPE = "image/*";
        int IMAGE_CODE = 2; // 这里的IMAGE_CODE是自己任意定义的

        // 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片

        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType(IMAGE_TYPE);
        startActivityForResult(getAlbum, IMAGE_CODE);

    }
    private void editUserPhoto() {

        photoDialog = new MaterialDialog(this);
        photoDialog.setTitle("更换头像");
        photoDialog.setMessage("");

        photoDialog.setPositiveButton("拍照", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog.dismiss();
                takeCamera();

            }
        }).setNegativeButton("从相册中选取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialog.dismiss();
                selectFromALUM();
            }
        }).setCanceledOnTouchOutside(true);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.v("...", "onActivityResult" + (data == null) + requestCode);

        if (requestCode == 1) {
            startPhotoZoom(currentPhotoUri);
        }

        if (requestCode == 3 && data != null) {
            getImageToView(data);

        }
        if (requestCode == 2 && data != null) {
            final Uri uri = data.getData();
            startPhotoZoom(uri);

        }

        if (resultCode==TO_EDIT_USER_NAME){
            currentUser=BmobUser.getCurrentUser(UserDetailActivity.this,User.class);
            MenuItemBean userName = new MenuItemBean();
            userName.setImgResId(R.drawable.avatar);
            userName.setTitle(currentUser.getNickName());


            itemS.set(0,userName);

            hadChangeUserName=true;
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 根据时间名命图片
     *
     * @return 返回图片File对象
     */
    private File getOutputMediaFileUri() {
        // 保存图片的文件夹
        File file = new File(Environment.getExternalStorageDirectory()
                + "/uAmbition/Head");
        if (!file.exists()) {
            file.mkdirs();// 创建文件夹
        }

        // File file = new File(Environment.getExternalStorageDirectory(),
        // "workmap");
        // file.mkdirs();// 创建文件夹
        String fileName = file.toString();

        // Create a media file name
        String timeStamp = new DateFormat().format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + "";
        // currentPhoto = timeStamp;
        File imageFile = new File(fileName + File.separator + currentUser.getObjectId()+ ".png");
        return imageFile;
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    private void getImageToView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            final Drawable drawable = new BitmapDrawable(photo);
            // faceImage.setImageDrawable(drawable);

            // 保存图片的文件夹
            File file = new File(Environment.getExternalStorageDirectory()
                    + "/uAmbition/Head");
            if (!file.exists()) {
                file.mkdirs();// 创建文件夹
            }

            // File file = new File(Environment.getExternalStorageDirectory(),
            // "workmap");
            // file.mkdirs();// 创建文件夹

            String fileName = file.toString();
            File picFile = new File(fileName + File.separator
                    + currentUser.getObjectId()+ ".png");
            localPicPath=picFile.getPath();
            progressDialog.show();
            BTPFileResponse response = BmobProFile.getInstance(this).upload(picFile.getPath(), new UploadListener() {

                @Override
                public void onSuccess(String fileName,String url,BmobFile file) {
                    // TODO Auto-generated method stub

                    progressDialog.dismiss();
                    imgFileName=fileName;
                    imgFilePath=url;
                    ToastUtil.showMessage(UserDetailActivity.this,"上传成功");
                    handler.sendEmptyMessage(UPLOAD_SUCCESS);
                    user_image_url=file.getUrl();
                    isUserImageChange=true;
                    Log.i("bmob","文件上传成功："+fileName+",可访问的文件地址："+file.getUrl());
                }

                @Override
                public void onProgress(int progress) {
                    // TODO Auto-generated method stub
                    progressDialog.setMessage("正在上传头像..." + progress + "%");
                    //progressDialog.setProgress(progress);
                    Log.i("bmob","onProgress :"+progress);
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                    // TODO Auto-generated method stub
                    Log.i("bmob","文件上传失败："+errormsg);
                    progressDialog.dismiss();
                    isUserImageChange=false;
                    ToastUtil.showMessage(UserDetailActivity.this, "上传失败");

                }
            });








        }
    }


    private class UploadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){



                case UPLOAD_SUCCESS:

                   final String URL =BmobProFile.getInstance(UserDetailActivity.this).
                            signURL(imgFileName, imgFilePath,
                                    getResources().getString(R.string.access_key), 0, null);

                    userLogo.setImageBitmap(Util.getLoacalBitmap(localPicPath));
                    AmbitionAPP.getInstance().getImageLoader().loadBigPic(user_image_url, 100, new ImageLoader.BigImageCallback() {
                        @Override
                        public void imageLoaded(String url, final Bitmap bmp) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    userLogo.setImageBitmap(bmp);
                                }
                            });

                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(1000);
                                currentUser.setUser_img_url(user_image_url);
                                currentUser.update(UserDetailActivity.this);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    break;
            }
        }
    }


}
