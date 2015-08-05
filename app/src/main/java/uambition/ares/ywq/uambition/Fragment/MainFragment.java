package uambition.ares.ywq.uambition.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import uambition.ares.ywq.uambition.Activity.AddAmbitionActivity;
import uambition.ares.ywq.uambition.Activity.AmbitionActivity;
import uambition.ares.ywq.uambition.Activity.MainActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.Logs;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.adapter.AmbitionListAdapter;
import uambition.ares.ywq.uambition.bean.Ambition;
import uambition.ares.ywq.uambition.bean.AmbitionDate;

/**
 * Created by ares on 15/7/22.
 */
public class MainFragment  extends Fragment{


    private SwipeRefreshLayout refreshLayout;
    private ListView mainListView;
    private List<Ambition> habitList;
    private static final int UPDATE=0x001;
    private FloatingActionButton addHabitBtn;
    private MainActivity activity;
    private AmbitionListAdapter adapter;
    private ProgressBar loadBar;
    private LinearLayout progress_layout;
    private LinearLayout tips_layout;

    public static final int ADD_AMBITION=1000;
    public static final int EDIT_AMBITION=1001;


    public FloatingActionButton getAddHabitBtn(){
        return  addHabitBtn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main, container, false);
        activity=(MainActivity)getActivity();
        initMainList(view);
        Logs.print("MainFragment");
        return view;
    }


    //listView中第一项的索引
    private int mListViewFirstItem = 0;
    //listView中第一项的在屏幕中的位置
    private int mScreenY = 0;
    //是否向上滚动
    private boolean mIsScrollToUp = false;



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //menu.setHeaderTitle("人物简介");
        //添加菜单项
        menu.add(0, Menu.FIRST,0,"修改");
        menu.add(0, Menu.FIRST+1,0,"删除");

        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
       final AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if(item.getItemId()==Menu.FIRST){

            Intent intent=new Intent(activity, AmbitionActivity.class);
            intent.putExtra("ambition",habitList.get(info.position));
            intent.putExtra("MODE",0);//修改模式
            intent.putExtra("position",info.position);
            startActivityForResult(intent, EDIT_AMBITION);
        }else if(item.getItemId()==Menu.FIRST+1){

           final ProgressDialog dialog = new ProgressDialog(activity);
            dialog.setMessage("正在删除");
            dialog.show();
            Ambition ambition = habitList.get(info.position);
            ambition.delete(activity, ambition.getObjectId(), new DeleteListener() {
                @Override
                public void onSuccess() {
                    habitList.remove(info.position);
                    if(habitList.size()==0){
                        tips_layout.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();

                    dialog.dismiss();
                    ToastUtil.showMessage(activity,"删除成功");
                }

                @Override
                public void onFailure(int i, String s) {
                    dialog.dismiss();
                }
            });



        }

        //Toast.makeText(ListViewTestActivity.this, "content"+item.getItemId()+info.position, Toast.LENGTH_LONG).show();
        return super.onContextItemSelected(item);
    }


    private void initMainList(View view){
        loadBar=(ProgressBar)view.findViewById(R.id.loadBar);

        progress_layout=(LinearLayout)view.findViewById(R.id.progress_layout);
        tips_layout=(LinearLayout)view.findViewById(R.id.tips_layout);


        //initDatePicker();
        addHabitBtn=(FloatingActionButton)view.findViewById(R.id.addHabit);
        addHabitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddAmbitionActivity.class);
                startActivityForResult(intent, ADD_AMBITION);
            }
        });
        addHabitBtn.setRippleColor(activity.getThemeColor());

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addHabitBtn.setRippleColor(activity.getThemeColor());

        }else{
             // addHabitBtn.setBackgroundColor(activity.getThemeColor());
        }
            activity.setFloatingActionButton(addHabitBtn);
       // addHabitBtn.setBackgroundTintMode(PorterDuff.Mode.ADD);
      // addHabitBtn.setBackgroundColor(activity.getThemeColor());

        //addHabitBtn.setBackgroundColor(activity.getThemeColor());
        //addHabitBtn.setRippleColor(getResources().getColor(R.color.theme_color));
//        habitList=new ArrayList<Ambition>();
        mainListView=(ListView)view.findViewById(R.id.listview_habit);
//        adapter=new AmbitionListAdapter(getActivity(),habitList);
//
//        mainListView.setAdapter(adapter);
        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout_main);



        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(activity, AmbitionActivity.class);
                intent.putExtra("ambition", habitList.get(position));
                intent.putExtra("MODE", 1);//查看模式
                startActivity(intent);


            }
        });

            refreshLayout.setEnabled(false);
            refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

                                               {

                                                   @Override
                                                   public void onRefresh() {

                                                       refreshData();


                                                   }
                                               }

            );
       final BmobQuery<Ambition> query=new BmobQuery<Ambition>();
        query.addWhereEqualTo("author", activity.getUser());



        // query.addWhereEqualTo("create_user_id", activity.getUser().getObjectId());
        // 先从缓存获取数据，如果没有，再从网络获取。如果网络不可用
//        if(!Util.isNetworkConnected(activity)) {
         // query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
//
//        }else {
//           // progress_layout.setVisibility(View.GONE);
//        }
       //ToastUtil.showMessage(activity, "create_user_id=" + activity.getUser().getObjectId());
        query.setLimit(150);
        // 先从缓存取数据，无论结果如何都会再次从网络获取数据。也就是说会产生2次调用。
       // query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);

        query.include("author");
        //降序排序
        query.order("-createdAt");
        query.findObjects(activity, new FindListener<Ambition>() {
            @Override
            public void onSuccess(List<Ambition> list) {

                habitList = makeListAuto(list);
                adapter = new AmbitionListAdapter(activity, habitList);
                // adapter.notifyDataSetChanged();


                progress_layout.setVisibility(View.GONE);
                if(habitList.size()==0){
                    tips_layout.setVisibility(View.VISIBLE);
                    mainListView.setVisibility(View.GONE);
                }
                mainListView.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {
                progress_layout.setVisibility(View.GONE);
                tips_layout.setVisibility(View.GONE);

                query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);

                query.findObjects(activity, new FindListener<Ambition>() {
                    @Override
                    public void onSuccess(List<Ambition> list) {

                        habitList = makeListAuto(list);
                        adapter.notifyDataSetChanged();
                        adapter = new AmbitionListAdapter(activity, habitList);
                        mainListView.setAdapter(adapter);
                        progress_layout.setVisibility(View.GONE);
                        mainListView.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(int i, String s) {
                        progress_layout.setVisibility(View.GONE);
                        mainListView.setVisibility(View.VISIBLE);

                    }
                });
            }
        });
        registerForContextMenu(mainListView);

    }
    /**
     * 得到根Fragment
     *
     * @return
     */
    private Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        if(fragment==null){
            return null;
        }
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;

    }

    public void refreshData(){
       final ProgressDialog progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage("正在更新列表");
        progressDialog.show();;
        BmobQuery<Ambition> query=new BmobQuery<Ambition>();
        query.addWhereEqualTo("author", activity.getUser());
        //query.addWhereEqualTo("create_user_id", activity.getUser().getObjectId());
        query.setLimit(150);
        query.include("author");
        //降序排序
        query.order("-createdAt");
        // 先从缓存取数据，无论结果如何都会再次从网络获取数据。也就是说会产生2次调用。
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findObjects(activity, new FindListener<Ambition>() {
            @Override
            public void onSuccess(List<Ambition> list) {

                if(habitList.size()==list.size()){
                   // ToastUtil.showMessage(activity,"亲，没有更多数据了");
                }

                habitList.clear();
                habitList.addAll(makeListAuto(list));
               // ToastUtil.showMessage(activity,"list.size="+habitList.size());
                //adapter=new AmbitionListAdapter(activity,habitList);

                progressDialog.dismiss();
                adapter.notifyDataSetChanged();;
               // mainListView.setAdapter(adapter);
                //refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.dismiss();
                //ToastUtil.showMessage(activity,"更新失败，请检查网络");
                //refreshLayout.setRefreshing(false);
            }
        });




    }

    /**
     * 按照目标进度排序
     * @param ambitionList
     * @return
     */
    public List<Ambition> makeListAuto(List<Ambition> ambitionList){

        List<Ambition> ambitionAll = new ArrayList<Ambition>();
        List<Ambition> ambitionsDone = new ArrayList<Ambition>();
        List<Ambition> ambitionsDoing = new ArrayList<Ambition>();
        List<Ambition> ambitionsNotBegin = new ArrayList<Ambition>();
        List<Ambition> ambitionsToday = new ArrayList<Ambition>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    currentDate    =    simpleDateFormat.format(curDate);
        for (Ambition ambition:ambitionList){

            if(AmbitionDate.getBetweenDays(currentDate, ambition.getBeginTime())>0){
                //holder.dayView.setText("目标未开始");
                ambitionsNotBegin.add(ambition);
            }else{
                //判断是否过期
                if(AmbitionDate.getBetweenDays(currentDate, ambition.getEndTime())<0){
                   // holder.dayView.setText("目标已完成");
                    ambitionsDone.add(ambition);
                }else
                if(AmbitionDate.getBetweenDays(currentDate, ambition.getEndTime())==0){
                   // "今天要完成目标"
                    ambitionsToday.add(ambition);
                }else{
                    //进行中
                    ambitionsDoing.add(ambition);
                     }

            }




        }
        ambitionAll.addAll(ambitionsToday);

        List<Ambition> ambitionsDoingList = new ArrayList<Ambition>();
        Ambition temp=new Ambition();
        //按照时间差排序
         for(int i = 0;i<ambitionsDoing.size();i++){
             for(int j = i+1 ;j<ambitionsDoing.size();j++){
                 if(Ambition.getBetweenDays(ambitionsDoing.get(i))>Ambition.getBetweenDays(ambitionsDoing.get(j))){
                     temp=ambitionsDoing.get(j);
                     ambitionsDoing.set(j,ambitionsDoing.get(i));
                     ambitionsDoing.set(i,temp);
                     temp=ambitionsDoing.get(i);
                 }
             }

         }
        //ambitionAll.addAll(ambitionsDoing);

        ambitionAll.addAll(ambitionsNotBegin);
         ambitionsDoingList.addAll(ambitionsDoing);

        ambitionAll.addAll(ambitionsDoingList);
        ambitionAll.addAll(ambitionsDone);
        return ambitionAll;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (resultCode){

            case ADD_AMBITION :

                if(data!=null){

                    Ambition newAmbition = (Ambition)data.getSerializableExtra("ambition");
                    if(newAmbition!=null){
                        //Logs.printLogD("执行哈哈哈");
                        habitList.add(0, newAmbition);
                       // ToastUtil.showMessage(activity, "增加了！");
                        //habitList=makeListAuto(habitList);
                        //ambitions.addAll(habitList);
                        //habitList.clear();
                        //habitList=ambitions;
                        tips_layout.setVisibility(View.GONE);

                        mainListView.setVisibility(View.VISIBLE);
                        adapter=new AmbitionListAdapter(activity,habitList);
                        mainListView.setAdapter(adapter);
                        refreshData();
                    }

                }
                break;
            case EDIT_AMBITION :

                if(data!=null){

                    Ambition newAmbition = (Ambition)data.getSerializableExtra("ambition");
                    if(newAmbition!=null){

                       int position =  data.getIntExtra("position",0);

                        // habitList.remove(position);
                        habitList.set(position, newAmbition);
                        //ToastUtil.showMessage(activity, "增加了！");
                        //habitList=makeListAuto(habitList);
                        //ambitions.addAll(habitList);
                        //habitList.clear();
                        //habitList=ambitions;
                        tips_layout.setVisibility(View.GONE);

                        mainListView.setVisibility(View.VISIBLE);
                        adapter=new AmbitionListAdapter(activity,habitList);
                        mainListView.setAdapter(adapter);
                    }

                }




                break;
        }





    }



}
