package com.techjd.swagatcorner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.techjd.swagatcorner.Adapters.BranchesAdapter;
import com.techjd.swagatcorner.models.AllBranches;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllBranchesActivity extends AppCompatActivity {
    private ProgressDialog progressDialog ;
    private BranchesAdapter branchesAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_branches);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading All Branches");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);


        Call<List<AllBranches>> call  = api.getAllBranches();

        call.enqueue(new Callback<List<AllBranches>>() {
            @Override
            public void onResponse(Call<List<AllBranches>> call, Response<List<AllBranches>> response) {
                List<AllBranches> allBranchesList = response.body();

                ArrayList<AllBranches> allBranchesArrayList = new ArrayList<>();
                for (int i = 0; i < allBranchesList.size(); i++) {
                    Log.d("ALLBRANCHES", allBranchesList.get(i).getBranchName());

                    allBranchesArrayList.add(new AllBranches(
                            allBranchesList.get(i).getId(),
                            allBranchesList.get(i).getBranchName(),
                            allBranchesList.get(i).getBranchAddress()
                    ));

                }

                branchesAdapter = new BranchesAdapter(allBranchesArrayList);
                progressDialog.dismiss();
                recyclerView.setAdapter(branchesAdapter);

            }

            @Override
            public void onFailure(Call<List<AllBranches>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });


    }
}