package com.example.recipefy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final ArrayList<CardItem> cardList;
    private final ArrayList<String> selectedItemsList;
    private final OnDeleteClickListener deleteClickListener;
    private final OnItemSelectListener itemSelectListener;

    public CardAdapter(ArrayList<CardItem> cardList, ArrayList<String> selectedItemsList,
                       OnDeleteClickListener deleteClickListener, OnItemSelectListener itemSelectListener) {
        this.cardList = cardList;
        this.selectedItemsList = selectedItemsList;
        this.deleteClickListener = deleteClickListener;
        this.itemSelectListener = itemSelectListener;
    }

    public void removeSelectedItem(String itemId) {
        selectedItemsList.remove(itemId);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(view, deleteClickListener, itemSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem currentItem = cardList.get(position);
        holder.textViewItemName.setText(currentItem.getItemName());
        holder.textViewExpiryDate.setText(currentItem.getExpiryDate());
        holder.checkBoxItemSelect.setChecked(selectedItemsList.contains(currentItem.getItemId()));
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItemName;
        private TextView textViewExpiryDate;
        private CheckBox checkBoxItemSelect;

        public CardViewHolder(@NonNull View itemView, OnDeleteClickListener deleteClickListener,
                              OnItemSelectListener itemSelectListener) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.itemNameTextView);
            textViewExpiryDate = itemView.findViewById(R.id.expiryDateTextView);
            checkBoxItemSelect = itemView.findViewById(R.id.check_box_select);

            checkBoxItemSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                itemSelectListener.onItemSelect(getAdapterPosition(), isChecked);
            });

            itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
                deleteClickListener.onDeleteClick(cardList.get(getAdapterPosition()));
            });
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(CardItem item);
    }

    public interface OnItemSelectListener {
        void onItemSelect(int position, boolean isSelected);
    }
}
