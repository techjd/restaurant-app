package com.techjd.swagatcorneradmin.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techjd.swagatcorneradmin.API;
import com.techjd.swagatcorneradmin.OrdersActivity;
import com.techjd.swagatcorneradmin.R;
import com.techjd.swagatcorneradmin.models.AllBranches;
import com.techjd.swagatcorneradmin.models.Orders;

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
            public void onClick(final View view) {
                Toast.makeText(view.getContext(), allBranches.getId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), OrdersActivity.class);
                intent.putExtra("BRANCHID", allBranches.getId());
                view.getContext().startActivity(intent);
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://agile-everglades-70176.herokuapp.com/allBranches/")
//                        .addConverterFactory(GsonConverterFactory.create())
//
//                        .build();
//
//                API api = retrofit.create(API.class);
//
//                Call<List<AllBranches>> listCall = api.getBranches();
//
//                listCall.enqueue(new Callback<List<AllBranches>>() {
//                    @Override
//                    public void onResponse(Call<List<AllBranches>> call, Response<List<AllBranches>> response) {
//                        if (response.isSuccessful()) {
//                            Toast.makeText(view.getContext(), "Brnach", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(view.getContext(), OrdersActivity.class);
//                            intent.putExtra("BRANCHID", allBranches.getId());
//                            view.getContext().startActivity(intent);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<AllBranches>> call, Throwable t) {
//                        Log.e("ERROR", t.getMessage());
//                    }
//                });




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
