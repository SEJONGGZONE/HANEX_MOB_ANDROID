package com.gzonesoft.sg623;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.StringDef;

import com.gzonesoft.sg623.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 노티피케이션 매니저
 */
public class NotiManager {

    private static final String GROUP_TRUCKER = "GROUP_TRUCKER";

    public static void createChannel(Context context) {

        NotificationChannelGroup group1 = new NotificationChannelGroup(GROUP_TRUCKER, GROUP_TRUCKER);
        getManager(context).createNotificationChannelGroup(group1);


        NotificationChannel channelMessage = new NotificationChannel(Channel.MESSAGE,
                context.getString(R.string.notification_channel_message_title), NotificationManager.IMPORTANCE_DEFAULT);
        channelMessage.setDescription(context.getString(R.string.notification_channel_message_description));
        channelMessage.setGroup(GROUP_TRUCKER);
        channelMessage.setLightColor(Color.GREEN);
        channelMessage.setShowBadge(false);
        channelMessage.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(channelMessage);

        NotificationChannel channelComment = new NotificationChannel(Channel.COMMENT,
                context.getString(R.string.notification_channel_comment_title), NotificationManager.IMPORTANCE_DEFAULT);
        channelComment.setDescription(context.getString(R.string.notification_channel_comment_description));
        channelComment.setGroup(GROUP_TRUCKER);
        channelComment.setLightColor(Color.BLUE);
        channelComment.setShowBadge(false);
        channelComment.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager(context).createNotificationChannel(channelComment);

// 노티 알림음 설정(포그라운드에서 작동)
//        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.recv_dispatch);
//        AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_ALARM)
//                .build();

        NotificationChannel channelNotice = new NotificationChannel(Channel.NOTICE,
                context.getString(R.string.notification_channel_notice_title), NotificationManager.IMPORTANCE_LOW); // 알림음을 안나게한다..
        channelNotice.setDescription(context.getString(R.string.notification_channel_notice_description));
        channelNotice.setGroup(GROUP_TRUCKER);
        channelNotice.setLightColor(Color.RED);
        channelNotice.setShowBadge(false);
//        channelNotice.setSound(soundUri, audioAttributes);
        channelNotice.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager(context).createNotificationChannel(channelNotice);


    }

    private static NotificationManager getManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void deleteChannel(Context context, @Channel String channel) {
        getManager(context).deleteNotificationChannel(channel);

    }

    public static void sendNotification(Context context, int id, @Channel String channel, String title, String body) {

        //playDispatch(context, R.raw.recv_dispatch);



        Notification.Builder builder = new Notification.Builder(context, channel)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.recv_dispatch);
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .build();

            NotificationChannel channelNotice = new NotificationChannel(Channel.NOTICE,
                    context.getString(R.string.notification_channel_notice_title), NotificationManager.IMPORTANCE_HIGH);
            channelNotice.setDescription(context.getString(R.string.notification_channel_notice_description));
            channelNotice.setGroup(GROUP_TRUCKER);
            channelNotice.setLightColor(Color.RED);
            channelNotice.setShowBadge(false);
//            channelNotice.setSound(soundUri, audioAttributes);
            channelNotice.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager(context).createNotificationChannel(channelNotice);
        }
        getManager(context).notify(id, builder.build());
    }

    /**
     * 지정리소스 재생
     * @param resId
     */
    public static void playDispatch(Context context, int resId) {
        MediaPlayer ring= MediaPlayer.create(context, resId);
        ring.start();
    }

    private static int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Channel.MESSAGE,
            Channel.COMMENT,
            Channel.NOTICE
    })
    public @interface Channel {
        String MESSAGE = "message";
        String COMMENT = "comment";
        String NOTICE = "notice";
    }

}
