package com.example.recipefy;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        Data input = getInputData();
        String itemName = input.getString("itemName");
        long expiryTime = input.getLong("expiryTime", 0);

        // Check if the expiry date is in the past
        if (expiryTime <= System.currentTimeMillis()) {
            return Result.success();
        }

        showNotification(context, itemName, expiryTime);
        return Result.success();
    }

    @SuppressLint("MissingPermission")
    private void showNotification(Context context, String itemName, long expiryTime) {
        // Create a notification channel (if required) for Android Oreo and above
        NotificationUtils.createNotificationChannel(context);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Food Expiry Reminder")
                .setContentText(itemName + " is about to expire!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) expiryTime, builder.build());
    }
}
