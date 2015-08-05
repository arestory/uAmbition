package uambition.ares.ywq.uambition.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import uambition.ares.ywq.uambition.Activity.AmbitionActivity;
import uambition.ares.ywq.uambition.Activity.MainActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.Logs;
import uambition.ares.ywq.uambition.Util.ToastUtil;
import uambition.ares.ywq.uambition.adapter.AmbitionBBSAdapter;
import uambition.ares.ywq.uambition.bean.Ambition;
import uambition.ares.ywq.uambition.bean.Comment;

/**
 * Created by ares on 15/7/22.
 */
public class BbsFragment extends Fragment {

    private SwipeRefreshLayout refreshLayout;
    private ListView ambition_bbs_ListView;
    private List<Ambition> ambitionList=new ArrayList<Ambition>();

    private ProgressBar loadBar;
    private LinearLayout progress_layout;

    private MainActivity activity;
    private AmbitionBBSAdapter adapter;
    //查询数据
    private BmobQuery<Ambition> query;
    private View footer_view;

    private int lastItem;
    private BmobPushManager pushManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_bbs, container, false);
        activity=(MainActivity)getActivity();
        pushManager=activity.getPushManager();
        initView(view);
        return view;
    }

    public BbsFragment() {
        super();
         
    }

    @Override
    public void onDetach() {
        super.onDetach();
      Logs.printLogD("BBsFragment onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       Logs.printLogD("BBsFragment onDestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       Logs.printLogD("BBsFragment onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();

       Logs.printLogD("BBsFragment onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        
       Logs.printLogD("BBsFragment onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
       Logs.printLogD("BBsFragment onResume");
    }

    @Override
    public void onStart() {
       Logs.printLogD("BBsFragment onResume");
        super.onStart();
    }

    public void initView(View view){
        footer_view = getActivity().getLayoutInflater().inflate(R.layout.footer_listview,
                null);
        footer_view.setOnClickListener(null);

        refreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout_main);
        refreshLayout.setEnabled(true);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()

                                           {

                                               @Override
                                               public void onRefresh() {


                                                   refreshData();
                                               }
                                           }

        );
        ambition_bbs_ListView=(ListView)view.findViewById(R.id.listview_ambition_bbs);
        loadBar=(ProgressBar)view.findViewById(R.id.loadBar);
        progress_layout=(LinearLayout)view.findViewById(R.id.progress_layout);
        //progress_layout.setVisibility(View.GONE);
        ambition_bbs_ListView.addFooterView(footer_view);
        ambition_bbs_ListView.setClickable(false);

        BmobQuery  query=new BmobQuery<Ambition>();
        query.setLimit(10);

        // 先从缓存取数据，无论结果如何都会再次从网络获取数据。也就是说会产生2次调用。
        //query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);

        //根据该目标是否公开进行查询
       // query.addWhereEqualTo("personal","false");

        //query.addWhereContains("personal","false");
        query.include("author,commentList");
        query.order("-createdAt");

        query.addWhereEqualTo("personal", true);


        query.findObjects(activity, new FindListener<Ambition>() {
            @Override
            public void onSuccess(List<Ambition> list) {


                for (Ambition ambition:list){
                  BmobRelation relation =  ambition.getCommentList();

                }


                ambitionList.addAll(list);
                footer_view.setVisibility(View.GONE);
                //ambitionList = list;
                adapter = new AmbitionBBSAdapter(activity, ambitionList);
                ambition_bbs_ListView.setAdapter(adapter);
                progress_layout.setVisibility(View.GONE);

                if(ambitionList.size()==0){

                    ambition_bbs_ListView.setVisibility(View.GONE);
                }else{
                    if(ambitionList.size()>10){
                        ambition_bbs_ListView.addFooterView(footer_view);
                        ambition_bbs_ListView.setClickable(false);
                        footer_view.setVisibility(View.VISIBLE);



                    }
                    ambition_bbs_ListView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onError(int i, String s) {
                progress_layout.setVisibility(View.GONE);

            }
        });
        ambition_bbs_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(activity, AmbitionActivity.class);
                intent.putExtra("ambition", ambitionList.get(position));
                intent.putExtra("MODE", 1);//查看模式
                startActivity(intent);


            }
        });

        ambition_bbs_ListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (ambitionList.size() <= lastItem && scrollState == SCROLL_STATE_IDLE) {
                    footer_view.setVisibility(View.VISIBLE);

                    upToloadMoreData(ambitionList.size());
                    //getInfo(false, Integer.toString(++page));
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;

            }
        });

    }



    public void upToloadMoreData(int row){
      BmobQuery<Ambition> query=new BmobQuery<Ambition>();

        query.setLimit(10);
        //忽略前row条数据
        query.setSkip(row );

        query.addWhereEqualTo("personal", true);
        query.include("author");
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
        //降序排序
        query.order("-createdAt");
//        query.addWhereEqualTo("personal", true);
        // 先从缓存获取数据，如果没有，再从网络获取。
        query.findObjects(activity, new FindListener<Ambition>() {
            @Override
            public void onSuccess(List<Ambition> list) {

                if(ambitionList!=null){
                    if ( list.size()==0) {
                        ToastUtil.showMessage(activity, "亲，已经没有更多数据了");
                        refreshLayout.setRefreshing(false);
                        footer_view.setVisibility(View.GONE);
                        return;
                    }else{


                    }
                    refreshLayout.setRefreshing(false);
                    //ambitionList.clear();
                    ambitionList.addAll(list);
                    adapter.notifyDataSetChanged();
                }

                ;
                // mainListView.setAdapter(adapter);
                //refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {

                //ToastUtil.showMessage(activity,"更新失败，请检查网络");
                //refreshLayout.setRefreshing(false);
            }
        });



    }



    public void refreshData(){
       // footer_view.setVisibility(View.GONE);
        // 先从缓存获取数据，如果没有，再从网络获取。
        BmobQuery query=new BmobQuery<Ambition>();
        query.setLimit(10);

        // 先从缓存取数据，无论结果如何都会再次从网络获取数据。也就是说会产生2次调用。
       // query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);

        //根据该目标是否公开进行查询
        // query.addWhereEqualTo("personal","false");

        //query.addWhereContains("personal","false");
        query.include("author");
        query.order("-createdAt");
        query.addWhereEqualTo("personal", true);
         query.findObjects(activity, new FindListener<Ambition>() {
            @Override
            public void onSuccess(List<Ambition> list) {

                if(ambitionList!=null){

                    refreshLayout.setRefreshing(false);


                    ambitionList=list;
                    adapter=new AmbitionBBSAdapter(activity, ambitionList);
                    ambition_bbs_ListView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                    if(ambitionList.size()==0){
                        ambition_bbs_ListView.setVisibility(View.GONE);
                    }else{
                        if(ambitionList.size()>10){
                            ambition_bbs_ListView.addFooterView(footer_view);
                            ambition_bbs_ListView.setClickable(false);

                            footer_view.setVisibility(View.VISIBLE);
                        }

                        ambition_bbs_ListView.setVisibility(View.VISIBLE);

                    }
                }

                ;
                // mainListView.setAdapter(adapter);
                //refreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int i, String s) {

                //ToastUtil.showMessage(activity,"更新失败，请检查网络");
                //refreshLayout.setRefreshing(false);
            }
        });




    }


}
