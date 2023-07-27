package com.example.recipefy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

// reference: https://www.youtube.com/watch?v=kxdVo4RH3nE

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<String> dishDescription, dishName, dishIngredients, picURL;

    public Adapter(Context context, List<String> dishDescription, List<String> dishName,
                   List<String> dishIngredients, List<String> dishPicURL){
        this.inflater= LayoutInflater.from(context);
        this.dishDescription = dishDescription;
        this.dishName = dishName;
        this.dishIngredients = dishIngredients;
        this.picURL = dishPicURL;

//        Log.d("TAG", "Adapter: " + plantName);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String description = dishDescription.get(position);
        String dish = dishName.get(position);
        String picURL = this.picURL.get(position);

        // load text into individual textViews
        holder.description.setText(description);
        holder.dish.setText(dish);

        // load picture into imageView for the picture
//        Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(holder.image);
        Picasso.get().load(picURL).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return dishName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView description, dish;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.dishDescriptionTextView);
            dish = itemView.findViewById(R.id.dishNameTextView);
            image = itemView.findViewById(R.id.pictureOfDish);
        }
    }
}