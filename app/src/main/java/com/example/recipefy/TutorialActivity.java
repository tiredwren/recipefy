package com.example.recipefy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isNewUser = preferences.getBoolean("isNewUser", true);

        if (isNewUser) {
            setContentView(R.layout.activity_tutorial);

            ViewPager viewPager = findViewById(R.id.viewPager);
            TutorialPagerAdapter adapter = new TutorialPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);

            Button skipButton = findViewById(R.id.skipButton);
            skipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences.edit().putBoolean("isNewUser", false).apply();
                    startActivity(new Intent(TutorialActivity.this, HomeActivity.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(TutorialActivity.this, HomeActivity.class));
            finish();
        }
    }
}
