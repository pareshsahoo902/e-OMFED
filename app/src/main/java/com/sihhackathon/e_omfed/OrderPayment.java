package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.JavaMailAPI.JavaMailAPI;
import com.sihhackathon.e_omfed.Model.Cart;
import com.sihhackathon.e_omfed.Model.Orders;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.ViewHolder.CartViewHolder;

import java.util.ArrayList;

public class OrderPayment extends AppCompatActivity {

    private TextView id,price,email_text;
    private Button UPIpay;
    private RecyclerView recyclerView;
    private String total_amount,Oid;
    private EditText email_id;

    private ArrayList<Products> arrayList;
    private FirebaseRecyclerOptions<Cart> options;
    private FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;

    final int UPI_PAYMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS}, PackageManager.PERMISSION_GRANTED);


        id=(TextView)findViewById(R.id.order_id);
        price=(TextView)findViewById(R.id.order_total_payment);
        email_text=(TextView)findViewById(R.id.email_text);
        email_id=(EditText)findViewById(R.id.email_id);
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
//                startActivity(new Intent(OrderPayment.this,GenerateBill.class).putExtra("total price",String.valueOf(total_amount)).putExtra("order id",String.valueOf(Oid)));

//                String title_msg="Invoice Copy ";
//                String text_msg="You have paid a amount of ₹"+total_amount+" for your monthly subscription . Thanks for using E-dairy. order id"+
//                        Oid+". For any issue contact us in www.e-dairy.com ";
//////
////                Intent intent=new Intent(getApplicationContext(),GenerateBill.class);
////                PendingIntent pi= PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
//                SmsManager sms= SmsManager.getDefault();
//                sms.sendTextMessage(title_msg, null, text_msg, null,null);

                senEmail();




                String amount = total_amount;
                String note = "payment for subscription_id :"+Oid;
                String name = "PARESH KUMAR SAHOO";
                String upiId = "8917351934@ybl";
//                payUsingUpi(amount, upiId, name, note);

                Toast.makeText(OrderPayment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OrderPayment.this,GenerateBill.class).putExtra("total price",String.valueOf(total_amount)).putExtra("order id",String.valueOf(Oid)));





            }
        });


    }

    void payUsingUpi(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(OrderPayment.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(OrderPayment.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(OrderPayment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(OrderPayment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(OrderPayment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }






    private void senEmail() {

        String email=email_id.getText().toString().trim();
        String subject="Invoice Copy ";
        String message="You have paid a amount of ₹"+total_amount+" for your monthly subscription . Thanks for using E-dairy. order id"+
                Oid+". For any issue contact us in www.e-dairy.com ";

        JavaMailAPI javaMailAPI=new JavaMailAPI(this,email,subject,message);
        javaMailAPI.execute();



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
