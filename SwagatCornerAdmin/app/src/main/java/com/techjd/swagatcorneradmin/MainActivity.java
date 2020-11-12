package com.techjd.swagatcorneradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techjd.swagatcorneradmin.Adapters.BranchesAdapter;
import com.techjd.swagatcorneradmin.models.AllBranches;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private BranchesAdapter branchesAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("TOKEN", token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Your Branches");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://agile-everglades-70176.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);


        Call<List<AllBranches>> call  = api.getBranches();

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