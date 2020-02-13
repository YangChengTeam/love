package com.music.player.lib.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.music.player.lib.R;
import com.music.player.lib.bean.MusicInfo;

import androidx.core.app.NotificationCompat;


/**
 * Created by wanglin  on 2018/9/14 15:13.
 * 创建通知
 */
public class NotificationUtil {

    public static void showNotify(Context context, MusicInfo musicInfo, boolean isPlay, int flags) {
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
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_music_small_player_controller);

        if (isPlay) {
            remoteViews.setImageViewResource(R.id.iv_play_pause, R.drawable.ic_player_pause);
        } else {
            remoteViews.setImageViewResource(R.id.iv_play_pause, R.drawable.ic_player_play);
        }
        remoteViews.setTextViewText(R.id.tv_music_title, musicInfo.getTitle());

        remoteViews.setImageViewBitmap(R.id.iv_covor_thumb, musicInfo.getBitmap());


        //设置播放或暂停意图
        Intent play = new Intent();

        play.setAction(Constant.NOTIFY_PLAY_PAUSE);

        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, play, PendingIntent.FLAG_UPDATE_CURRENT);


        //设置取消意图
        Intent cancel = new Intent();
        cancel.setAction(Constant.NOTIFY_CANCEL);

        PendingIntent closePendingIntent = PendingIntent.getBroadcast(context, 5, cancel, PendingIntent.FLAG_UPDATE_CURRENT);


        remoteViews.setOnClickPendingIntent(R.id.iv_play_pause, playPendingIntent);


        remoteViews.setOnClickPendingIntent(R.id.iv_player_close, closePendingIntent);

        Intent intent = new Intent("com.music.player.love.action");


        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, 6, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setCustomBigContentView(remoteViews)
                .setCustomContentView(remoteViews)
                .setContentIntent(notificationPendingIntent);
        Notification notification = builder.build();
        notification.tickerText = "播放通知";
        notification.icon = R.drawable.ic_launcher;
        notification.flags |= flags;


        nm.notify(100, notification);

    }

    public static void clear(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(100);
    }


}
