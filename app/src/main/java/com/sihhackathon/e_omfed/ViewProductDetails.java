package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ViewProductDetails extends AppCompatActivity {

    private ElegantNumberButton elegantNumberButton;
    private Button add_cart;
    private TextView product_details_name,product_details_description,product_details_price;
    private ImageView product_detail_image;

//    private String productName,productDescription,productPrice ;

    private String productID="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product_details);

        productID=getIntent().getStringExtra("pid");


        elegantNumberButton=(ElegantNumberButton)findViewById(R.id.elegnant_button);
        add_cart=(Button)findViewById(R.id.product_cart_button);
        product_detail_image=(ImageView) findViewById(R.id.product_details_imageview);
        product_details_name=(TextView) findViewById(R.id.product_details_name);
        product_details_description=(TextView) findViewById(R.id.product_details_description);
        product_details_price=(TextView) findViewById(R.id.product_details_price);

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddingToCartList();

            }
        });


        GetProductdetails(productID);




    }

    private void AddingToCartList() {

        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
        String CurrentDate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        String CurrentTime = currenttime.format(calendar.getTime());

        final DatabaseReference cartRef=FirebaseDatabase.getInstance().getReference().child("Cart_List");

        final HashMap<String,Object> cartmap=new HashMap<>();

        cartmap.put("pid",productID);
        cartmap.put("pname",product_details_name.getText().toString());
        cartmap.put("price",product_details_price.getText().toString());
        cartmap.put("date",CurrentDate);
        cartmap.put("time",CurrentTime);
        cartmap.put("Discout","");
        cartmap.put("quantity",elegantNumberButton.getNumber());

        cartRef.child("user_view").child(Prevalent.currentOnlineUsers.getPhone_number())
                .child("PRODUCTS").child(productID)
                .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    cartRef.child("admin_view").child(Prevalent.currentOnlineUsers.getPhone_number())
                            .child("PRODUCTS").child(productID)
                            .updateChildren(cartmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ViewProductDetails.this, "added to cartlist", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(ViewProductDetails.this,HomeActivity.class);
                                startActivity(intent);

                            }

                        }
                    });
                }

            }
        });



    }

    private void GetProductdetails(String productID) {

        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("products");
        databaseReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Products products=dataSnapshot.getValue(Products.class);

                    product_details_name.setText(products.getProduct_name());
                    product_details_description.setText(products.getProduct_Description());
                    product_details_price.setText(products.getProduct_price());

                    Picasso.get().load(products.getImage_URL()).into(product_detail_image);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
