package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearcProductActivity extends AppCompatActivity {

    private EditText search_text;
    private ImageButton search;

    private RecyclerView recyclerView;
    private String searchInput;


    private ArrayList<Products> arrayList;
    private FirebaseRecyclerOptions<Products> options;
    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searc_product);

        search=(ImageButton)findViewById(R.id.search_btn);
        search_text=(EditText)findViewById(R.id.search_text_edttxt);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList=new ArrayList<Products>();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchInput=search_text.getText().toString();

                onStart();


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("products");


        options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(databaseReference.orderByChild("Product_name").startAt(searchInput) ,Products.class).build();

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
                        Intent i = new Intent(SearcProductActivity.this, ViewProductDetails.class).putExtra("pid", products.getPid());
                        startActivity(i);
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout,parent,false));
            }
        };

        recyclerView.setAdapter(adapter);

        adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
