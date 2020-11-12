package com.techjd.swagatcorneradmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


import com.techjd.swagatcorneradmin.Adapters.OrdersAdapters;
import com.techjd.swagatcorneradmin.models.Orders;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrdersActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private OrdersAdapters ordersAdapters;
    private RecyclerView recyclerView;
    private String branchid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
//        branchid = getIntent().getStringExtra("BRANCHID");
//        Log.d("BRANCH", branchid);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Your Branches Orders");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL+"allBranches/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        Log.d("TOKEN", token);
        Call<List<Orders>> listCall = api.getOrders(token);

        listCall.enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                if (response.isSuccessful()) {
                    List<Orders> orders = response.body();

                    ArrayList<Orders> ordersArrayList = new ArrayList<>();

                    for (int i = 0; i < orders.size(); i++) {
                        Log.d("ALLORDERS", orders.get(i).getIsPaid().toString());
                        ordersArrayList.add(new Orders(
                            orders.get(i).getIsPaid(),
                            orders.get(i).getId(),
                            orders.get(i).getUser(),
                            orders.get(i).getOrderItems(),
                            orders.get(i).getTotalPrice()

                        ));
                    }

                    ordersAdapters = new OrdersAdapters(ordersArrayList);

                    progressDialog.dismiss();
                    recyclerView.setAdapter(ordersAdapters);
                }
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {

            }
        });
    }
}