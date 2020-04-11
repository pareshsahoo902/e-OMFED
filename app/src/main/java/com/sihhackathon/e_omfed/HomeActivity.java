package com.sihhackathon.e_omfed;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sihhackathon.e_omfed.Model.Products;
import com.sihhackathon.e_omfed.Prevalent.Prevalent;
import com.sihhackathon.e_omfed.ViewHolder.ProductViewHolder;
import com.sihhackathon.e_omfed.ui.gallery.GalleryFragment;
import com.sihhackathon.e_omfed.ui.slideshow.SlideshowFragment;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
//    private Fragment cartfragment =new SlideshowFragment();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);



        View headerview =navigationView.getHeaderView(0);
        TextView usernameTextView = headerview.findViewById(R.id.username);
        CircleImageView profilepic = headerview.findViewById(R.id.ProfileImage);


        //String currentUser=getString(R.string.nav_header_title);
        usernameTextView.setText(Prevalent.currentOnlineUsers.getName());


        Picasso.get().load(Prevalent.currentOnlineUsers.getProfilepic_URL()).placeholder(R.drawable.circle).into(profilepic);





        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if(destination.getId() ==R.id.nav_home)
                {
//                    Toast.makeText(HomeActivity.this, "working", Toast.LENGTH_LONG).show();
                }
                if(destination.getId() ==R.id.nav_gallery)
                {

  //                  Toast.makeText(HomeActivity.this, "working", Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(HomeActivity.this, GalleryFragment.class);
//                    startActivity(i);

                }

                if(destination.getId() ==R.id.nav_tools)
                {
                }

                if(destination.getId() ==R.id.nav_share)
                {

                }
                if(destination.getId() ==R.id.nav_slideshow)
                {
                }

                if(destination.getId() ==R.id.nav_send)
                {
                    Toast.makeText(HomeActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
                    Paper.book().destroy();
                    Intent i = new Intent(HomeActivity.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {

        if (item.getItemId()==R.id.action_search){

            startActivity(new Intent(HomeActivity.this,SearcProductActivity.class));


        }
        if (item.getItemId()==R.id.cart_icon){
            Toast.makeText(HomeActivity.this, "Settins menu here", Toast.LENGTH_LONG).show();
//            Fragment frag=new SlideshowFragment();

//            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, frag).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
