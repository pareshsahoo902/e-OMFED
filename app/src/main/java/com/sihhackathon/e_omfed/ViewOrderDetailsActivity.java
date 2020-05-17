package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sihhackathon.e_omfed.Model.Cart;
import com.sihhackathon.e_omfed.Model.Orders;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.ViewHolder.CartViewHolder;
import com.sihhackathon.e_omfed.ViewHolder.ProductViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOrderDetailsActivity extends AppCompatActivity {

    private TextView product;
    private Button approve;
    private String uid;
    private RecyclerView recyclerView;
    private DatabaseReference cartListref;

    private ArrayList<Products> arrayList;
    private FirebaseRecyclerOptions<Cart> options;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_details);

        product=(TextView)findViewById(R.id.view_product_txt1);
        approve=(Button)findViewById(R.id.approve_btn);


        recyclerView=(RecyclerView)findViewById(R.id.recycler_view_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uid=getIntent().getStringExtra("uid");


        arrayList=new ArrayList<Products>();

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference verifyRefrence ;
                verifyRefrence= FirebaseDatabase.getInstance().getReference();

                verifyRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("orders").child(uid).exists())
                        {
                            Orders orderdata=dataSnapshot.child("orders").child(uid).getValue(Orders.class);
                            if(orderdata.getState().equals("not approved"))
                            {

                                HashMap<String, Object> hashMap= new HashMap<>();
                                hashMap.put("state","approved");
                                verifyRefrence.child("orders").child(uid)
                                        .updateChildren(hashMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful())
                                                {
//                                                    HashMap<String, Object> hashMapuser= new HashMap<>();
//                                                    hashMapuser.put("state","approved");
//                                                    verifyRefrence.child("orders").child("users_view")
//                                                            .updateChildren(hashMapuser);

                                                    Toast.makeText(ViewOrderDetailsActivity.this, "this order is approved", Toast.LENGTH_SHORT).show();
                                                    approve.setVisibility(View.GONE);
                                                }
                                                else
                                                {
                                                    Toast.makeText(ViewOrderDetailsActivity.this, "Error please try again", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            }
                            else{





                                Toast.makeText(ViewOrderDetailsActivity.this, "Already approved", Toast.LENGTH_SHORT).show();

                                approve.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        cartListref= FirebaseDatabase.getInstance().getReference().child("Cart_List").child("admin_view").child(uid)
        .child("PRODUCTS");
        cartListref.keepSynced(true);

        options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListref , Cart.class).build();

        adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                cartViewHolder.txtproduct_name.setText(cart.getPname());
                cartViewHolder.txtproduct_quantity.setText("Quantity= "+cart.getQuantity());
                cartViewHolder.txtproduct_price.setText("Price :₹"+cart.getPrice());

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false));

            }
        };
        recyclerView.setAdapter(adapter);

        adapter.startListening();






    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
