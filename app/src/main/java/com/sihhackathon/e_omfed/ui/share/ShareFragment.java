package com.sihhackathon.e_omfed.ui.share;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sihhackathon.e_omfed.AdminActivity;
import com.sihhackathon.e_omfed.AdminCategoryActivity;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.R;
import com.sihhackathon.e_omfed.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    View settings;

    private TextView close, update;

    private EditText user_phone, user_fullname;
    private Uri imageuri;

    private CircleImageView profile_new_image;
//    private ProgressDialog loadingbar;
    private static final int GALLERYPICK = 1;

    private String myUrl = "", checker = "";
    private StorageTask uploadTask;
    private StorageReference CustomerImageRef;
    private DatabaseReference databaseReference;
    Fragment fragment=new HomeFragment();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        settings = inflater.inflate(R.layout.fragment_share, container, false);


        close = (TextView) settings.findViewById(R.id.close);
        update = (TextView) settings.findViewById(R.id.update);
        user_phone = (EditText) settings.findViewById(R.id.phone_update_text);
        user_fullname = (EditText) settings.findViewById(R.id.FullName_update);

        profile_new_image = (CircleImageView) settings.findViewById(R.id.Profile_update_image);


        CustomerImageRef = FirebaseStorage.getInstance().getReference().child("Customer Profile pic");

        UserInfoDisplay(profile_new_image, user_phone, user_fullname);


        profile_new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "upload image here", Toast.LENGTH_SHORT).show();
                checker = "clicked";

//                CropImage.activity(imageuri)
//                        .setAspectRatio(1, 1).start(getActivity());


                openGallery();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//

                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();



            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateCustomerInfo();

                if (checker.equals("clicked")) {
                    ValidateCustomerInfo();


                }
                else {
                    UpdateOnlyUserInfo();

                }


            }
        });


        return settings;
    }

    private void openGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERYPICK);


    }


    private void UpdateOnlyUserInfo()
    {
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference().child("users");

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Contact_no",user_phone.getText().toString());
        hashMap.put("name",user_fullname.getText().toString().trim());

        reference.child(Prevalent.currentOnlineUsers.getPhone_number()).updateChildren(hashMap);

        Toast.makeText(getContext(), "User info Updated", Toast.LENGTH_SHORT).show();



        startActivity(new Intent(getContext(),HomeFragment.class));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERYPICK && resultCode== RESULT_OK && data != null)
        {
            imageuri=data.getData();
            profile_new_image.setImageURI(imageuri);

        }
         else {
            Toast.makeText(getContext(), "error try again", Toast.LENGTH_SHORT).show();

        }

    }

    private void UserInfoDisplay(final CircleImageView profile_new_image, final EditText user_phone, final EditText user_fullname) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUsers.getPhone_number());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("Profilepic_URL").exists()) {
                        String image = dataSnapshot.child("Profilepic_URL").getValue().toString();
                        String phone = dataSnapshot.child("phone_number").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();

                        Picasso.get().load(image).into(profile_new_image);
                        user_phone.setText(phone);
                        user_fullname.setText(name);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ValidateCustomerInfo() {

        if (TextUtils.isEmpty(user_phone.getText().toString())) {
            Toast.makeText(getContext(), "enter the contact.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(user_fullname.getText().toString())) {
            Toast.makeText(getContext(), "enter your full name.", Toast.LENGTH_SHORT).show();
        } else if (checker.equals("clicked")){
            UploadImage();

        }
    }

    private void UploadImage() {


//        loadingbar.setTitle("Updating");
//        loadingbar.setMessage("please wait.....");
//        loadingbar.setCanceledOnTouchOutside(false);
//        loadingbar.show();

        if(imageuri!=null)
        {
            final StorageReference filepath = CustomerImageRef.child(Prevalent.currentOnlineUsers.getPhone_number() + ".jpg");
            uploadTask = filepath.putFile(imageuri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }


                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl=downloadUrl.toString();

                        DatabaseReference reference =FirebaseDatabase.getInstance().getReference().child("users");

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("Contact_no",user_phone.getText().toString());
                        hashMap.put("name",user_fullname.getText().toString().trim());
                        hashMap.put("Profilepic_URL",myUrl);

                        reference.child(Prevalent.currentOnlineUsers.getPhone_number()).updateChildren(hashMap);

//                        loadingbar.dismiss();

                        Toast.makeText(getContext(), "Uploaded data sucessfully", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getActivity(),HomeFragment.class));


                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();


                    }
                    else
                    {
//                        loadingbar.dismiss();
                        Toast.makeText(getContext(), "error try again", Toast.LENGTH_SHORT).show();


                    }

                }
            });

        }
        else
        {
            Toast.makeText(getContext(), "image is not selected", Toast.LENGTH_SHORT).show();

        }

    }


}


//

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==GALLERYPICK && resultCode== RESULT_OK && data != null)
//        {
//            imageuri=data.getData();
//            profile_new_image.setImageURI(imageuri);
//
//        }
//    }
//
//
//

//
//    private void UpdateCustomerInfo() {
//
//

//
//
//
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                String meassage = e.toString();
//                Toast.makeText(getContext(), "Error: " + meassage, Toast.LENGTH_SHORT).show();
//                loadingbar.dismiss();
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                Toast.makeText(getContext(), "Image uploaded succesfully", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                        if (!task.isSuccessful()) {
//                            throw task.getException();
//                        }
//
//
//                        downloadImageUrl = filepath.getDownloadUrl().toString();
//                        return filepath.getDownloadUrl();
//
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//
//                        downloadImageUrl = task.getResult().toString();
//
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(), "getting the Url success", Toast.LENGTH_SHORT).show();
//
//                            SaveUserInfoToDatabase();
//                        }
//
//                    }
//                });
//
//
//            }
//        });
//
//
//    }
//
//    private void SaveUserInfoToDatabase()
//    {
//

//
//
//        databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                if(task.isSuccessful())
//                {
//
//                    Intent i = new Intent(getContext(), HomeFragment.class);
//                    startActivity(i);
//
//
//                    loadingbar.dismiss();
//                    Toast.makeText(getContext(), "Account updated", Toast.LENGTH_SHORT).show();
//
//
//                }
//                else
//                {
//                    String message =task.getException().toString();
//                    Toast.makeText(getContext(), "Error:"+message, Toast.LENGTH_SHORT).show();
//                    loadingbar.dismiss();
//                }
//
//            }
//        });
//
//
//    }





