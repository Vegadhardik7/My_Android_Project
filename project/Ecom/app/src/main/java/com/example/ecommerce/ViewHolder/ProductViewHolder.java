package com.example.ecommerce.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.Interface.ItemClickListener;
import com.example.ecommerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductName,txtproductDescription,txtproductPrice;
    public ImageView imageView;
    public ItemClickListener listener;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.pro_image);
        txtproductName=(TextView) itemView.findViewById(R.id.pro_name);
        txtproductPrice=(TextView) itemView.findViewById(R.id.pro_price);
        txtproductDescription=(TextView) itemView.findViewById(R.id.pro_description);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        listener.OnClick(v, getAdapterPosition(), false );
    }
}
