package com.example.recipefy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class RecipesActivity extends AppCompatActivity {

    private ArrayList<String> selectedItemsList;

    RecyclerView recyclerView;

    ImageButton backButton;
    ProgressBar progressBar;
    RecipesAdapter adapter;
    AsyncHttpClient client;
    Workbook workbook;
    List<String> dishDescription, dishName, dishPicURL, dishIngredients, dishRecipeURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Intent intent = getIntent();
        selectedItemsList = intent.getStringArrayListExtra("selectedItemsList");

        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.imageButton);
        addData(selectedItemsList);

        backButton.setOnClickListener(view -> {
            startActivity(new Intent(this,HomeActivity.class));
        });
    }

    private void addData(ArrayList<String> usableIngredients) {
        // url is from github, xls file
        String url = "https://github.com/tiredwren/recipefy/raw/master/RECIPE%20SPREADSHEET%20FINAL.xls";
        recyclerView = findViewById(R.id.recyclerView);

        // creating arraylists for each part of the card
        dishDescription = new ArrayList<>();
        dishName = new ArrayList<>();
        dishPicURL = new ArrayList<>();
        dishIngredients = new ArrayList<>();
        dishRecipeURL = new ArrayList<>();

        client = new AsyncHttpClient();
        progressBar.setVisibility(View.VISIBLE);
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            // error catching so app doesn't crash
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RecipesActivity.this, "Download Failed.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progressBar.setVisibility(View.GONE);
                WorkbookSettings ws = new WorkbookSettings();
                ws.setGCDisabled(true);
                if (file != null) {
                    try {
                        // accessing the excel downloaded spreadsheet
                        workbook = Workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(0);
                        for (int i = 1; i < sheet.getRows(); i++) {
                            Cell[] row = sheet.getRow(i);

                            // shows all available recipes if no specific ingredients are specified
                            if (usableIngredients.isEmpty()) {
                                dishDescription.add(row[2].getContents());
                                dishRecipeURL.add(row[0].getContents());
                                dishName.add(row[1].getContents());
                                dishIngredients.add(row[4].getContents());
                                dishPicURL.add(row[3].getContents());
                            } else {
                                // making sure the recipes shown contain ALL chosen ingredients
                                boolean hasAll = true;
                                for (String ingredient : usableIngredients) {
                                    if (!row[4].getContents().toLowerCase(Locale.ROOT).contains(ingredient)) {
                                        hasAll = false;
                                    }
                                }
                                if (hasAll) {
                                    dishDescription.add(row[2].getContents());
                                    dishRecipeURL.add(row[0].getContents());
                                    dishName.add(row[1].getContents());
                                    dishIngredients.add(row[4].getContents());
                                    dishPicURL.add(row[3].getContents());
                                }
                            }
                        }

                        showData();

                    } catch (IOException | BiffException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showData() {
        // calling adapter class
        adapter = new RecipesAdapter(this, dishDescription, dishName, dishIngredients, dishPicURL, dishRecipeURL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
