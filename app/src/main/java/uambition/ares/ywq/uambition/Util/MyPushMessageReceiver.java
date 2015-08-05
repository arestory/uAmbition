package uambition.ares.ywq.uambition.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.bmob.push.PushConstants;
import cn.bmob.push.PushReceiver;
import uambition.ares.ywq.uambition.Activity.AmbitionActivity;
import uambition.ares.ywq.uambition.R;

/**
 * Created by ares on 15/8/4.
 */
public class MyPushMessageReceiver extends PushReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String message=intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            Log.d("bmob", "客户端收到推送内容：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));

            //通知栏显示收到的反馈信息
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = new Notification();
            notification.icon = R.drawable.icon;
            notification.tickerText = "收到反馈消息";
            notification.when = System.currentTimeMillis();
            Intent intent2 = new Intent(context, AmbitionActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
            notification.setLatestEventInfo(context, "收到一条评论", message, pi);
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(1, notification);



        }
    }
}
