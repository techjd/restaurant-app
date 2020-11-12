package com.techjd.swagatcorneradmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techjd.swagatcorneradmin.R;
import com.techjd.swagatcorneradmin.models.OrderItem;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemsViewHolder> {
    List<OrderItem> orderItemList;

    @NonNull
    @Override
    public OrderItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_food_items, parent, false);
        OrderItemsViewHolder orderItemsViewHolder = new OrderItemsViewHolder(listItem);
        return orderItemsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemsViewHolder holder, int position) {
        final OrderItem orderItem = orderItemList.get(position);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderItemsViewHolder extends RecyclerView.ViewHolder {

        public OrderItemsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
