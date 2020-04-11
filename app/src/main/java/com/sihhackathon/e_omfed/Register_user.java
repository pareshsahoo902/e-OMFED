package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register_user extends AppCompatActivity {

    private EditText input_name,input_phone,input_password,input_pincode,input_addres;
    private Button create_account;
    private ImageView register_applgo;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        input_name=(EditText)findViewById(R.id.input_name);
        input_phone=(EditText)findViewById(R.id.input_phone_number);
        input_password=(EditText)findViewById(R.id.input_password);
        input_pincode=(EditText)findViewById(R.id.input_pincode);
        input_addres=(EditText)findViewById(R.id.input_Address);
        create_account=(Button) findViewById(R.id.create_account_btn);
        register_applgo=(ImageView)findViewById(R.id.register_applogo);
        loadingbar = new ProgressDialog(this);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_SHORT).show();
                createAccount();


            }
        });
    }

    private void createAccount()
    {
        String name= input_name.getText().toString().trim();
        String phone= input_phone.getText().toString().trim();
        String password= input_password.getText().toString().trim();
        String pincode= input_pincode.getText().toString().trim();
        String Addres= input_addres.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(getApplicationContext(),"enter name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(getApplicationContext(),"enter phone number",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pincode))
        {
            Toast.makeText(getApplicationContext(),"enter PINCODE",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Addres))
        {
            Toast.makeText(getApplicationContext(),"enter ADDRESS",Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("please wait..");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            validatePhoneNumber(name,phone,password,pincode, Addres);
        }
    }

    private void validatePhoneNumber(final String name, final String phone, final String password, final String pincode, final String Addres)
    {
        final DatabaseReference databaseReference ;
        databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.child("users").child(phone).exists())
                {

                    HashMap<String, Object> hashMap= new HashMap<>();
                    hashMap.put("name",name);
                    hashMap.put("phone_number",phone);
                    hashMap.put("password",password);
                    hashMap.put("pincode",pincode);
                    hashMap.put("Address",Addres);

                    databaseReference.child("users").child(phone).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();


                                Intent i = new Intent(Register_user.this,login_activity_user.class);
                                startActivity(i);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Error please try again",Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"the number"+phone+"already exists",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(getApplicationContext(),"please try again",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(Register_user.this,MainActivity.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
