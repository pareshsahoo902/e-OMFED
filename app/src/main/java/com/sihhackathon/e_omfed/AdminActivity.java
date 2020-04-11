package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sihhackathon.e_omfed.bottomUI.AdminAddProductFragment;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class AdminActivity extends AppCompatActivity {

    private TextView textView;
    private String CategoryName;
    private ImageView product_img;
    private EditText product_name, product_desc ,product_price;
    private Button add_product;

    private String name, description ,price;
    private ProgressDialog loadingbar;
    private static final int GALLERYPICK = 1;
//    private String saveCurrentDate ,saveCurrentTime,  productRandomKey;
    private Uri imageuri;
    private String downloadImageUrl, pid;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        textView=(TextView)findViewById(R.id.text1) ;
        product_img=(ImageView)findViewById(R.id.product_img);
        product_name=(EditText)findViewById(R.id.product_name);
        product_desc=(EditText)findViewById(R.id.product_desc);
        product_price=(EditText)findViewById(R.id.product_price);
        add_product=(Button)findViewById(R.id.add_product_button);

        loadingbar= new ProgressDialog(this);

        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef= FirebaseDatabase.getInstance().getReference().child("products");

        CategoryName=getIntent().getExtras().getString("Category");
        textView.setText(CategoryName);

        product_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
            }
        });

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateProductData();
            }
        });



    }
    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERYPICK && resultCode== RESULT_OK && data != null)
        {
            imageuri=data.getData();
            product_img.setImageURI(imageuri);

        }
    }


    private void ValidateProductData()
    {
        name=product_name.getText().toString();
        description=product_desc.getText().toString();
        price=product_price.getText().toString();

        if(imageuri==null)
        {
            Toast.makeText(this, "product image mandatory", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "enter the product name.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "enter the product description.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "enter the product price.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }

    }

    private void StoreProductInformation()
    {


        loadingbar.setTitle("Adding the Product");
        loadingbar.setMessage("please wait..., while we add the product..");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

//        Calendar calendar= Calendar.getInstance();
//
//        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
//        saveCurrentDate = currentdate.format(calendar.getTime());
//
//        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
//        saveCurrentTime = currenttime.format(calendar.getTime());
//
//
//        productRandomKey = saveCurrentDate+saveCurrentTime;

        UUID uuid = UUID.randomUUID();
        pid=uuid.toString();

        final StorageReference filepath = ProductImageRef.child(imageuri.getLastPathSegment() + pid + ".jpg");
        final UploadTask uploadTask=filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String meassage = e.toString();
                Toast.makeText(AdminActivity.this, "Error: "+ meassage, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminActivity.this, "Image uploaded succesfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }


                        downloadImageUrl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        downloadImageUrl=task.getResult().toString();

                        if(task.isSuccessful())
                        {
                            Toast.makeText(AdminActivity.this, "getting the Url success", Toast.LENGTH_SHORT).show();

                            SaveproductInfoToDatabase();
                        }

                    }
                });


            }
        });

    }

    private void SaveproductInfoToDatabase() {

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("pid",pid);
        hashMap.put("Product_name",name);
        hashMap.put("Product_Description",description);
        hashMap.put("Product_price",price);
        hashMap.put("Image_URL",downloadImageUrl);
        hashMap.put("Category ",CategoryName);

        ProductRef.child(pid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Intent i = new Intent(AdminActivity.this, AdminHomeActivity.class);
                    startActivity(i);

                    loadingbar.dismiss();
                    Toast.makeText(getApplicationContext(), "Product added succesfully", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    String message =task.getException().toString();
                    Toast.makeText(AdminActivity.this, "Error:"+message, Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }
        });

    }
}
