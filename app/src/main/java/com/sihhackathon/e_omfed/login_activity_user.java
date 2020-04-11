package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sihhackathon.e_omfed.Model.Users;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;

import io.paperdb.Paper;

public class login_activity_user extends AppCompatActivity {

    private Button login_btn;
    private CheckBox remember_me;
    private EditText phone_number,login_password;
    private TextView forgot_password,admin,not_admin;
    private String ParentDbName="users";

    ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        phone_number=(EditText)findViewById(R.id.phone_number);
        login_password=(EditText)findViewById(R.id.login_password_user);
        remember_me=(CheckBox)findViewById(R.id.remember_chbx);
        admin=(TextView)findViewById(R.id.admin);
        forgot_password=(TextView)findViewById(R.id.forgot_password);
        not_admin=(TextView)findViewById(R.id.not_admin);
        login_btn=(Button)findViewById(R.id.login_btn);

        loadingbar=new ProgressDialog(this);

        not_admin.setVisibility(View.INVISIBLE);

        Paper.init(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // USERS LOGIN IS TAKEN PLACE...

                LoginUser();

            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_btn.setText("Admin Login");
                admin.setVisibility(View.INVISIBLE );
                not_admin.setVisibility(View.VISIBLE);
                ParentDbName="admins";
            }
        });


        not_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login_btn.setText("Login");
                admin.setVisibility(View.VISIBLE );
                not_admin.setVisibility(View.INVISIBLE);
                ParentDbName="users";
            }
        });





    }

    private void LoginUser() {

        String phone= phone_number.getText().toString().trim();
        String password= login_password.getText().toString().trim();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(getApplicationContext(),"enter phone number",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(),"enter password",Toast.LENGTH_SHORT).show();
        }
        else
        {


            loadingbar.setTitle("Logging In");
            loadingbar.setMessage("please wait...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            AccessAccount(phone,password);

        }


    }

    private void AccessAccount(final String phone, final String password) {

        if(remember_me.isChecked())
        {

            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordkey, password);
//            Paper.book().write(Prevalent.AccountTypekey, ParentDbName);

        }


        final DatabaseReference databaseReference ;
        databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(ParentDbName).child(phone).exists()){
                    Users userdata = dataSnapshot.child(ParentDbName).child(phone).getValue(Users.class);


//                    Toast.makeText(getApplicationContext(),userdata.getPhone_number(),Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getApplicationContext(),userdata.getPassword(),Toast.LENGTH_SHORT).show();

                    if(userdata.getPhone_number().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {

                            if (ParentDbName.equals("admins"))
                            {
                                Toast.makeText(getApplicationContext(),ParentDbName,Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();

                                Intent i = new Intent(login_activity_user.this,AdminHomeActivity.class);
                                startActivity(i);
                            }
                            else if(ParentDbName.equals("users"))
                            {

                                Toast.makeText(getApplicationContext(),"logged in sucessful..",Toast.LENGTH_SHORT).show();
                                Prevalent.currentOnlineUsers=userdata;
                                loadingbar.dismiss();

                                Intent i = new Intent(login_activity_user.this,HomeActivity.class);
                                startActivity(i);
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"wrong password..",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"wrong phoneno...",Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }

                }
                else{

                    Toast.makeText(getApplicationContext(),"phone number doesnt exists",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
