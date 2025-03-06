package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;

import java.util.List;

public class WeddingShopAdapter extends RecyclerView.Adapter<WeddingShopAdapter.ViewHolder> {
    private final Context context;
    private final List<WeddingShopModel> shopList;

    public WeddingShopAdapter(Context context, List<WeddingShopModel> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wedding_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeddingShopModel shop = shopList.get(position);
        holder.shopName.setText(shop.getShopName());

        // Load shop image
        int imageResource = context.getResources().getIdentifier(
                shop.getShopImage(), // e.g., "image4.jpg"
                "drawable",          // Resource type
                context.getPackageName()
        );

        if (imageResource != 0) { // Only load if a valid resource is found
            holder.shopImage.setImageResource(imageResource);
        } else {
            holder.shopImage.setImageResource(R.drawable.account); // Use a default image
        }
    }

    @Override
    public int getItemCount() {
        return shopList != null ? shopList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        ImageView shopImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.wed_text);
            shopImage = itemView.findViewById(R.id.imageview_wedding_shop);
        }
    }
}
