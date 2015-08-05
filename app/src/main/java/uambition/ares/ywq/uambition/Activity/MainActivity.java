package uambition.ares.ywq.uambition.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;
import uambition.ares.ywq.uambition.App.AmbitionAPP;
import uambition.ares.ywq.uambition.Fragment.BbsFragment;
import uambition.ares.ywq.uambition.Fragment.FeedbackFragment;
import uambition.ares.ywq.uambition.Fragment.MoreFragment;
import uambition.ares.ywq.uambition.Fragment.PersonFragment;
import uambition.ares.ywq.uambition.Fragment.MainFragment;
import uambition.ares.ywq.uambition.Fragment.SettingFragment;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ImageLoader;
import uambition.ares.ywq.uambition.Util.ThemeColorUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.Util.Util;
import uambition.ares.ywq.uambition.adapter.MenuListAdapter;
import uambition.ares.ywq.uambition.bean.MenuItemBean;
import uambition.ares.ywq.uambition.bean.MyInstall;
import uambition.ares.ywq.uambition.bean.User;
import uambition.ares.ywq.uambition.view.CircularImageView;


public class MainActivity extends BaseActivity {
    private Toolbar toolbar;
    private ListView menuListView,mainListView;
    private SwipeRefreshLayout refreshLayout;
    private  DrawerLayout mDrawerLayout;
    private List<MenuItemBean> menuList=new ArrayList<MenuItemBean>();
    private FragmentTransaction fragmentTransaction;
    private MainFragment mainFragment;
    private BbsFragment bbsFragment;
    private PersonFragment personFragment;
    private SettingFragment settingFragment;
    private MoreFragment moreFragment;
    private FeedbackFragment feedbackFragment;
    private Fragment currentFM;//当前Fragment
    private User user;
    private CircularImageView avatar;
    private TextView userNameText;
    private LinearLayout menu_titleView;
    private static final int UPDATE_USER_LOGO=2000;

    private static final int TO_EDIT_USER_NAME=1002;
    private  MenuListAdapter adapter;
    private ProgressDialog checkUpdateDialog;
    private BmobPushManager bmobPushManager;


    public MoreFragment getMoreFragment(){

        return moreFragment;
    }

    public static Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        checkUpdateDialog=new ProgressDialog(this);
        checkUpdateDialog.setMessage("正在检查更新");
    }
private void setCurrentUser(User user){
    this.user=user;
}
    public  User getUser(){

        return this.user;
    }

public FragmentManager getManager(){

    return  getFragmentManager();
}

    @Override
    public void setContentView() {
        //设置当前用户
        setCurrentUser(BmobUser.getCurrentUser(this, User.class));
        BmobUpdateAgent.initAppVersion(this);
        setContentView(R.layout.activity_main);
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
        menu_titleView=(LinearLayout)findViewById(R.id.menu_title_view);
        menu_titleView.setBackgroundColor(getThemeColor());
        //initDatePicker();
        // datePickerDialog.show(getFragmentManager(),"");
        //BmobUpdateAgent.update(this);
        BmobPush.startWork(this, getResources().getString(R.string.bmob_sdk_key));

        // 创建推送消息的对象
        bmobPushManager = new BmobPushManager(this);

        //开启推送
        MyInstall is = new MyInstall(this);

        is.subscribe(this.user.getObjectId());
        //is.setReceiverId(this.user.getObjectId());
        //is.setInstallationId(this.user.getObjectId());
        //is.setDeviceToken(ththisis.user.getObjectId());
        is.save(this, new SaveListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });



//        BmobInstallation installation =
//                BmobInstallation.getCurrentInstallation(this);
//        installation.setDeviceToken(this.user.getObjectId());
//        installation.save();
//        this.user.setReceiver(installation);
//        this.user.save(this, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                ToastUtil.showMessage(MainActivity.this,"更新成功");
//
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                ToastUtil.showMessage(MainActivity.this,"更新fail"+s);
//
//            }
//        });



    }



    public BmobPushManager getPushManager(){

        return  bmobPushManager;
    }

    //获取菜单的adapter
    public MenuListAdapter getListAdapter(){

        return  adapter;
    }
    private int list_positon;
    //获取当前菜单的点击位置
    public int getCurrentPosition(){

        return list_positon;
    }
    @Override
    public void findViews() {

        setDefaultFragment();
       initToolBar();
        initMenuList();
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {

                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(){
        mainFragment=new MainFragment();
        personFragment =new PersonFragment();
        settingFragment = new SettingFragment();
        bbsFragment = new BbsFragment();
        moreFragment=new MoreFragment();
        feedbackFragment=new FeedbackFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_layout,mainFragment).show(mainFragment);
        fragmentTransaction.commit();
        currentFM=mainFragment;
//        getSupportFragmentManager().beginTransaction().

    }




     public  CircularImageView getAvatar(){
         return  avatar;
     }

   public Toolbar getTitleBar(){
      // RelativeLayout layout = (RelativeLayout)findViewById(R.id.title_bar);
       return  toolbar;

   }

    public  LinearLayout getMenuTitleView(){


        return menu_titleView;
    }

    private FloatingActionButton addHabitBtn;

    public void setFloatingActionButton(FloatingActionButton addHabitBtn){
        this.addHabitBtn=addHabitBtn;
    }
    public FloatingActionButton getFloatingActionButton(){
        return this.addHabitBtn;
    }

    private  void initMenuList(){

        MenuItemBean mainItem = new MenuItemBean();
        mainItem.setTitle("你的目标");
        mainItem.setImgResId(R.drawable.menu_habit_list);
        mainItem.setFragment(mainFragment);

        MenuItemBean updateItem = new MenuItemBean();
        updateItem.setTitle("检查更新");
        updateItem.setImgResId(R.drawable.update);
        updateItem.setFragment(personFragment);

        MenuItemBean bbsItem = new MenuItemBean();
        bbsItem.setTitle("人生就是不停的战斗社区");
        bbsItem.setImgResId(R.drawable.menu_habit_bbs);
        bbsItem.setFragment(bbsFragment);

        MenuItemBean setItem  = new MenuItemBean();
        setItem.setTitle("颜色设置");
        setItem.setImgResId(R.drawable.color_setting);
        setItem.setFragment(settingFragment);

        MenuItemBean exitItem = new MenuItemBean();
        exitItem.setImgResId(R.drawable.menu_habit_exit);
        exitItem.setFragment(null);
        exitItem.setTitle("退出");

        MenuItemBean feedItem = new MenuItemBean();
        feedItem.setImgResId(R.drawable.email);
        feedItem.setFragment(feedbackFragment);
        feedItem.setTitle("反馈");

        MenuItemBean moreItem = new MenuItemBean();
        moreItem.setTitle("关于uAmbition");
        moreItem.setImgResId(R.drawable.more);
        moreItem.setFragment(moreFragment);

        menuList.add(mainItem);
        menuList.add(bbsItem);
        menuList.add(setItem);
        menuList.add(feedItem);
        menuList.add(updateItem);
        menuList.add(moreItem);
        menuList.add(exitItem);

        userNameText=(TextView)findViewById(R.id.user_name);
        userNameText.setText(user.getNickName());
        menuListView=(ListView)findViewById(R.id.menu_list);
        avatar=(CircularImageView)findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UserDetailActivity.class);

                startActivityForResult(intent, UPDATE_USER_LOGO);
            }
        });
        AmbitionAPP.getInstance().getImageLoader().loadBigPic(user.getUser_img_url(), 100, new ImageLoader.BigImageCallback() {
            @Override
            public void imageLoaded(String url, final Bitmap bmp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        avatar.setImageBitmap(bmp);
                    }
                });

            }
        });
     adapter=new MenuListAdapter(this,menuList);
        adapter.setSelectItem(0);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItemBean itemBean = menuList.get(position);
                adapter.setSelectItem(position);
                list_positon=position;
                adapter.notifyDataSetInvalidated();

                if(itemBean.getFragment()==null){

                    BmobUser.logOut(MainActivity.this);
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    ThemeColorUtil.clearData(MainActivity.this);
                    finish();


                }else if(itemBean.getFragment().equals(personFragment)){
                    checkUpdateDialog.show();;
                    BmobUpdateAgent.forceUpdate(MainActivity.this);
                    BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
                        @Override
                        public void onUpdateReturned(int i, UpdateResponse updateResponse) {

                            if (i == UpdateStatus.No){
                                ToastUtil.showMessage(MainActivity.this, "已经是最新版本");

                               // mDrawerLayout.closeDrawers();;
                            }
                            checkUpdateDialog.dismiss();

                        }
                    });

                }
                else{
                    toolbar.setTitle(itemBean.getTitle());

                    //关闭菜单
                    mDrawerLayout.closeDrawers();
                    switchContent(currentFM,itemBean.getFragment());
//                    for (MenuItemBean item:menuList){
//                        if (!item.equals(itemBean)){
//
//                            getSupportFragmentManager().beginTransaction().hide(item.getFragment());
//                        }else{
//                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, itemBean.getFragment()).commit();
//
//                        }
//                    }
                }


               // fragmentTransaction.commit();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取当前的fragment
        Fragment fragment=currentFM;
        //触发对应的方法
        fragment.onActivityResult(requestCode,resultCode,data);
        if(resultCode==UPDATE_USER_LOGO){
            //ToastUtil.showMessage(MainActivity.this,"修改头像");
            if(data!=null){

                String url = data.getStringExtra("logo_url");
               // ToastUtil.showMessage(MainActivity.this,"修改头像"+url);
                avatar.setImageBitmap(Util.getLoacalBitmap(url));
                user=BmobUser.getCurrentUser(MainActivity.this,User.class);
                userNameText.setText(user.getNickName());
            }

        }

            user=BmobUser.getCurrentUser(MainActivity.this,User.class);
            userNameText.setText(user.getNickName());



    }

    /**
     * 切换Fragment
     * @param from 当前Fragment
     * @param to  跳转的Fragment
     */
    public void switchContent(Fragment from, Fragment to) {
        if (currentFM != to) {
            currentFM = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.fragment_layout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void initToolBar(){

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getThemeColor());
        toolbar.setTitle("你的目标");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(toolbar);
///* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */

/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理 */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", 0).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", 0).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        //设置侧滑
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open,
                R.string.close);
       // mDrawerLayout.setScrimColor(getResources().getColor(R.color.white));
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu, menu);//隐藏菜单键

        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
