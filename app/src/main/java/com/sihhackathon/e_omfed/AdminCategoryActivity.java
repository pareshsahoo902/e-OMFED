package com.sihhackathon.e_omfed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView milk,ghee,curd,butter,panner,ice_cream;
    private TextView category_txt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        milk=(ImageView)findViewById(R.id.img_milk);
        curd=(ImageView)findViewById(R.id.img_curd);
        ghee=(ImageView)findViewById(R.id.img_ghee);
        panner=(ImageView)findViewById(R.id.img_panner);
        ice_cream=(ImageView)findViewById(R.id.img_icecream);
        butter=(ImageView)findViewById(R.id.img_butter);


        milk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminCategoryActivity.this,AdminActivity.class);
                i.putExtra("Category","milk");
                startActivity(i);

            }
        });

         curd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminCategoryActivity.this,AdminActivity.class);
                i.putExtra("Category","curd");
                startActivity(i);

            }
        });

         ghee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminCategoryActivity.this,AdminActivity.class);
                i.putExtra("Category","ghee");
                startActivity(i);

            }
        });

         butter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminCategoryActivity.this,AdminActivity.class);
                i.putExtra("Category","butter");
                startActivity(i);

            }
        });

         ice_cream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminCategoryActivity.this,AdminActivity.class);
                i.putExtra("Category","ice_cream");
                startActivity(i);

            }
        });

         panner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminCategoryActivity.this,AdminActivity.class);
                i.putExtra("Category","panner");
                startActivity(i);

            }
        });




    }
}
