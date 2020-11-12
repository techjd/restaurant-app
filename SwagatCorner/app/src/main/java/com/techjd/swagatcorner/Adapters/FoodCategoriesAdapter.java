package com.techjd.swagatcorner.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techjd.swagatcorner.FoodItemActivity;
import com.techjd.swagatcorner.R;
import com.techjd.swagatcorner.models.AllCategories;
import com.techjd.swagatcorner.models.AllFoodItem;

import java.util.List;

public class FoodCategoriesAdapter extends RecyclerView.Adapter<FoodCategoriesAdapter.FoodViewHolder> {

    private List<String> allCategories;

    public FoodCategoriesAdapter(List<String> allCategories) {
        this.allCategories = allCategories;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.food_categories, parent, false);
        FoodViewHolder foodViewHolder = new FoodViewHolder(listItem);
        return foodViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        String categoryName = allCategories.get(position);
        holder.category.setText(categoryName);

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(holder.itemView.getContext(), categoryName, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(holder.itemView.getContext(), FoodItemActivity.class);
                intent.putExtra("CATEGORYNAME", categoryName);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView category;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.category);
        }
    }
}
