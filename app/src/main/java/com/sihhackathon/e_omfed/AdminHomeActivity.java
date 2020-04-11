package com.sihhackathon.e_omfed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sihhackathon.e_omfed.bottomUI.AdminAccountFragment;
import com.sihhackathon.e_omfed.bottomUI.AdminAddProductFragment;
import com.sihhackathon.e_omfed.bottomUI.AdminHomeFragment;
import com.sihhackathon.e_omfed.bottomUI.AdminOrdersFragment;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        BottomNavigationView navView = findViewById(R.id.nav_view_bottom);
        navView.setOnNavigationItemSelectedListener(navlistner);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminHomeFragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment =null;


                    switch (menuItem.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment= new AdminHomeFragment();
                            break;
                        case R.id.navigation_add_product:
                            selectedFragment= new AdminAddProductFragment();
                            break;
                        case R.id.navigation_orders:
                            selectedFragment= new AdminOrdersFragment();
                            break;
                         case R.id.navigation_account:
                            selectedFragment= new AdminAccountFragment();
                            break;




                    }


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };


}
