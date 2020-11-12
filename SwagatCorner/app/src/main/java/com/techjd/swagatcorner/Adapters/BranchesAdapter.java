package com.techjd.swagatcorner.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.techjd.swagatcorner.API;
import com.techjd.swagatcorner.Constants;
import com.techjd.swagatcorner.FoodItemActivity;
import com.techjd.swagatcorner.PaymentActivity;
import com.techjd.swagatcorner.R;
import com.techjd.swagatcorner.models.AllBranches;
import com.techjd.swagatcorner.models.Cart;
import com.techjd.swagatcorner.models.Orders;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.BranchesViewHolder> {
    List<AllBranches> allBranchesList;

    public BranchesAdapter(List<AllBranches> allBranchesList) {
        this.allBranchesList = allBranchesList;
    }

    @NonNull
    @Override
    public BranchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_branches, parent, false);
        BranchesViewHolder branchesViewHolder = new BranchesViewHolder(listItem);
        return branchesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BranchesViewHolder holder, int position) {
        final AllBranches allBranches = allBranchesList.get(position);

        holder.branchName.setText(allBranches.getBranchName());
        holder.branchAddress.setText(allBranches.getBranchAddress());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(view.getContext(), allBranches.getId(), Toast.LENGTH_LONG).show();
                Context context;
                final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Taking you to Payment Activity");
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
                Call<Orders> ordersCall = api.addToOrder(token, allBranches.getId());



                ordersCall.enqueue(new Callback<Orders>() {
                    @Override
                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Order SuccessFully Placed", Toast.LENGTH_LONG).show();
                            String orders = response.body().getId();

                            Integer totalPrice = response.body().getTotalPrice();
                            Log.d("PRICE", String.valueOf(totalPrice));
                            Log.d("ORDERS", orders);
                            progressDialog.dismiss();
                            Intent intent = new Intent(holder.itemView.getContext(), PaymentActivity.class);
                            intent.putExtra("PRICE", totalPrice);
                            intent.putExtra("ORDERID", orders);
                            view.getContext().startActivity(intent);

                        } else {
                            Toast.makeText(view.getContext(), "Try Again After Some Time", Toast.LENGTH_LONG).show();
                            Log.d("WHAT IS GOING ON", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Orders> call, Throwable t) {
                        Log.d("ERROR", t.getMessage());
                    }
                });


            }
        });




    }

    @Override
    public int getItemCount() {
        return allBranchesList.size();
    }

    public static class BranchesViewHolder extends RecyclerView.ViewHolder {
        public TextView branchName, branchAddress;
        public CardView cardView;

        public BranchesViewHolder(@NonNull View itemView) {
            super(itemView);

            branchName = itemView.findViewById(R.id.branchName);
            branchAddress = itemView.findViewById(R.id.branchAddress);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
