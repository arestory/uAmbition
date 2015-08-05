package uambition.ares.ywq.uambition.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.bean.Ambition;
import uambition.ares.ywq.uambition.bean.AmbitionDate;

/**
 * Created by ares on 15/7/23.
 */
public class AmbitionListAdapter extends BaseAdapter{

    private List<Ambition> list;
    private Context context;
    private String currentDate;//当前日期



    public AmbitionListAdapter(Context context, List<Ambition> list){
        this.list=list;
        this.context=context;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        Ambition habitBean=list.get(position);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    currentDate    =    simpleDateFormat.format(curDate);

        this.currentDate=currentDate;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.ambition_item,null);
            holder=new ViewHolder();
            holder.titleView=(TextView)convertView.findViewById(R.id.title);
            holder.dayView=(TextView)convertView.findViewById(R.id.deadline);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();


        }
        holder.titleView.setText(habitBean.getTitle());
        //判断当前日期与目标开始日期之间关系
        if(AmbitionDate.getBetweenDays(currentDate, habitBean.getBeginTime())>0){

            holder.dayView.setText(AmbitionDate.getBetweenDays(currentDate, habitBean.getBeginTime())+"天后开始执行");
        }else{
            //判断是否过期
            if(AmbitionDate.getBetweenDays(currentDate, habitBean.getEndTime())<0){
                holder.dayView.setText("目标已完成");
            }else
            if(AmbitionDate.getBetweenDays(currentDate, habitBean.getEndTime())==0){
                holder.dayView.setText("今天要完成目标");
            }else{
                habitBean.setBetweenDay(AmbitionDate.getBetweenDays(currentDate, habitBean.getEndTime()));
                holder.dayView.setText("距离目标还有" + habitBean.getBetweenDay()+"天");
            }


           ;

        }

        return convertView;
    }


    public  static class ViewHolder{

        TextView titleView;
        TextView dayView;

    }





}
