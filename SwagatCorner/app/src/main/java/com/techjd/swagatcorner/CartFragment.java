package com.techjd.swagatcorner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.techjd.swagatcorner.Adapters.CartItemsAdapter;
import com.techjd.swagatcorner.models.AllCartItems;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CartFragment extends Fragment {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private TextView totalPrice;
    private CartItemsAdapter cartItemsAdapter;
    private Button pay;
    private Integer sum = 0;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
//        toolbar.setTitle("Your Cart");
        toolbarTitle.setText("YOUR CART");

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Cart");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        totalPrice = view.findViewById(R.id.totalPrice);
        pay = view.findViewById(R.id.pay);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AllBranchesActivity.class);

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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
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
                    Toast.makeText(getContext(), "Cart Is Empty", Toast.LENGTH_LONG).show();
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