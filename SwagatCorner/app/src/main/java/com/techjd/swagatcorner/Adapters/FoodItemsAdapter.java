package com.techjd.swagatcorner.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.number.IntegerWidth;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.techjd.swagatcorner.API;
import com.techjd.swagatcorner.Constants;
import com.techjd.swagatcorner.R;
import com.techjd.swagatcorner.models.AllFoodItem;
import com.techjd.swagatcorner.models.Cart;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodItemsAdapter extends RecyclerView.Adapter<FoodItemsAdapter.FoodItemsViewHolder> {
    List<AllFoodItem> allFoodItemList;
    Integer[] nums = {0, 1, 2, 3, 4, 5};
    Integer finalPrice;
    Integer stringPrice;
    Integer quantity;
    Integer price;

    public FoodItemsAdapter(List<AllFoodItem> allFoodItemList) {
        this.allFoodItemList = allFoodItemList;
    }

    @NonNull
    @Override
    public FoodItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_food, parent, false);
        FoodItemsViewHolder foodItemsViewHolder = new FoodItemsViewHolder(listItem);
        return foodItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemsViewHolder holder, int position) {
        final AllFoodItem allFoodItem = allFoodItemList.get(position);

        holder.foodName.setText(allFoodItem.getItemName());
        holder.foodPrice.setText(allFoodItem.getPrice().toString());
        holder.foodStock.setText(allFoodItem.getStockORNot().toString());


//        holder.spinner.setOnItemSelectedListener(this);

        ArrayAdapter<Integer> list = new ArrayAdapter<Integer>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, nums);
        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(list);
        holder.spinner.setSelection(0);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(adapterView.getContext(), adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                if (adapterView.getSelectedItem().equals(0)) {
//                    Toast.makeText(view.getContext(), "Please Select Quantity", Toast.LENGTH_SHORT).show();
                    holder.addToCart.setEnabled(false);
                } else {
                    holder.addToCart.setEnabled(true);
                    stringPrice = Integer.parseInt(adapterView.getSelectedItem().toString());
                    Log.d("PRICE", stringPrice.toString());
                    quantity = stringPrice;
                    finalPrice = allFoodItem.getPrice() * stringPrice;
                    price = finalPrice;
                    Log.d("FINALPRICE", finalPrice.toString());
                    holder.foodPrice.setText(finalPrice.toString());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String wholeItem = allFoodItem.getItemName() + " " + finalPrice + " " + holder.spinner.getSelectedItem();

                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setTitle("Adding");
                progressDialog.setMessage("Adding Item To Cart");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
//                Toast.makeText(view.getContext(), wholeItem, Toast.LENGTH_LONG).show();
                Log.d("WHOLEITEM", wholeItem);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.URL)
                        .addConverterFactory(GsonConverterFactory.create())

                        .build();

                API api = retrofit.create(API.class);
//                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(view.getContext());
//                String token = googleSignInAccount.getIdToken();

                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("TOKEN", "");
                Log.d("TOKEN", token);
                Call<Cart> cartCall = api.addToCart(token, allFoodItem.getId(), allFoodItem.getItemName(), finalPrice, Integer.parseInt(holder.spinner.getSelectedItem().toString()));

                cartCall.enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        if (!response.isSuccessful()) {
                            Log.d("ERRO", response.message());

                            Toast.makeText(view.getContext(), "Item Not Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(), "Item Added To Cart", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Server Error, Please Add Later", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }


    @Override
    public int getItemCount() {
        return allFoodItemList.size();
    }


    public static class FoodItemsViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView foodName, foodPrice, foodStock;
        public Button addToCart;
        public Spinner spinner;

        public FoodItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.itemName);
            foodPrice = itemView.findViewById(R.id.itemPrice);
            foodStock = itemView.findViewById(R.id.InStock);
            addToCart = itemView.findViewById(R.id.addToCart);
            spinner = itemView.findViewById(R.id.spinner);
            cardView = itemView.findViewById(R.id.cardView);


        }
    }
}
