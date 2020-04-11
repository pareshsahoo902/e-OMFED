package com.sihhackathon.e_omfed.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sihhackathon.e_omfed.R;

public class OrderViewHolder extends RecyclerView.ViewHolder{

    public TextView oreder_username, oreder_contact,oreder_cityname, oreder_pincode, oreder_total_price,oreder_Date;
    public Button viewDetails;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        oreder_username=(TextView)itemView.findViewById(R.id.order_name);
        oreder_total_price=(TextView)itemView.findViewById(R.id.order_price);
        oreder_cityname=(TextView)itemView.findViewById(R.id.order_cityname);
        oreder_contact=(TextView)itemView.findViewById(R.id.order_contact);
        oreder_Date=(TextView)itemView.findViewById(R.id.order_date);
        oreder_pincode=(TextView)itemView.findViewById(R.id.order_pincode);


        viewDetails=(Button)itemView.findViewById(R.id.order_view_btn);



    }
}
