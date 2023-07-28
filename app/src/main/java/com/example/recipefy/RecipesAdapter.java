package com.example.recipefy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private Context context;
    private List<String> dishDescription, dishName, dishIngredients, dishPicURL, dishRecipeURL;

    public RecipesAdapter(Context context, List<String> dishDescription, List<String> dishName,
                          List<String> dishIngredients, List<String> dishPicURL, List<String> dishRecipeURL) {
        this.context = context;
        this.dishDescription = dishDescription;
        this.dishName = dishName;
        this.dishIngredients = dishIngredients;
        this.dishPicURL = dishPicURL;
        this.dishRecipeURL = dishRecipeURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String description = dishDescription.get(position);
        String dish = dishName.get(position);
        String picURL = dishPicURL.get(position);

        holder.description.setText(description);
        holder.dish.setText(dish);

        if (picURL != null && !picURL.isEmpty()) {
            // Load the image using Picasso
            Picasso.get()
                    .load(picURL)
                    .fit()
                    .centerCrop()
                    .into(holder.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image loaded successfully
                        }

                        @Override
                        public void onError(Exception e) {
                            // Handle errors here
                            e.printStackTrace();
                        }
                    });
        } else {
            // If the image URL is empty, you can set a placeholder image
            holder.image.setImageResource(R.drawable.placeholder_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                String recipeURL = dishRecipeURL.get(clickedPosition);
                if (recipeURL != null && !recipeURL.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeURL));
                    v.getContext().startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
