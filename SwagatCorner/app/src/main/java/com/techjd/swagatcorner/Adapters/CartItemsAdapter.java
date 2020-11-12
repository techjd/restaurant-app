package com.techjd.swagatcorner.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.techjd.swagatcorner.API;
import com.techjd.swagatcorner.CartActivity;
import com.techjd.swagatcorner.CartFragment;
import com.techjd.swagatcorner.Constants;
import com.techjd.swagatcorner.R;
import com.techjd.swagatcorner.models.AllCartItems;
import com.techjd.swagatcorner.models.Orders;
import com.techjd.swagatcorner.models.UpdateOrder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CartViewHolder> {
    List<AllCartItems> allCartItemsList;
    Integer[] nums = {1, 2, 3, 4, 5};
    Integer finalPrice;
    Integer stringPrice;

    public CartItemsAdapter(List<AllCartItems> allCartItemsList) {
        this.allCartItemsList = allCartItemsList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_cart, parent, false);
        CartViewHolder cartViewHolder = new CartViewHolder(listItem);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        final AllCartItems allCartItems = allCartItemsList.get(position);

        holder.itemName.setText(allCartItems.getItemName());
        holder.itemPrice.setText(allCartItems.getPrice().toString());
        holder.itemQuantity.setText(allCartItems.getQuantity().toString());


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Change The Quantity");
                builder.setCancelable(false);
                final View customView = LayoutInflater.from(view.getContext()).inflate(R.layout.ic_dialog, null);
                builder.setView(customView);
                Spinner spinner = customView.findViewById(R.id.spinner);
                ArrayAdapter<Integer> list = new ArrayAdapter<Integer>(view.getContext(), android.R.layout.simple_spinner_item, nums);
                list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(list);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                        progressDialog.setTitle("Updating");
                        progressDialog.setMessage("Updating Items From Cart");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        Toast.makeText(view.getContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL+"allCartItems/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        API api = retrofit.create(API.class);
//                        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(view.getContext());
//                        String token = googleSignInAccount.getIdToken();

                        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("TOKEN", "");
                        Log.d("TOKEN", token);
                        String id = allCartItems.getId();
                        String quan = spinner.getSelectedItem().toString();
                        Integer quantity = Integer.parseInt(quan);
                        Call<UpdateOrder> updateOrderCall = api.updateCartItem(token, id, quantity);
                        updateOrderCall.enqueue(new Callback<UpdateOrder>() {
                            @Override
                            public void onResponse(Call<UpdateOrder> call, Response<UpdateOrder> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(view.getContext(), "Item Updated", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
//                                    Intent intent = ((Activity) view.getContext()).getIntent();
//                                    ((Activity) view.getContext()).finish();
//
//                                    view.getContext().startActivity(intent);

                                    ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                            new CartFragment()).commit();
                                } else {
                                    Toast.makeText(view.getContext(), "Item Not Updated", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<UpdateOrder> call, Throwable t) {
                                Log.d("ERROR", t.getMessage());
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setTitle("Deleting");
                progressDialog.setMessage("Deleting Items From Cart");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.URL+"allCartItems/")
                        .addConverterFactory(GsonConverterFactory.create())

                        .build();

                API api = retrofit.create(API.class);
//                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(view.getContext());
//                String token = googleSignInAccount.getIdToken();
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("TOKEN", "");
                Log.d("TOKEN", token);
                String id = allCartItems.getId();
                Call<UpdateOrder> ordersCall = api.deleteOrder(token, id);

                ordersCall.enqueue(new Callback<UpdateOrder>() {
                    @Override
                    public void onResponse(Call<UpdateOrder> call, Response<UpdateOrder> response) {
                        if (response.isSuccessful()) {
                            removeAt(position);
                            Toast.makeText(view.getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
//                            Intent intent = ((Activity) view.getContext()).getIntent();
//                            ((Activity) view.getContext()).finish();
//
//                            view.getContext().startActivity(intent);

                            ((AppCompatActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    new CartFragment()).commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrder> call, Throwable t) {
                        Log.d("ERROR", t.getMessage());
                    }
                });


            }
        });
    }

    public void removeAt(int position) {
        allCartItemsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, allCartItemsList.size());
    }

    @Override
    public int getItemCount() {
        return allCartItemsList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName, itemPrice, itemQuantity;
        public ImageButton delete;
        public ImageButton edit;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.cartName);
            itemPrice = itemView.findViewById(R.id.cartPrice);
            itemQuantity = itemView.findViewById(R.id.cartQuantity);
            delete = itemView.findViewById(R.id.deletItem);
            edit = itemView.findViewById(R.id.editItem);
        }
    }
}
