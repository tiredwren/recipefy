package com.example.recipefy;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements CardAdapter.OnDeleteClickListener,
        CardAdapter.OnItemSelectListener {

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private ArrayList<CardItem> cardList;
    private ArrayList<String> selectedItemsList = new ArrayList<>();
    private TextView textView;
    private static final int NOTIFICATION_ID = 100;
    private static final String CHANNEL_ID = "expiry_channel";

    // Schedule notifications for items that are about to expire
    private void scheduleNotificationsForExpiringItems() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        for (CardItem cardItem : cardList) {
            Date expiryDate = cardItem.getExpiryDateAsDate();
            if (expiryDate != null) {
                // Calculate the date 5 days before expiry
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(expiryDate);
                calendar.add(Calendar.DAY_OF_MONTH, -5);
                Date notificationDate = calendar.getTime();

                // Check if the notification date is in the future and within the 10-day range
                Date currentDate = new Date();
                if (notificationDate.after(currentDate)) {
                    // Schedule the notification
                    scheduleNotification(notificationManager, cardItem.getItemId(), notificationDate);
                }
            }
        }
    }

    private void scheduleNotification(NotificationManagerCompat notificationManager, String itemId, Date notificationDate) {
        // Create a notification intent to be triggered when the notification fires
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra("item_id", itemId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Calculate the time delay until the notification fires
        long delayInMillis = notificationDate.getTime() - System.currentTimeMillis();

        // Schedule the notification using AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayInMillis, pendingIntent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.cardRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList, selectedItemsList, this, this);
        recyclerView.setAdapter(cardAdapter);

        textView = findViewById(R.id.logoutTextView);
        ImageButton addButton = findViewById(R.id.btnAddItem);
        addButton.setOnClickListener(v -> showAddItemDialog());

        Button selectedItemsButton = findViewById(R.id.button);
        selectedItemsButton.setOnClickListener(v -> {
            String selectedItems = "Selected items: ";
            for (String item : selectedItemsList) {
                selectedItems += item + ", ";
            }
            Toast.makeText(HomeActivity.this, selectedItems, Toast.LENGTH_SHORT).show();
        });

        loadDataFromFirebase();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
    }

    private void showAddItemDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_item, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextItemName = dialogView.findViewById(R.id.editTextItemName);
        final Button expiryDateButton = dialogView.findViewById(R.id.button_expiry_date);

        expiryDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        expiryDateButton.setText(selectedDate);
                    }
                };

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, R.style.CustomDatePickerTheme, dateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        dialogBuilder.setTitle("add new item");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String itemName = editTextItemName.getText().toString();
                String expiryDate = expiryDateButton.getText().toString();
                addCardItem(itemName, expiryDate);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void addCardItem(String itemName, String expiryDate) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userItemsRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(currentUser.getUid())
                    .child("items");

            String itemId = userItemsRef.push().getKey();
            CardItem newItem = new CardItem(itemName, expiryDate);

            if (itemId != null) {
                userItemsRef.child(itemId).setValue(newItem)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                cardAdapter.notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(HomeActivity.this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    @Override
    public void onDeleteClick(CardItem item) {
        deleteCardItem(item);
        // AlarmManager.cancel(pendingIntent);
        cardAdapter.removeSelectedItem(item.getItemId());
    }

    private void deleteCardItem(CardItem cardItem) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userItemsRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(currentUser.getUid())
                    .child("items")
                    .child(cardItem.getItemId());

            // Get the notification ID associated with the item
            int notificationId = cardItem.getItemId().hashCode();

            userItemsRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            cardList.remove(cardItem);

                            // Remove the notification when the item is deleted
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(HomeActivity.this);
                            notificationManager.cancel(notificationId);

                            cardAdapter.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomeActivity.this, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    @Override
    public void onItemSelect(int position, boolean isSelected) {
        CardItem selectedItem = cardList.get(position);
        if (isSelected) {
            selectedItemsList.add(selectedItem.getItemName());
        } else {
            selectedItemsList.remove(selectedItem.getItemName());
        }
    }

    private void loadDataFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userItemsRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(currentUser.getUid())
                    .child("items");

            // Add a ValueEventListener to keep track of selected items
            userItemsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cardList.clear();
                    selectedItemsList.clear();
                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                        CardItem cardItem = itemSnapshot.getValue(CardItem.class);
                        if (cardItem != null) {
                            cardItem.setItemId(itemSnapshot.getKey());
                            cardList.add(cardItem);

                            // Check if the item is selected and add it to the selectedItemsList
                            if (cardItem.isSelected()) {
                                selectedItemsList.add(cardItem.getItemId());
                            }
                        }
                    }
                    cardAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(HomeActivity.this, "Failed to load data from Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}