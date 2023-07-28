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
        String url = "https://github.com/tiredwren/recipefy/raw/master/SHORTER%20DESCRIPTIONS%20-%20FINAL%20RECIPE%20SPREADSHEET.xls";
        recyclerView = findViewById(R.id.recyclerView);

        dishDescription = new ArrayList<>();
        dishName = new ArrayList<>();
        dishPicURL = new ArrayList<>();
        dishIngredients = new ArrayList<>();
        dishRecipeURL = new ArrayList<>();

        client = new AsyncHttpClient();
        progressBar.setVisibility(View.VISIBLE);
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
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
                        workbook = Workbook.getWorkbook(file);
                        Sheet sheet = workbook.getSheet(0);
                        for (int i = 1; i < sheet.getRows(); i++) {
                            Cell[] row = sheet.getRow(i);

                            if (usableIngredients.isEmpty()) {
                                dishDescription.add(row[2].getContents());
                                dishRecipeURL.add(row[0].getContents());
                                dishName.add(row[1].getContents());
                                dishIngredients.add(row[4].getContents());
                                dishPicURL.add(row[3].getContents());
                            } else {
                                for (String ingredient : usableIngredients) {
                                    if (row[4].getContents().toLowerCase(Locale.ROOT).contains(ingredient)) {
                                        dishDescription.add(row[2].getContents());
                                        dishRecipeURL.add(row[0].getContents());
                                        dishName.add(row[1].getContents());
                                        dishIngredients.add(row[4].getContents());
                                        dishPicURL.add(row[3].getContents());
                                    }}
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
        adapter = new RecipesAdapter(this, dishDescription, dishName, dishIngredients, dishPicURL, dishRecipeURL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
