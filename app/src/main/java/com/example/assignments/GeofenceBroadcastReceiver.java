package com.example.assignments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.Random;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context,"Geofence triggerred",Toast.LENGTH_LONG).show();
    }

    private void showNotification(Context context, String title, String content) {

        String channelId = "geofence_entry_alerts";
        CharSequence channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.setDescription("Notifications for geofence entry alerts");
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification_icon_logo)
                .setContentTitle("DANGEROUS AREA")
                .setContentText("You had entered dangerous area, please be careful !!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        //Set the notification to have the default settings for sounds, vibration and lights
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        Notification notification = builder.build();

        // Provide a unique ID for each notification
        notificationManager.notify(new Random().nextInt(), notification); // Provide a unique ID for each notification
    }




}



