package com.techjd.swagatcorner;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techjd.swagatcorner.Adapters.FoodCategoriesAdapter;
import com.techjd.swagatcorner.models.AllCategories;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OrderFragment extends Fragment {
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ProgressDialog progressDialog;
    private FoodCategoriesAdapter foodCategoriesAdapter;
    private RecyclerView recyclerView;

    public OrderFragment() {
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        toolbarTitle = view.findViewById(R.id.toolbar_title);

        toolbarTitle.setText("SWAGAT CORNER");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Food Categories");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL + "allFoodItems/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        Call<AllCategories> call = api.getAllCategories();

        call.enqueue(new Callback<AllCategories>() {
            @Override
            public void onResponse(Call<AllCategories> call, Response<AllCategories> response) {
                AllCategories allCategories = response.body();
                ArrayList<String> categoryName = new ArrayList<>();
                for (int i = 0; i < allCategories.getCategory().size(); i++) {
                    categoryName.add(allCategories.getCategory().get(i));

                }

                foodCategoriesAdapter = new FoodCategoriesAdapter(categoryName);
                progressDialog.dismiss();
                recyclerView.setAdapter(foodCategoriesAdapter);

            }

            @Override
            public void onFailure(Call<AllCategories> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
            }
        });
    }
}