package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class ConfirmOrderAtivity extends AppCompatActivity {

    TextView shipment_details_text;
    EditText shipment_name,shipment_Address,shipment_Contact,shipment_cityname,shipment_pincode;
    Button deliver_here;
    private String saveCurrentDate, saveCurrentTime,total_amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order_ativity);

        total_amount= getIntent().getStringExtra("Total Price");

        shipment_details_text =(TextView)findViewById(R.id.shipmentText);
        shipment_name =(EditText) findViewById(R.id.shipment_name);
        shipment_Address =(EditText)findViewById(R.id.shipment_addres);
        shipment_Contact =(EditText)findViewById(R.id.shipment_number);
        shipment_cityname =(EditText)findViewById(R.id.shipment_cityname);
        shipment_pincode =(EditText)findViewById(R.id.shipment_pincode);
        deliver_here =(Button) findViewById(R.id.deliver_here);


        Calendar calendar= Calendar.getInstance();



        SimpleDateFormat currentdate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currenttime.format(calendar.getTime());

        deliver_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkShipmentDetails();
            }
        });


    }

    private void checkShipmentDetails() {


        if(TextUtils.isEmpty(shipment_name.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"enter name",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(shipment_Contact.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"enter contact",Toast.LENGTH_SHORT).show();
        }

         else if(TextUtils.isEmpty(shipment_Address.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"enter address",Toast.LENGTH_SHORT).show();
        }

         else if(TextUtils.isEmpty(shipment_cityname.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"enter cityname",Toast.LENGTH_SHORT).show();
        }

         else if(TextUtils.isEmpty(shipment_pincode.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"enter pincode",Toast.LENGTH_SHORT).show();
        }

         else {
             ConfirmUserOrder();
        }

    }

    private void ConfirmUserOrder()
    {

        final UUID uuid=UUID.randomUUID();
        final String uid = uuid.toString();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child("users_view").child(Prevalent.currentOnlineUsers.getPhone_number()).child(uid);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("total_price",total_amount);
        hashMap.put("Shipment_Name",shipment_name.getText().toString());
        hashMap.put("Shipment_Address",shipment_Address.getText().toString());
        hashMap.put("Shipment_Cityname",shipment_cityname.getText().toString());
        hashMap.put("Shipment_Pincode",shipment_pincode.getText().toString());
        hashMap.put("Shipment_Contact",shipment_Contact.getText().toString());
        hashMap.put("date",saveCurrentDate);
        hashMap.put("time",saveCurrentTime);
        hashMap.put("state","not approved");
        hashMap.put("order_id",uid);

        orderRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    DatabaseReference admin_order_ref = FirebaseDatabase.getInstance().getReference().child("orders").child("admins_view").child(Prevalent.currentOnlineUsers.getPhone_number());
                    HashMap<String,Object> hashMap1=new HashMap<>();
                    hashMap1.put("total_price",total_amount);
                    hashMap1.put("Shipment_Name",shipment_name.getText().toString());
                    hashMap1.put("Shipment_Address",shipment_Address.getText().toString());
                    hashMap1.put("Shipment_Cityname",shipment_cityname.getText().toString());
                    hashMap1.put("Shipment_Pincode",shipment_pincode.getText().toString());
                    hashMap1.put("Shipment_Contact",shipment_Contact.getText().toString());
                    hashMap1.put("date",saveCurrentDate);
                    hashMap1.put("time",saveCurrentTime);
                    hashMap1.put("state","not approved");
                    admin_order_ref.updateChildren(hashMap1);


                    FirebaseDatabase.getInstance().getReference().child("Cart_List").child("user_view")
                            .child(Prevalent.currentOnlineUsers.getPhone_number())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(ConfirmOrderAtivity.this, "order confirmed", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(ConfirmOrderAtivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));


                                    }

                                }
                            });
                }

            }
        });






    }
}
