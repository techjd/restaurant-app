package com.techjd.swagatcorner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.techjd.swagatcorner.Adapters.CartItemsAdapter;
import com.techjd.swagatcorner.Adapters.FoodItemsAdapter;
import com.techjd.swagatcorner.models.AllCartItems;
import com.techjd.swagatcorner.models.AllFoodItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TextView totalPrice;
    private CartItemsAdapter cartItemsAdapter;
    private Button pay;
    private Integer sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Cart");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        totalPrice = findViewById(R.id.totalPrice);
        pay = findViewById(R.id.pay);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, AllBranchesActivity.class);

                startActivity(intent);
//                startPayment();
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplication());
//        String token = googleSignInAccount.getIdToken();

        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        Log.d("TOKEN", token);

        Call<List<AllCartItems>> call = api.getAllCartItems(token);

        call.enqueue(new Callback<List<AllCartItems>>() {
            @Override
            public void onResponse(Call<List<AllCartItems>> call, Response<List<AllCartItems>> response) {
                List<AllCartItems> allCartItems = response.body();
                ArrayList<AllCartItems> allCartItemsArrayList = new ArrayList<>();


                for (int i = 0; i < allCartItems.size(); i++) {
                    Log.d("ALL CART PRCIES", allCartItems.get(i).getPrice().toString());
                    sum = sum + allCartItems.get(i).getPrice();
                    Log.d("SUM", sum.toString());
                    allCartItemsArrayList.add(new AllCartItems(allCartItems.get(i).getItemName()
                            , allCartItems.get(i).getPrice()
                            , allCartItems.get(i).getQuantity(),
                            allCartItems.get(i).getId()));
                }
                totalPrice.setText(sum.toString());

                if (sum.toString().equals("0")) {
                    pay.setEnabled(false);
                    Toast.makeText(CartActivity.this, "Cart Is Empty", Toast.LENGTH_LONG).show();
                }
                cartItemsAdapter = new CartItemsAdapter(allCartItemsArrayList);
                progressDialog.dismiss();
                recyclerView.setAdapter(cartItemsAdapter);

            }

            @Override
            public void onFailure(Call<List<AllCartItems>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }


}
