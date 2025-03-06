package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private List<WeddingShopModel> shopList;

    public ShopAdapter(List<WeddingShopModel> shopList) {
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        WeddingShopModel shop = shopList.get(position);
        holder.shopNameTextView.setText(shop.getShopName());
        holder.shopCategoryTextView.setText(shop.getShopCategory());

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(shop.getShopImage())
                .placeholder(R.drawable.band_cat) // Add a placeholder image in your drawable folder
                .into(holder.shopImageView);
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView shopNameTextView, shopCategoryTextView;
        ImageView shopImageView;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            shopNameTextView = itemView.findViewById(R.id.shop_name_textview);
            shopCategoryTextView = itemView.findViewById(R.id.shop_category_textview);
            shopImageView = itemView.findViewById(R.id.shop_imageview);
        }
    }
}