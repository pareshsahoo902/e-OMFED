package com.sihhackathon.e_omfed.bottomUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sihhackathon.e_omfed.AdminActivity;
import com.sihhackathon.e_omfed.AdminCategoryActivity;
import com.sihhackathon.e_omfed.R;

public class AdminAddProductFragment extends Fragment {



    private ImageView milk,ghee,curd,butter,panner,ice_cream;
    private TextView category_txt;
    View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_bottom_addproduct, container,false);


        milk=(ImageView) view.findViewById(R.id.img_milk);
        curd=(ImageView) view.findViewById(R.id.img_curd);
        ghee=(ImageView) view.findViewById(R.id.img_ghee);
        panner=(ImageView) view.findViewById(R.id.img_panner);
        ice_cream=(ImageView) view.findViewById(R.id.img_icecream);
        butter=(ImageView) view.findViewById(R.id.img_butter);

        category_txt=(TextView)view.findViewById(R.id.catagory_admin_text);


        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(), AdminActivity.class);
                i.putExtra("Category","milk");
                startActivity(i);
            }
        });

        curd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),AdminActivity.class);
                i.putExtra("Category","curd");
                startActivity(i);

            }
        });

        ghee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),AdminActivity.class);
                i.putExtra("Category","ghee");
                startActivity(i);

            }
        });

        butter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),AdminActivity.class);
                i.putExtra("Category","butter");
                startActivity(i);

            }
        });

        ice_cream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),AdminActivity.class);
                i.putExtra("Category","ice_cream");
                startActivity(i);

            }
        });

        panner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity(),AdminActivity.class);
                i.putExtra("Category","panner");
                startActivity(i);

            }
        });

        return view;

    }
}
