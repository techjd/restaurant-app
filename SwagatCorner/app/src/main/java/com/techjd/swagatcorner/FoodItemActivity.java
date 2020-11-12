package com.techjd.swagatcorner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.techjd.swagatcorner.Adapters.FoodCategoriesAdapter;
import com.techjd.swagatcorner.Adapters.FoodItemsAdapter;
import com.techjd.swagatcorner.models.AllCategories;
import com.techjd.swagatcorner.models.AllFoodItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodItemActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FoodItemsAdapter foodItemsAdapter;
    private RecyclerView recyclerView;
    private String category;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);
        category = getIntent().getStringExtra("CATEGORYNAME");
        Log.d("LETS SEE NAME", category);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(category);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Food Items");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL+"allFoodItems/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        Call<List<AllFoodItem>> call = api.getAllFoodItems(category);

        call.enqueue(new Callback<List<AllFoodItem>>() {
            @Override
            public void onResponse(Call<List<AllFoodItem>> call, Response<List<AllFoodItem>> response) {
                List<AllFoodItem> allFoodItems = response.body();
                ArrayList<AllFoodItem> allFoodItemArrayList = new ArrayList<>();

                for (int i = 0; i < allFoodItems.size(); i++) {

                    allFoodItemArrayList.add(new AllFoodItem(allFoodItems.get(i).getItemName()
                            , allFoodItems.get(i).getPrice()
                            , allFoodItems.get(i).getStockORNot(),
                            allFoodItems.get(i).getId()));
                }

                foodItemsAdapter = new FoodItemsAdapter(allFoodItemArrayList);
                progressDialog.dismiss();
                recyclerView.setAdapter(foodItemsAdapter);


            }

            @Override
            public void onFailure(Call<List<AllFoodItem>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}