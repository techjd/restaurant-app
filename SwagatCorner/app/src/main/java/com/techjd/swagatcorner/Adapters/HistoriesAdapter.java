package com.techjd.swagatcorner.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techjd.swagatcorner.R;
import com.techjd.swagatcorner.models.History;

import java.util.List;

public class HistoriesAdapter extends RecyclerView.Adapter<HistoriesAdapter.HistoriesViewHolder> {
    List<History> historyList;

    public HistoriesAdapter(List<History> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_history, parent, false);
        HistoriesViewHolder historiesViewHolder = new HistoriesViewHolder(listItem);
        return historiesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriesViewHolder holder, int position) {
        final History history = historyList.get(position);

        Integer price = history.getTotalPrice();
        String totalPrice = price.toString();
        Boolean paid = history.getIsPaid();
        String isPaid = paid.toString();
        holder.amount.setText(totalPrice);
        holder.paid.setText(isPaid);
        for (int i = 0; i < history.getOrderItems().size(); i++) {
            String content = "";

            content += history.getOrderItems().get(i).getItemName() + "  (" + history.getOrderItems().get(i).getQuantity().toString() + ")" + "\n";
//            holder.name.setText(orders.getOrderItems().get(i).getItemName());
            holder.name.append(content);
        }

        holder.cart.setText(history.getId());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoriesViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, paid, name,  cart;
        public CardView cardView;

        public HistoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            paid = itemView.findViewById(R.id.paid);
            cardView = itemView.findViewById(R.id.cardView);
            name = itemView.findViewById(R.id.name);

            cart = itemView.findViewById(R.id.order);
        }
    }

}
