package tianchi.com.risksourcecontrol2.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import tianchi.com.risksourcecontrol2.R;
import tianchi.com.risksourcecontrol2.activitiy.notice.MyselfRectifyReplyListActivity;
import tianchi.com.risksourcecontrol2.activitiy.notice.UnreadMsgListActivity;

/**
 * Created by hairun.tian on 2018-06-27.
 */

public class MyNotification {

    public static void sendNotification(Context context,NotificationManager manager,int size) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,UnreadMsgListActivity.class), 0);
        Notification notification = new Notification.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.logo03)//设置小图标
                .setContentTitle("怀阳高速")
                .setContentText("您有新的未读消息，请查看")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
            manager.notify(0,notification);
    }


    public static void sendNotification2(Context context,NotificationManager manager,int size) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,MyselfRectifyReplyListActivity.class), 0);
        Notification notification = new Notification.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_logo)//设置小图标
                .setContentTitle("河惠莞")
                .setContentText("您有新的有新的回复通知未读，请查看")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1,notification);
    }


}
