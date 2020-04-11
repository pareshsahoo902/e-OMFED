package com.sihhackathon.e_omfed.ui.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.Model.Cart;
import com.sihhackathon.e_omfed.Model.Orders;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.R;
import com.sihhackathon.e_omfed.ViewHolder.CartViewHolder;
import com.sihhackathon.e_omfed.ViewHolder.OrderViewHolder;
import com.sihhackathon.e_omfed.ViewOrderDetailsActivity;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;


    private RecyclerView recyclerView;

    private DatabaseReference orderRef;
    private ArrayList<Orders> arrayList;
    private FirebaseRecyclerOptions<Orders> options;
    private FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);


        recyclerView=(RecyclerView)root.findViewById(R.id.recycler_order_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayList=new ArrayList<Orders>();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        orderRef= FirebaseDatabase.getInstance().getReference().child("orders").child("users_view").child(Prevalent.currentOnlineUsers.getPhone_number());

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

                orderViewHolder.viewDetails.setText(orders.getState());

                orderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence Options[] =new CharSequence[]
                                {
                                        "View Details",
                                        "Cancel Order"
                                };

                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setTitle("Manage Orders");

                        builder.setItems(Options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0)
                                {
                                    Toast.makeText(getContext(), "view product details", Toast.LENGTH_SHORT).show();



                                }
                                if (which==1)
                                {

                                    orderRef.child(orders.getOrder_id()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(getActivity(), "order removed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                            }
                        });

                        builder.show();
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
    }
}