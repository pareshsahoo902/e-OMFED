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

    Fragment fragment = new AdminHomeFragment();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_bottom_adminaccount, container, false);

        close = (TextView) view.findViewById(R.id.close_admin);
        update = (TextView) view.findViewById(R.id.update_admin);
        admin_phone = (EditText) view.findViewById(R.id.phone_update_text_admin);
        admin_fullname = (EditText) view.findViewById(R.id.FullName_update_admin);

        admin_profile_new_image = (CircleImageView) view.findViewById(R.id.Profile_update_image_admin);

        return view;
    }
}
