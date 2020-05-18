package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sihhackathon.e_omfed.Model.Users;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button login,join;
    private ImageView logo;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login=(Button)findViewById(R.id.login);
        join=(Button)findViewById(R.id.join_now);
        logo =(ImageView)findViewById(R.id.applogo);
        loadingbar = new ProgressDialog(this);

        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,login_activity_user.class);
                startActivity(i);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register_user.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordkey);

        if(UserPhoneKey != ""  &&  UserPasswordKey != "")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey) )
            {
                AloowAccess(UserPhoneKey,UserPasswordKey);

                loadingbar.setTitle("Redirecting");
                loadingbar.setMessage("please wait...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

            }
        }

    }

    private void AloowAccess(final String phone, final String password) {

        final DatabaseReference databaseReference ;
        databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("users").child(phone).exists())
                {
                    Users userdata = dataSnapshot.child("users").child(phone).getValue(Users.class);

                    if(userdata.getPhone_number().equals(phone))
                    {
                        if(userdata.getPassword().equals(password))
                        {
                            Toast.makeText(getApplicationContext(),"logged in sucessful..",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();

                            Intent i = new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentOnlineUsers=userdata;
                            startActivity(i);
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
