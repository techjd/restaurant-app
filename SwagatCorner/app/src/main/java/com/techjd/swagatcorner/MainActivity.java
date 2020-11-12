package com.techjd.swagatcorner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.techjd.swagatcorner.Adapters.FoodCategoriesAdapter;
import com.techjd.swagatcorner.models.AllCategories;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FoodCategoriesAdapter foodCategoriesAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        Log.d("TOKEN", token);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new OrderFragment()).commit();
        }

//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Food Categories");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
//
//
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "allFoodItems/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        API api = retrofit.create(API.class);
//
//        Call<AllCategories> call = api.getAllCategories();
//
//        call.enqueue(new Callback<AllCategories>() {
//            @Override
//            public void onResponse(Call<AllCategories> call, Response<AllCategories> response) {
//                AllCategories allCategories = response.body();
//                ArrayList<String> categoryName = new ArrayList<>();
//                for (int i = 0; i < allCategories.getCategory().size(); i++) {
//                    categoryName.add(allCategories.getCategory().get(i));
//
//                }
//
//                foodCategoriesAdapter = new FoodCategoriesAdapter(categoryName);
//                progressDialog.dismiss();
//                recyclerView.setAdapter(foodCategoriesAdapter);
//
//            }
//
//            @Override
//            public void onFailure(Call<AllCategories> call, Throwable t) {
//                Log.d("ERROR", t.getMessage());
//            }
//        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_order:
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_cart:
                            selectedFragment = new CartFragment();
                            break;
                        case R.id.nav_about:
                            selectedFragment = new AboutFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Carts", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.item2:
                Toast.makeText(this, "Your Order", Toast.LENGTH_SHORT).show();
                Intent history = new Intent(this, HistoryActivity.class);
                startActivity(history);
        }
        return super.onOptionsItemSelected(item);
    }
}