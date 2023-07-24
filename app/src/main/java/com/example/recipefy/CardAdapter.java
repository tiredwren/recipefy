package com.example.recipefy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardItem> cardItemList;
    private Context context;

    public CardAdapter(List<CardItem> cardItemList, Context context) {
        this.cardItemList = cardItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem cardItem = cardItemList.get(position);
        holder.tvItemName.setText(cardItem.getItemName());
        holder.tvExpiryDate.setText(formatDate(cardItem.getExpiryDate()));

        int backgroundColor = ContextCompat.getColor(context,
                cardItem.isSelected() ? R.color.card_selected : android.R.color.white);
        holder.cardView.setCardBackgroundColor(backgroundColor);

        holder.cardView.setOnClickListener(v -> {
            cardItem.setSelected(!cardItem.isSelected());
            int newBackgroundColor = ContextCompat.getColor(context,
                    cardItem.isSelected() ? R.color.card_selected : android.R.color.white);
            holder.cardView.setCardBackgroundColor(newBackgroundColor);
        });
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvExpiryDate;
        CardView cardView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.itemNameTextView);
            tvExpiryDate = itemView.findViewById(R.id.expiryDateTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
