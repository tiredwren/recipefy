package com.example.recipefy;

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
        if (intent != null) { // getting name and expiry date IF they are inputted
            String itemName = intent.getStringExtra("item_name");
            String expiryDate = intent.getStringExtra("expiry_date");

            int notificationId = itemName.hashCode(); // getting a unique id for each notification

            String notificationText = "Your item " + itemName + " will expire soon, on " + expiryDate;

            Intent appIntent = new Intent(context, HomeActivity.class);
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, 0);

            // setting all aspects of notification appearance
            Notification notification = new NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Item Expiry Reminder")
                    .setContentText(notificationText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(appPendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId, notification);
        }
    }
}
