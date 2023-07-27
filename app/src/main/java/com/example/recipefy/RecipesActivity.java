package com.example.recipefy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

    private ArrayList<RecipesAdapter> adapterArrayList = new ArrayList<>();
    private HomeActivity homeActivity;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> selectedItemsList;


    RecyclerView recyclerView;
    ProgressBar progressBar;
    RecipesAdapter adapter;
    AsyncHttpClient client;
    Workbook workbook;
    List<String> dishDescription, dishName, dishPicURL, dishIngredients;


    public ArrayList<String> getSelectedItemsList() {
        return selectedItemsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // Retrieve the selectedItemsList from the intent
        Intent intent = getIntent();
        selectedItemsList = intent.getStringArrayListExtra("selectedItemsList");

        // connecting activity to xml components
        progressBar = findViewById(R.id.progressBar);
        addData(selectedItemsList);

        // setting array adapter, which sets the information displayed in the searchview
        arrayAdapter = new ArrayAdapter<>
                (this, R.layout.list_item, R.id.dishNameTextView, dishName);

    }

    private void addData(ArrayList<String> usableIngredients) {
        // code for excel spreadsheet information
        String url = "https://github.com/brindamoudgalya/MoonGate/blob/master/MoonGateFinalSheet.xls?raw=true";
        recyclerView = findViewById(R.id.recyclerView);

        dishDescription = new ArrayList<>();
        dishName = new ArrayList<>();
        dishPicURL = new ArrayList<>();
        dishIngredients = new ArrayList<>();

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
                        for (int i = 0; i < sheet.getRows(); i++) {
                            Cell[] row = sheet.getRow(i);
                            for (String ingredient : usableIngredients) {
                                if (row[4].getContents().toLowerCase(Locale.ROOT).contains(ingredient)) {
                                    dishDescription.add(row[2].getContents());
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
        adapter = new RecipesAdapter(this, dishDescription, dishName, dishIngredients, dishPicURL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}