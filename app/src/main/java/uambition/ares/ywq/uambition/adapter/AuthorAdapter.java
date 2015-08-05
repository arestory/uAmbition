package uambition.ares.ywq.uambition.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uambition.ares.ywq.uambition.Activity.BaseActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.bean.MenuItemBean;

/**
 * Created by ares on 15/7/18.
 */
public class AuthorAdapter extends BaseAdapter{

    private List<MenuItemBean> list;
    private  Context context;
    public AuthorAdapter(Context context, List<MenuItemBean> list){
        this.context=context;
        this.list=list;

    }

    @Override
    public int getCount() {
        return this.list.size();
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
        MenuItemBean itemBean = list.get(position);
        if (convertView==null){
           convertView= LayoutInflater.from(context).inflate(R.layout.author_item,null);
            holder=new ViewHolder();
            holder.titleView=(TextView)convertView.findViewById(R.id.title);
            holder.img=(ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
         holder=(ViewHolder)convertView.getTag();

        }
        if (position == selectItem) {
           if (itemBean.getTitle().equals("检查更新")){
//               convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
//               holder.titleView.setTextColor(context.getResources().getColor(R.color.black));

           }else{
               convertView.setBackgroundColor(((BaseActivity) context).getThemeColor());
               holder.titleView.setTextColor(context.getResources().getColor(R.color.white));

           }
        }
        else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
            holder.titleView.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.titleView.setText(itemBean.getTitle());
        holder.img.setBackground(context.getResources().getDrawable(itemBean.getImgResId()));
        return convertView;
    }

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=-1;

    public  static  class ViewHolder{
        ImageView img;
        TextView titleView;

    }
}
