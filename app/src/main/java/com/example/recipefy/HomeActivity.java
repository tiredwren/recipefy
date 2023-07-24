package com.example.recipefy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView cardRecyclerView;
    private CardAdapter cardAdapter;
    private List<CardItem> cardItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardRecyclerView = findViewById(R.id.cardRecyclerView);
        cardItemList = new ArrayList<>();

        // For testing, add some sample items to the list
        Calendar calendar = Calendar.getInstance();
        cardItemList.add(new CardItem("Item 1", calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        cardItemList.add(new CardItem("Item 2", calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        cardItemList.add(new CardItem("Item 3", calendar.getTime()));

        cardAdapter = new CardAdapter(cardItemList, this);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardRecyclerView.setAdapter(cardAdapter);

        // Button to add a new item
        findViewById(R.id.btnAddItem).setOnClickListener(view -> showAddItemDialog());
    }

    private void showAddItemDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String itemName = "New Item"; // Default item name, you can customize this
            addCardItem(itemName, calendar.getTime());
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addCardItem(String itemName, Date expiryDate) {
        CardItem cardItem = new CardItem(itemName, expiryDate);
        cardItemList.add(cardItem);
        cardAdapter.notifyDataSetChanged();
        scheduleNotification(cardItem);
    }

    private void scheduleNotification(CardItem cardItem) {
        Calendar expiryCalendar = Calendar.getInstance();
        expiryCalendar.setTime(cardItem.getExpiryDate());
        long expiryTimeInMillis = expiryCalendar.getTimeInMillis();

        // Calculate the notification time
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        long tenDaysInMillis = 10 * 24 * 60 * 60 * 1000L;
        long fiveDaysInMillis = 5 * 24 * 60 * 60 * 1000L;

        long tenDaysNotificationTime = expiryTimeInMillis - tenDaysInMillis;
        long fiveDaysNotificationTime = expiryTimeInMillis - fiveDaysInMillis;

        // Check if the notification time is in the future
        if (tenDaysNotificationTime > currentTimeInMillis) {
            scheduleNotificationWorker(cardItem, tenDaysNotificationTime);
        }

        if (fiveDaysNotificationTime > currentTimeInMillis) {
            scheduleNotificationWorker(cardItem, fiveDaysNotificationTime);
        }
    }

    private void scheduleNotificationWorker(CardItem cardItem, long notificationTime) {
        Data inputData = new Data.Builder()
                .putString("itemName", cardItem.getItemName())
                .putLong("expiryTime", cardItem.getExpiryDate().getTime())
                .build();

        OneTimeWorkRequest notificationWorkRequest =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .setInitialDelay(notificationTime - Calendar.getInstance().getTimeInMillis(), TimeUnit.MILLISECONDS)
                        .setInputData(inputData)
                        .build();

        WorkManager.getInstance(this).enqueue(notificationWorkRequest);
    }
}

// get values from selected cards:
// button onclick listener
// List<CardItem> selectedCards = cardAdapter.getSelectedCards();
