package uambition.ares.ywq.uambition.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.GetListener;
import uambition.ares.ywq.uambition.App.AmbitionAPP;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ImageLoader;
import uambition.ares.ywq.uambition.bean.Ambition;
import uambition.ares.ywq.uambition.bean.AmbitionDate;
import uambition.ares.ywq.uambition.bean.User;
import uambition.ares.ywq.uambition.view.CircularImageView;
import uambition.ares.ywq.uambition.view.UserDialog;

/**
 * Created by ares on 15/7/27.
 */
public class AmbitionBBSAdapter extends BaseAdapter {


    private List<Ambition> ambitionBBS;
    private Context context;
    public  AmbitionBBSAdapter( Context context,List<Ambition> ambitionBBS){


        this.context=context;
        this.ambitionBBS=ambitionBBS;
    }

    @Override
    public int getCount() {
        return ambitionBBS.size();
    }

    @Override
    public Object getItem(int position) {
        return ambitionBBS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Ambition ambition = ambitionBBS.get(position);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    currentDate    =    simpleDateFormat.format(curDate);

        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.bbs_ambition_item,null);
            holder=new ViewHolder();
            holder.user_img=(CircularImageView)convertView.findViewById(R.id.user_img);
            holder.user_name=(TextView)convertView.findViewById(R.id.user_name);
            holder.content=(TextView)convertView.findViewById(R.id.ambition_content);
            holder.deadLine=(TextView)convertView.findViewById(R.id.deadline);
            holder.favorite=(ImageView)convertView.findViewById(R.id.favorite);
            holder.favoriteNum=(TextView)convertView.findViewById(R.id.favorite_num);
            holder.stateImg=(ImageView)convertView.findViewById(R.id.state_img);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        AmbitionAPP.getInstance().getImageLoader().loadBigPic(ambition.getAuthor().getUser_img_url(), 100, new ImageLoader.BigImageCallback() {
            @Override
            public void imageLoaded(String url, final Bitmap bmp) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        holder.user_img.setImageBitmap(bmp);
                    }
                });
            }
        });
        holder.content.setText(ambition.getTitle());
        holder.user_name.setText(ambition.getAuthor().getNickName());
//判断当前日期与目标开始日期之间关系
        if(AmbitionDate.getBetweenDays(currentDate, ambition.getBeginTime())>0){

            holder.stateImg.setBackground(context.getResources().getDrawable(R.drawable.more));
            holder.deadLine.setText(AmbitionDate.getBetweenDays(currentDate, ambition.getBeginTime())+"天后开始执行");
        }else{
            //判断是否过期
            if(AmbitionDate.getBetweenDays(currentDate, ambition.getEndTime())<0){
                holder.deadLine.setText("目标已完成");
                holder.stateImg.setBackground(context.getResources().getDrawable(R.drawable.finish));
            }else
            if(AmbitionDate.getBetweenDays(currentDate, ambition.getEndTime())==0){
                holder.deadLine.setText("今天要完成目标");
                holder.stateImg.setBackground(context.getResources().getDrawable(R.drawable.clock_end));
            }else{
                ambition.setBetweenDay(AmbitionDate.getBetweenDays(currentDate, ambition.getEndTime()));
                holder.deadLine.setText("距离目标还有" + ambition.getBetweenDay() + "天");
                holder.stateImg.setBackground(context.getResources().getDrawable(R.drawable.clock_begin));
            }


            ;

        }
        if(ambition.getFavorite_num()==null){
            ambition.setFavorite_num(0);
            holder.favoriteNum.setVisibility(View.GONE);
        }else if(ambition.getFavorite_num()==0){

            holder.favoriteNum.setVisibility(View.GONE);
        } else{

            holder.favoriteNum.setVisibility(View.VISIBLE);
        }
            holder.favoriteNum.setText(ambition.getFavorite_num()+"人");


        holder.user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDialog.Builder builder = new UserDialog.Builder(context,ambition.getAuthor());
                builder.setMessage(ambition.getAuthor().getNickName());
               // builder.create().show();
            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BmobQuery<Ambition> query = new BmobQuery<Ambition>();
                query.include("author");

                        query.getObject(context, ambition.getObjectId(), new GetListener<Ambition>() {
                            @Override
                            public void onSuccess(Ambition ambition) {
                                if (ambition.getFavorite_num() == null) {
                                    ambition.setFavorite_num(0);
                                }
                                BmobRelation relation = new BmobRelation();
                                relation.add(BmobUser.getCurrentUser(context, User.class));
                                ambition.setLiker(relation);
                                ambition.setFavorite_num(ambition.getFavorite_num() + 1);
                                ambition.setAuthor(ambition.getAuthor());
                                ambitionBBS.set(position, ambition);
                                notifyDataSetChanged();
                                ambition.update(context);
                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });





            }
        });
        return convertView;
    }


    public  static class ViewHolder{
        CircularImageView user_img;
        TextView user_name;
        TextView content;
        TextView deadLine;
        ImageView stateImg;
        ImageView favorite;
        TextView favoriteNum;



    }


}
