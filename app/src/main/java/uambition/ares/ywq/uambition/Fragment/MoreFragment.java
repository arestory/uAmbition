package uambition.ares.ywq.uambition.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.DeleteListener;
import cn.waps.AppConnect;
import uambition.ares.ywq.uambition.Activity.AmbitionActivity;
import uambition.ares.ywq.uambition.Activity.MainActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.Logs;
import uambition.ares.ywq.uambition.Util.ThemeColorUtil;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.adapter.AuthorAdapter;
import uambition.ares.ywq.uambition.adapter.MenuListAdapter;
import uambition.ares.ywq.uambition.bean.Ambition;
import uambition.ares.ywq.uambition.bean.MenuItemBean;

/**
 * Created by ares on 15/7/31.
 */
public class MoreFragment extends Fragment {


    private ListView listView;
    private AuthorAdapter adapter;
    public View rootView;
    private String openOrCloseADV;
    private List<MenuItemBean> menuItemBeans;
    private MainActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more, container, false);

        activity= (MainActivity)getActivity();
        rootView=view;
        view.setBackgroundColor(activity.getThemeColor());

        openOrCloseADV=AppConnect.getInstance(activity).getConfig("openADV", "open");
        initView(view);
        String spOpenOrClose =ThemeColorUtil.getData(getActivity(),"ambition","isOpenADV");

//        if(openOrCloseADV.equals("open")){
//            AppConnect.getInstance(activity).initPopAd(activity);
//            AppConnect.getInstance(activity).showPopAd(activity);
//
//        }


        return view;
    }


    public View getRootView(){

        return rootView;
    }




    private void initView(View view ){
        listView=(ListView)view.findViewById(R.id.more_listview);
        menuItemBeans=new ArrayList<MenuItemBean>();
        MenuItemBean userName = new MenuItemBean();
        userName.setImgResId(R.drawable.avatar);
        userName.setTitle("作者：AresYWQ");

        MenuItemBean phoneItem = new MenuItemBean();
        phoneItem.setImgResId(R.drawable.color_setting);
        phoneItem.setTitle("橙色控,所以默认为橙色");

        MenuItemBean emailItem = new MenuItemBean();
        emailItem.setImgResId(R.drawable.thumb_black);
        emailItem.setTitle("觉得不错，随意打赏");

        MenuItemBean pswItem = new MenuItemBean();
        pswItem.setImgResId(R.drawable.send);
        pswItem.setTitle("支付宝账号:277825722@qq.com");


        MenuItemBean appItem = new MenuItemBean();
        appItem.setImgResId(R.drawable.app);
        appItem.setTitle("推荐应用");


        MenuItemBean removeAVDItem = new MenuItemBean();
        removeAVDItem.setImgResId(R.drawable.app);
        removeAVDItem.setTitle("长按去掉广告");


        menuItemBeans.add(userName);
        menuItemBeans.add(phoneItem);
        menuItemBeans.add(emailItem);
        menuItemBeans.add(pswItem);
        /*先从本地sharepreference判断用户是否进行过删除广告操作
          再从网络判断是否显示广告
         */

        String spOpenOrClose =ThemeColorUtil.getData(getActivity(),"ambition","isOpenADV");
        if(spOpenOrClose!=null){
            if(!TextUtils.isEmpty(spOpenOrClose)){
                Logs.d("YWQ","!TextUtils.isEmpty(spOpenOrClose)");
                Logs.d("YWQ","spOpenOrClose="+spOpenOrClose);

                 if(!spOpenOrClose.equals("close")){

                     Logs.d("YWQ","spOpenOrClose=close");
                     if(openOrCloseADV.equals("open")){
                         menuItemBeans.add(appItem);
                         menuItemBeans.add(removeAVDItem);

                         Logs.d("YWQ", "openOrCloseADV="+openOrCloseADV);
                         openADV();
                     }else {
                         Logs.d("YWQ", "openOrCloseADV="+openOrCloseADV);
                     }
                }
            }else{
                Logs.d("YWQ", "TextUtils.isEmpty(spOpenOrClose)");
                if(openOrCloseADV.equals("open")){
                    menuItemBeans.add(appItem);
                    menuItemBeans.add(removeAVDItem);
                    openADV();
                    Logs.d("YWQ", "TextUtils.isEmpty(spOpenOrClose) AND openOrCloseADV="+openOrCloseADV);

                }else{
                    Logs.d("YWQ", "TextUtils.isEmpty(spOpenOrClose) AND openOrCloseADV="+openOrCloseADV);

                }
            }
        }else{
            Logs.d("YWQ", "spOpenOrClose=null");

            if(openOrCloseADV.equals("open")){
                menuItemBeans.add(appItem);
                menuItemBeans.add(removeAVDItem);
                openADV();
                Logs.d("YWQ", "spOpenOrClose=null and openOrCloseADV="+openOrCloseADV);
            }else{
                Logs.d("YWQ", "spOpenOrClose=null and openOrCloseADV="+openOrCloseADV);

            }


        }



        adapter = new AuthorAdapter(getActivity(),menuItemBeans);
        listView.setAdapter(adapter);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==4){
                    AppConnect.getInstance(getActivity()).showOffers(getActivity());
                }
            }
        });

    }
    //打开广告
    public void openADV(){
        registerForContextMenu(listView);
        AppConnect.getInstance(activity).initPopAd(activity);
        AppConnect.getInstance(activity).showPopAd(activity);


    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, Menu.FIRST,0,"去掉广告");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if(item.getItemId()==Menu.FIRST){

            menuItemBeans.remove(5);
//            menuItemBeans.remove(4);
            unregisterForContextMenu(listView);
          //  registerForContextMenu(null);
//
            adapter.notifyDataSetChanged();
            ToastUtil.showMessage(getActivity(),"没广告了，开心啦");
            ThemeColorUtil.saveColor(getActivity(),"ambition","isOpenADV","close");
        }
        //Toast.makeText(ListViewTestActivity.this, "content"+item.getItemId()+info.position, Toast.LENGTH_LONG).show();
        return super.onContextItemSelected(item);
    }



}
