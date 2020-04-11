package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sihhackathon.e_omfed.bottomUI.AdminHomeFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminManageProducts extends AppCompatActivity {

    private TextView textView1;
    private String productID;
    private ImageView product_img_manage;
    private EditText product_name_manage, product_desc_manage ,product_price_manage;
    private Button apply_changes_manage;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_products);


        textView1=(TextView)findViewById(R.id.text1) ;
        product_img_manage=(ImageView)findViewById(R.id.product_img_manage);
        product_name_manage=(EditText)findViewById(R.id.product_name_manage);
        product_desc_manage=(EditText)findViewById(R.id.product_desc_manage);
        product_price_manage=(EditText)findViewById(R.id.product_price_manage);
        apply_changes_manage=(Button)findViewById(R.id.applychanges_button_manage);

        productID=getIntent().getStringExtra("pid");

        productRef= FirebaseDatabase.getInstance().getReference().child("products").child(productID);

        DisplayInfoProduct();

        apply_changes_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyChanges();
            }
        });
    }

    private void ApplyChanges() {
        String productName=product_name_manage.getText().toString();
        String productDesc=product_desc_manage.getText().toString();
        String productPrice=product_price_manage.getText().toString();

        if (productName.equals(""))
        {
            Toast.makeText(AdminManageProducts.this, "enter product name", Toast.LENGTH_SHORT).show();
        }

        else if (productDesc.equals(""))
        {
            Toast.makeText(AdminManageProducts.this, "enter product description", Toast.LENGTH_SHORT).show();
        }

        else if (productPrice.equals(""))
        {
            Toast.makeText(AdminManageProducts.this, "enter product price", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("pid",productID);
            hashMap.put("Product_name",productName);
            hashMap.put("Product_Description",productDesc);
            hashMap.put("Product_price",productPrice);

            productRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(AdminManageProducts.this, "changes applied ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminManageProducts.this, AdminHomeActivity.class));
                        finish();
                    }
                }
            });
        }


    }

    private void DisplayInfoProduct()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String pName=dataSnapshot.child("Product_name").getValue().toString();
                    String pDesc=dataSnapshot.child("Product_Description").getValue().toString();
                    String pPrice=dataSnapshot.child("Product_price").getValue().toString();
//                    String pCtegory=dataSnapshot.child("Category").getValue().toString();
                    String pImage=dataSnapshot.child("Image_URL").getValue().toString();

                    product_name_manage.setText(pName);
                    product_desc_manage.setText(pDesc);
                    product_price_manage.setText(pPrice);
//                    textView1.setText(pCtegory);
                    Picasso.get().load(pImage).into(product_img_manage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
