package com.eza.mariwatch.mariwatch;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by aalshaikh on 2/10/2018.
 */

public class NotificationUtils {
    private AppCompatActivity activity;

    public NotificationUtils(AppCompatActivity activity){
        this.activity = activity;
    }

    public void notifier() {
        Notification.Builder notificationBuilderInboxStyle = new Notification.Builder(activity);
        // NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        notificationBuilderInboxStyle.setSmallIcon(R.mipmap.red_alert);
        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_square);
        notificationBuilderInboxStyle.setLargeIcon(bm);
        notificationBuilderInboxStyle.setLights(Color.RED, 1, 1);
        notificationBuilderInboxStyle.setContentInfo("RED ALERT");
        notificationBuilderInboxStyle.setContentText("RESTRICTED AREA");
        notificationBuilderInboxStyle.setContentTitle("WARNING");
        // notificationBuilderInboxStyle.setColor(0xffff0000);
        // notificationBuilderInboxStyle.setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilderInboxStyle.build());
    }
    public void RedAlert(String fishType, String fishE){
        MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.danger);
        mediaPlayer.start();
        ((Vibrator)activity.getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
        Notification.Builder notificationBuilderInboxStyle = new Notification.Builder(activity);
        // NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        notificationBuilderInboxStyle.setSmallIcon(R.mipmap.red_alert);
        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.red_alert);
        notificationBuilderInboxStyle.setLargeIcon(bm);
        notificationBuilderInboxStyle.setLights(Color.RED, 1, 1);
        notificationBuilderInboxStyle.setContentInfo("RED ALERT");
        notificationBuilderInboxStyle.setContentText("RESTRICTED AREA "+fishType+" "+ fishE);
        notificationBuilderInboxStyle.setContentTitle("WARNING");
        // notificationBuilderInboxStyle.setColor(0xffff0000);
        // notificationBuilderInboxStyle.setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilderInboxStyle.build());

    }
    public void YellowAlert(String fishType, String fishE){
        ((Vibrator)activity.getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
        Notification.Builder notificationBuilderInboxStyle = new Notification.Builder(activity);
        // NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        notificationBuilderInboxStyle.setSmallIcon(R.mipmap.yellow_alert);
        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.yellow_alert);
        notificationBuilderInboxStyle.setLargeIcon(bm);
        notificationBuilderInboxStyle.setLights(Color.YELLOW, 1, 1);
        notificationBuilderInboxStyle.setContentInfo("YELLOW ALERT");
        notificationBuilderInboxStyle.setContentText("APPROACHING RESTRICTED AREA "+fishType+" "+ fishE);
        notificationBuilderInboxStyle.setContentTitle("WARNING");
        // notificationBuilderInboxStyle.setColor(0xffff0000);
        // notificationBuilderInboxStyle.setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilderInboxStyle.build());

    }
}
