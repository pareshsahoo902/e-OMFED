package com.sihhackathon.e_omfed.bottomUI;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.sihhackathon.e_omfed.AdminHomeActivity;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.R;
import com.sihhackathon.e_omfed.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AdminAccountFragment extends Fragment {
    View view;
    private TextView close, update;

    private EditText admin_phone, admin_fullname;
    private Uri imageuri;

    private CircleImageView admin_profile_new_image;
    //    private ProgressDialog loadingbar;
    private static final int GALLERYPICK = 1;

    private String myUrl = "", checker = "";
    private StorageTask uploadTask;
    private StorageReference CustomerImageRef;
    private DatabaseReference databaseReference;

    Fragment fragment=new AdminHomeFragment();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_bottom_adminaccount, container, false);

        close = (TextView) view.findViewById(R.id.close_admin);
        update = (TextView) view.findViewById(R.id.update_admin);
        admin_phone= (EditText) view.findViewById(R.id.phone_update_text_admin);
        admin_fullname = (EditText) view.findViewById(R.id.FullName_update_admin);

        admin_profile_new_image = (CircleImageView) view.findViewById(R.id.Profile_update_image_admin);

        CustomerImageRef = FirebaseStorage.getInstance().getReference().child("Admin Profile pic");

        UserInfoDisplay(admin_profile_new_image, admin_phone, admin_fullname);


        admin_profile_new_image.setOnClickListener(new View.OnClickListener() {
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

                startActivity(new Intent(getActivity(), AdminHomeActivity.class));

//                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();



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



        return view;

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("admins");

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Contact_no",admin_phone.getText().toString());
        hashMap.put("name",admin_fullname.getText().toString().trim());

        reference.child(Prevalent.currentOnlineAdmins.getPhone_number()).updateChildren(hashMap);

        Toast.makeText(getContext(), "Admin info Updated", Toast.LENGTH_SHORT).show();



        startActivity(new Intent(getActivity(), AdminHomeActivity.class));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERYPICK && resultCode== RESULT_OK && data != null)
        {
            imageuri=data.getData();
            admin_profile_new_image.setImageURI(imageuri);

        }
        else {
            Toast.makeText(getContext(), "error try again", Toast.LENGTH_SHORT).show();

        }

    }

    private void UserInfoDisplay(final CircleImageView admin_profile_new_image, final EditText admin_fullname, final EditText admin_phone) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("admins").child(Prevalent.currentOnlineAdmins.getPhone_number());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("Profilepic_URL").exists()) {
                        String image = dataSnapshot.child("Profilepic_URL").getValue().toString();
                        String phone = dataSnapshot.child("phone_number").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();

                        Picasso.get().load(image).into(admin_profile_new_image);
                        admin_phone.setText(phone);
                        admin_fullname.setText(name);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ValidateCustomerInfo() {

        if (TextUtils.isEmpty(admin_phone.getText().toString())) {
            Toast.makeText(getContext(), "enter the contact.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(admin_fullname.getText().toString())) {
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
            final StorageReference filepath = CustomerImageRef.child(Prevalent.currentOnlineAdmins.getPhone_number() + ".jpg");
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
                        hashMap.put("Contact_no",admin_phone.getText().toString());
                        hashMap.put("name",admin_fullname.getText().toString().trim());
                        hashMap.put("Profilepic_URL",myUrl);

                        reference.child(Prevalent.currentOnlineAdmins.getPhone_number()).updateChildren(hashMap);

//                        loadingbar.dismiss();

                        Toast.makeText(getContext(), "Uploaded data sucessfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(),AdminHomeActivity.class));


//                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();


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

