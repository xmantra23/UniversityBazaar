package org.samir.universitybazaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

/**
 * @author Samir Shrestha
 * @description This class displays the home activity page after the user logs in.
 */

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        createToggleMenu();
        handleMenuClicks();
        loadMainFragment();

    }

    private void initViews() {
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
    }

    private void createToggleMenu(){
        //creating menu toggle bar
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void handleMenuClicks(){
        //setting onclick listeners for menu items.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.newPost:
                        // TODO: 2/18/2021 Redirect to new post activity
                        break;
                    case R.id.newClub:
                        // TODO: 2/18/2021 Redirect to new club activity
                        break;
                    case R.id.sellItem:
                        // TODO: 2/18/2021 Redirect to sell item activity
                        break;
                    case R.id.loanItem:
                        // TODO: 2/18/2021 Redirect to loan item activity
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void loadMainFragment() {
    }
}