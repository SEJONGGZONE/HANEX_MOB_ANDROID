package com.gzonesoft.sg623;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gzonesoft.sg623.R;

import java.net.URLDecoder;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FirebaseMessageService extends FirebaseMessagingService {

    String CHANNEL_ID = "CALLTRUCK_CHANNEL";
    CharSequence CHANNEL_NAME = "fcm_nt";
    String description = "push";
    int importance = NotificationManager.IMPORTANCE_NONE;
    MediaPlayer mediaPlayer;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) { //포그라운드
            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
//            up_Nt(remoteMessage.getData().get("orderCnt1"),null,null);
        }
        else if (remoteMessage.getData().size() > 0) { //백그라운드
            sendNotification(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"));
//            up_Nt(remoteMessage.getData().get("orderCnt1"),null,null);

        }

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//
//        NotificationCompat.Builder builder = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
//                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//                notificationManager.createNotificationChannel(channel);
//            }
//            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
//        }else {
//            builder = new NotificationCompat.Builder(getApplicationContext());
//        }
//
//        String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();
//
//        builder.setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(R.drawable.ic_launcher_background);
//
//        Notification notification = builder.build();
//        notificationManager.notify(1, notification);
    }

//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        if (remoteMessage.getNotification() != null) { //포그라운드
//            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
////            up_Nt(remoteMessage.getData().get("orderCnt1"),null,null);
//        }
//        else if (remoteMessage.getData().size() > 0) { //백그라운드
//            sendNotification(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"));
////            up_Nt(remoteMessage.getData().get("orderCnt1"),null,null);
//
//        }
//    }


    private void sendNotification(String messageBody, String messageTitle)  {
        try {

            //////////////////////////// 포그라운드 및 백그라운드 푸시알림 처리 ////////////////////////////
            Intent intent = new Intent(this, NotiSettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                    PendingIntent.FLAG_IMMUTABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
                mChannel.setShowBadge(false);
                mChannel.setDescription(description);
                mChannel.enableLights(true);
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                int notifyID = 2;

                String CHANNEL_ID = "CALLTRUCK_CHANNEL";

                try {
                    Notification notification = new Notification.Builder(FirebaseMessageService.this)
                            .setContentTitle(URLDecoder.decode(messageTitle, "UTF-8"))
                            .setContentText(URLDecoder.decode(messageBody, "UTF-8"))
                            .setSmallIcon(R.drawable.icons_truck)
                            .setChannelId(CHANNEL_ID)
                            .setNumber(0)
                            //.setContentIntent(pendingIntent)
                            .build();

                    //            mediaPlayer = MediaPlayer.create(this,R.raw.dispatch_calltruck);
                    //            mediaPlayer.start();

                    mNotificationManager.notify(notifyID, notification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //////////////////////////// 포그라운드 및 백그라운드 푸시알림 처리 ////////////////////////////
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}