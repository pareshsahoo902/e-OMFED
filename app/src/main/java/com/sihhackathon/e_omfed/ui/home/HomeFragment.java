package com.sihhackathon.e_omfed.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.HomeActivity;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.R;
import com.sihhackathon.e_omfed.ViewHolder.ProductViewHolder;
import com.sihhackathon.e_omfed.ViewProductDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    View productView;

    private HomeViewModel homeViewModel;
    private DatabaseReference ProductRef;

    private RecyclerView recyclerView;

    private ArrayList<Products> arrayList;
    private FirebaseRecyclerOptions<Products> options;
    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        productView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=(RecyclerView)productView.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        arrayList=new ArrayList<Products>();


        return productView;
    }


    @Override
    public void onStart() {
        super.onStart();

        ProductRef= FirebaseDatabase.getInstance().getReference().child("products");
        ProductRef.keepSynced(true);

        options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductRef ,Products.class).build();


        adapter= new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {

                productViewHolder.Productname.setText(products.getProduct_name());
                productViewHolder.Productdescription.setText(products.getProduct_Description());
                productViewHolder.Productprice.setText("₹ " +products.getProduct_price());

                Picasso.get().load(products.getImage_URL()).into(productViewHolder.product_image);

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), ViewProductDetails.class).putExtra("pid", products.getPid());
                        startActivity(i);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProductViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.list_layout,parent,false));
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