package com.techjd.swagatcorner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.techjd.swagatcorner.Adapters.FoodItemsAdapter;
import com.techjd.swagatcorner.Adapters.HistoriesAdapter;
import com.techjd.swagatcorner.models.History;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryActivity extends AppCompatActivity {
    private ProgressDialog progressDialog ;
    private HistoriesAdapter historiesAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Your History");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL+"allCartItems/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplication());
//        String token = googleSignInAccount.getIdToken();

        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        Log.d("TOKEN", token);
        Call<List<History>> listCall = api.getOrders(token);

        listCall.enqueue(new Callback<List<History>>() {
            @Override
            public void onResponse(Call<List<History>> call, Response<List<History>> response) {
                if (response.isSuccessful()) {
                    List<History> histories = response.body();
//                    Log.d("RES", histories.get(0).getTotalPrice().toString());
                    ArrayList<History> historyArrayList = new ArrayList<>();

                    for (int i = 0; i < histories.size(); i++) {
//                        Log.d("ORDERITEMS", histories.get(i).getOrderItems().toString());
                        historyArrayList.add(new History(
                                histories.get(i).getIsPaid(),
                                histories.get(i).getId(),
                                histories.get(i).getUser(),
                                histories.get(i).getOrderItems(),
                                histories.get(i).getTotalPrice()


                        ));
                    }

                    historiesAdapter = new HistoriesAdapter(historyArrayList);
                    progressDialog.dismiss();
                    recyclerView.setAdapter(historiesAdapter);


                }
            }

            @Override
            public void onFailure(Call<List<History>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}