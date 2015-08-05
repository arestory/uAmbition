package uambition.ares.ywq.uambition.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import uambition.ares.ywq.uambition.App.AmbitionAPP;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.Util.ImageLoader;
import uambition.ares.ywq.uambition.bean.Comment;
import uambition.ares.ywq.uambition.bean.User;
import uambition.ares.ywq.uambition.view.CircularImageView;

/**
 * Created by ares on 15/8/4.
 */
public class CommentAdapter  extends BaseAdapter{

    private List<Comment> commentList;
    private Context context;
    public CommentAdapter(Context context,List<Comment> commentList){
        this.commentList=commentList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder=null;
        Comment comment = commentList.get(position);
        User user=comment.getCommenter();
        if(convertView==null){

            convertView= LayoutInflater.from(context).inflate(R.layout.comment_item,null);
            holder=new ViewHolder();

            holder.imageView=(CircularImageView)convertView.findViewById(R.id.comment_p_img);
            holder.commenterNameTextView=(TextView)convertView.findViewById(R.id.comment_name);
            holder.commentTextView=(TextView)convertView.findViewById(R.id.comment_c);
            holder.commentTimeTextView=(TextView)convertView.findViewById(R.id.comment_date);

            convertView.setTag(holder);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.commenterNameTextView.setText(user.getNickName());
        holder.commentTextView.setText(comment.getContent());
        holder.commentTimeTextView.setText(comment.getCreatedAt());

        //加载图片
        loadCommenterImg(holder.imageView,user);

        //根据评论内容动态设置内容行数
        int lines=(int)holder.commentTextView.getText().toString().length()/16;
        if(lines==0){
            holder.commentTextView.setLines(2);
        }else{
            holder.commentTextView.setLines(lines+1);
        }

        return convertView;
    }


    public void loadCommenterImg(final CircularImageView imageView,User user){

        AmbitionAPP.getInstance().getImageLoader().loadBigPic(user.getUser_img_url(), 100, new ImageLoader.BigImageCallback() {
            @Override
            public void imageLoaded(String url, final Bitmap bmp) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bmp);
                    }
                });

            }
        });

    }


    public static class ViewHolder{
        CircularImageView imageView;
        TextView commenterNameTextView;
        TextView commentTextView;
        TextView commentTimeTextView;


    }


}
