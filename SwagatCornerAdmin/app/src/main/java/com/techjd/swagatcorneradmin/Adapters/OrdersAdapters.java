package com.techjd.swagatcorneradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techjd.swagatcorneradmin.FoodItemsActivity;
import com.techjd.swagatcorneradmin.R;
import com.techjd.swagatcorneradmin.models.OrderItem;
import com.techjd.swagatcorneradmin.models.Orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrdersAdapters extends RecyclerView.Adapter<OrdersAdapters.OrdersViewHolder> {
    List<Orders> ordersList;

    public OrdersAdapters(List<Orders> ordersList) {
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listOrders = layoutInflater.inflate(R.layout.item_orders, parent, false);
        OrdersViewHolder ordersViewHolder = new OrdersViewHolder(listOrders);
        return ordersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        final Orders orders = ordersList.get(position);


        Integer price = orders.getTotalPrice();
        String totalPrice = price.toString();
        Boolean paid = orders.getIsPaid();
        String isPaid = paid.toString();
        holder.amount.setText(totalPrice);
        holder.paid.setText(isPaid);
        for (int i = 0; i < orders.getOrderItems().size(); i++) {
            String content = "";

            content += orders.getOrderItems().get(i).getItemName() + "  (" + orders.getOrderItems().get(i).getQuantity().toString() + ")" + "\n";
//            holder.name.setText(orders.getOrderItems().get(i).getItemName());
            holder.name.append(content);
        }

        holder.cart.setText(orders.getId());


    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, paid, name,  cart;
        public CardView cardView;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            paid = itemView.findViewById(R.id.paid);
            cardView = itemView.findViewById(R.id.cardView);
            name = itemView.findViewById(R.id.name);

            cart = itemView.findViewById(R.id.order);
        }
    }
}
