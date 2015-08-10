package uambition.ares.ywq.uambition.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.push.PushConstants;
import cn.bmob.push.PushReceiver;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import uambition.ares.ywq.uambition.Activity.AmbitionActivity;
import uambition.ares.ywq.uambition.Activity.AmbitionDetailActivity;
import uambition.ares.ywq.uambition.Activity.PhoneActivity;
import uambition.ares.ywq.uambition.R;
import uambition.ares.ywq.uambition.bean.Ambition;

/**
 * Created by ares on 15/8/4.
 */
public class MyPushMessageReceiver extends PushReceiver {


    private static final int JUMP_TO_AMBITION=0X001;

    @Override
    public void onReceive(final Context context, Intent intent) {

        MessageHandler handler = new MessageHandler(context);


        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String message=intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            //通知栏显示收到的反馈信息
           final  NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           final Notification notification = new Notification();
            notification.icon = R.drawable.email;
            notification.tickerText = "收到uAmbition的消息";
            notification.when = System.currentTimeMillis();

           final Intent notificationIntent = new Intent();


           JSONObject jsonObject =null;

            try {

                jsonObject=new JSONObject(message);

                final String type = jsonObject.getString("type");
                if(type.equals("feedback")){
                   // notificationIntent.setClass(context, AmbitionDetailActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            |Intent.FLAG_ACTIVITY_NEW_TASK);
                    final String feedbacker = jsonObject.getString("feedbacker");
                    final String detail = jsonObject.getString("feedback");

                    final  PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);


                        notification.setLatestEventInfo(context, feedbacker+"发来一条反馈", detail, pi);




                }else{

                    notificationIntent.setClass(context, AmbitionDetailActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            |Intent.FLAG_ACTIVITY_NEW_TASK);


                    final  String commenter= jsonObject.getString("commenter");
                    final String ambition_title = jsonObject.getString("ambition");
                    final  String detail = jsonObject.getString("detail");

                    final String ambitionID=jsonObject.getString("ambitionID");

                    notificationIntent.putExtra("ambitionID", ThemeColorUtil.getData(context,"ambition","ambitionID"));
                    notificationIntent.putExtra("ambitionTitle",ambitionID);
                    notificationIntent.putExtra("MODE", "1");
                    final  PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                    if(type.equals("talkAbout")){
                        notification.setLatestEventInfo(context, commenter+ "@了你", detail, pi);

                    }else if(type.equals("comment")){
                        notification.setLatestEventInfo(context, commenter+ "评论了你", detail, pi);

                    }
                }

                notification.defaults |= Notification.DEFAULT_SOUND;
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                nm.notify(1, notification);


            } catch (JSONException e) {
                e.printStackTrace();

            }




        }
    }


    public PendingIntent getDefalutIntent(Context context,int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }



    private class MessageHandler extends Handler{

        private Context context;
        public MessageHandler(Context context){
            this.context=context;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){


                case JUMP_TO_AMBITION:


                    break;
            }


        }
    }
}
