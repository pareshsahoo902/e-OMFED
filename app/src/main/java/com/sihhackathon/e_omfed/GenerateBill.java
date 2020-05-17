package com.sihhackathon.e_omfed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.BatchUpdateException;

public class GenerateBill extends AppCompatActivity {

    private String total_amount,Oid;
    private TextView name,address,subscription_id,amount,transacton_id,date,time;
    private Button save_invoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);
        name=(TextView)findViewById(R.id.billing_name);
        address=(TextView)findViewById(R.id.billing_adress);
        subscription_id=(TextView)findViewById(R.id.bill_subscription_id);
        amount=(TextView)findViewById(R.id.billingamount);
        transacton_id=(TextView)findViewById(R.id.bill_transaction_id);
        date=(TextView)findViewById(R.id.billing_date);
        time=(TextView)findViewById(R.id.billingtime);
        save_invoice=(Button)findViewById(R.id.save_invoice);

        total_amount= getIntent().getStringExtra("total price");
        Oid= getIntent().getStringExtra("order id");

        amount.setText("billing amount : ₹"+total_amount);

        save_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(GenerateBill.this,HomeActivity.class));
                Toast.makeText(GenerateBill.this, "file saved check orders", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
