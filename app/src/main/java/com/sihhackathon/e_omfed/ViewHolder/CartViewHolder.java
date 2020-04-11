package com.sihhackathon.e_omfed.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sihhackathon.e_omfed.Interface.ItemClickListner;
import com.sihhackathon.e_omfed.R;

public class CartViewHolder extends RecyclerView.ViewHolder {
    public TextView txtproduct_name,txtproduct_quantity,txtproduct_price;
    public ItemClickListner cart_listner;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtproduct_name=(TextView) itemView.findViewById(R.id.productcartname);
        txtproduct_quantity=(TextView) itemView.findViewById(R.id.productcartquantity);
        txtproduct_price=(TextView) itemView.findViewById(R.id.productcartprice);


    }
}
