package com.example.recipefy;

import static com.example.recipefy.NotificationUtils.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String itemId = intent.getStringExtra("item_id");
            String itemName = intent.getStringExtra("item_name");

            int notificationId = itemId.hashCode();

            Intent appIntent = new Intent(context, HomeActivity.class);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);

            Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Item Expiry Reminder")
                    .setContentText("Your item " + itemName + " is about to expire.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(appPendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId, notification);
        }
    }
}
