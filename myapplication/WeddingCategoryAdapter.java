package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeddingCategoryAdapter extends RecyclerView.Adapter<WeddingCategoryAdapter.ViewHolder> {

    Context context;
    List<WeddingCategoryModel> categoryList;
    OnCategoryClickListener listener;

    public WeddingCategoryAdapter(Context context, List<WeddingCategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wedding_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeddingCategoryModel category = categoryList.get(position);
        holder.catImage.setImageResource(category.getImage());
        holder.catName.setText(category.getName());

        holder.itemView.setOnClickListener(view -> {
            // Navigate to WeddingShopActivity with the selected category
            Intent intent = new Intent(context, WeddingShopActivity.class);
            intent.putExtra("category", category.getCategory()); // Pass the selected category
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView catImage;
        TextView catName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            catImage = itemView.findViewById(R.id.imageview_wedding);
            catName = itemView.findViewById(R.id.textview_wedding);
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }
}