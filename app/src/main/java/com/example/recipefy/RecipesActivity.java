package com.example.recipefy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.annotation.Documented;


public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        final TextView dishName = findViewById(R.id.dishName);
        final ImageView image = findViewById(R.id.imgView);

        // implementing jsoup
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    Document doc = Jsoup.connect
                            ("https://www.allrecipes.com/ingredients-a-z-6740416").
                            timeout(6000).get();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}