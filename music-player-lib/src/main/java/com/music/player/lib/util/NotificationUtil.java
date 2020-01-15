package com.yc.english.speak.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.yc.english.R;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.speak.view.activity.ListenEnglishActivity;


/**
 * Created by wanglin  on 2018/9/14 15:13.
 * 创建通知
 */
public class NotificationUtil {

    public static void showNotify(Context context, String title, boolean isPlay, int flags) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //分组（可选）
            //groupId要唯一
//            String groupId = "group_001";
//            NotificationChannelGroup group = new NotificationChannelGroup(groupId, "音乐");
//
//            //创建group
//            nm.createNotificationChannelGroup(group);

            //channelId要唯一
            String channelId = "channel_001";

            NotificationChannel adChannel = new NotificationChannel(channelId,
                    "歌曲欣赏", NotificationManager.IMPORTANCE_DEFAULT);
            //补充channel的含义（可选）
//            adChannel.setDescription("推广信息");
            //将渠道添加进组（先创建组才能添加）
//            adChannel.setGroup(groupId);
            //创建channel
            nm.createNotificationChannel(adChannel);

            //创建通知时，标记你的渠道id

            builder = new NotificationCompat.Builder(context, channelId);

        } else {
            builder = new NotificationCompat.Builder(context);

        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notifacation_player_view);

        if (isPlay) {
            remoteViews.setImageViewResource(R.id.notify_play, R.drawable.btn_pause_selector);
        } else {
            remoteViews.setImageViewResource(R.id.notify_play, R.drawable.btn_play_selector);
        }
        remoteViews.setTextViewText(R.id.notify_title, title);

        //设置播放或暂停意图
        Intent play = new Intent();

        play.setAction(Constant.NOTIFY_PLAY_PAUSE);

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);

        //设置上一首意图
        Intent pre = new Intent();
        pre.setAction(Constant.NOTIFY_PRE);
        PendingIntent prePendingIntent = PendingIntent.getBroadcast(context, 1, pre, PendingIntent.FLAG_UPDATE_CURRENT);

        //设置下一首意图
        Intent next = new Intent();
        next.setAction(Constant.NOTIFY_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 2, next, PendingIntent.FLAG_UPDATE_CURRENT);

        //设置取消意图
        Intent cancel = new Intent();
        cancel.setAction(Constant.NOTIFY_CANCEL);

        PendingIntent closePendingIntent = PendingIntent.getBroadcast(context, 5, cancel, PendingIntent.FLAG_UPDATE_CURRENT);


        remoteViews.setOnClickPendingIntent(R.id.notify_play, playPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.notify_prev, prePendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.notify_next, nextPendingIntent);

        remoteViews.setOnClickPendingIntent(R.id.notify_close, closePendingIntent);

        Intent intent = new Intent(context, ListenEnglishActivity.class);


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 6, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setCustomBigContentView(remoteViews)
                .setCustomContentView(remoteViews)
                .setContentIntent(notificationPendingIntent);
        Notification notification = builder.build();
        notification.tickerText = "播放通知";
        notification.icon = R.mipmap.lanucher;
        notification.flags |= flags;


        nm.notify(100, notification);

    }

    public static void clear(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(100);
    }


}
