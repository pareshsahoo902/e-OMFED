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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.Model.Cart;
import com.sihhackathon.e_omfed.Model.Orders;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.ViewHolder.CartViewHolder;

import java.util.ArrayList;

public class OrderPayment extends AppCompatActivity {

    private TextView id,price;
    private Button UPIpay;
    private RecyclerView recyclerView;
    private String total_amount,Oid;

    private ArrayList<Products> arrayList;
    private FirebaseRecyclerOptions<Cart> options;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        id=(TextView)findViewById(R.id.order_id);
        price=(TextView)findViewById(R.id.order_total_payment);
        UPIpay=(Button)findViewById(R.id.Upi_btn);

        total_amount= getIntent().getStringExtra("total price");
        Oid= getIntent().getStringExtra("order id");

        price.setText("billing amount : ₹"+total_amount);
        id.setText("order id : "+Oid);
        UPIpay.setText("pay ₹"+total_amount+" with UPI");

        recyclerView=(RecyclerView)findViewById(R.id.payment_recycler_item);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList=new ArrayList<Products>();


        UPIpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderPayment.this, "start transaction", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OrderPayment.this,GenerateBill.class).putExtra("total price",String.valueOf(total_amount)).putExtra("order id",String.valueOf(Oid)));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartlistREf= FirebaseDatabase.getInstance().getReference().child("Cart_List");
        cartlistREf.keepSynced(true);

        options=new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartlistREf.child("user_view")
                .child(Prevalent.currentOnlineUsers.getPhone_number()).child("PRODUCTS"), Cart.class).build();

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
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
