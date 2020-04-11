package com.sihhackathon.e_omfed.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sihhackathon.e_omfed.Interface.ItemClickListner;
import com.sihhackathon.e_omfed.R;

public class ProductViewHolder extends RecyclerView.ViewHolder
{
    public TextView Productname,Productdescription,Productprice;
    public ImageView product_image;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView)
    {
        super(itemView);


        product_image=(ImageView) itemView.findViewById(R.id.productlist_image);
        Productname=(TextView) itemView.findViewById(R.id.product_name_text);
        Productdescription=(TextView) itemView.findViewById(R.id.product_description1);
        Productprice=(TextView) itemView.findViewById(R.id.product_price1);


    }
}
