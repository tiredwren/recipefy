package com.example.recipefy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ExpiryNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Show the notification when the broadcast is received
        Toast.makeText(context, "Food item is about to expire!", Toast.LENGTH_SHORT).show();
    }
}
