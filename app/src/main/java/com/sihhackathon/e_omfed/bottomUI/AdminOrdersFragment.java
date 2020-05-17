package com.sihhackathon.e_omfed.bottomUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.Model.Orders;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.R;
import com.sihhackathon.e_omfed.ViewHolder.OrderViewHolder;
import com.sihhackathon.e_omfed.ViewHolder.ProductViewHolder;
import com.sihhackathon.e_omfed.ViewOrderDetailsActivity;

import java.util.ArrayList;
import java.util.Objects;

public class AdminOrdersFragment extends Fragment {

    View orderView;

    private RecyclerView recyclerView;

    private DatabaseReference orderRef;
    private ArrayList<Orders> arrayList;
    private FirebaseRecyclerOptions<Orders> options;
    private FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        orderView=inflater.inflate(R.layout.fragment_bottom_orders, container,false);

        recyclerView=(RecyclerView)orderView.findViewById(R.id.recycler_admin_orders_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayList=new ArrayList<Orders>();

        return orderView;

    }

    @Override
    public void onStart() {
        super.onStart();

        orderRef= FirebaseDatabase.getInstance().getReference().child("orders");

        options=new FirebaseRecyclerOptions.Builder<Orders>().setQuery(orderRef ,Orders.class).build();

        adapter=new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, final int i, @NonNull final Orders orders) {

                orderViewHolder.oreder_username.setText("Name: "+orders.getShipment_Name());
                orderViewHolder.oreder_contact.setText("Contact: "+orders.getShipment_Contact());
                orderViewHolder.oreder_cityname.setText(orders.getShipment_Cityname());
                orderViewHolder.oreder_pincode.setText("Pincode: "+orders.getShipment_Pincode());
                orderViewHolder.oreder_Date.setText("Date: "+orders.getDate());
                orderViewHolder.oreder_total_price.setText("Total. ₹ "+orders.getTotal_price());

                orderViewHolder.viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uId=getRef(i).getKey();
                        startActivity(new Intent(getActivity(), ViewOrderDetailsActivity.class).putExtra("uid",uId).putExtra("oid", orders.getOrder_id()));
                    }
                });


            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new OrderViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.activity_order_list,parent,false));
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.startListening();

    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
