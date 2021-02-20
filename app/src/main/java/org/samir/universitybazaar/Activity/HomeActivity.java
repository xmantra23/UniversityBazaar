package org.samir.universitybazaar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Fragments.AllPostsFragment;
import org.samir.universitybazaar.Fragments.HomeFragment;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

/**
 * @author Samir Shrestha
 * @description This class displays the home activity page after the user logs in.
 */

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private UserSession session;
    private ProfileDAO dao;
    private ImageView headerImage;
    private TextView userName,userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews(); //initialize all the views.
        createToggleMenu(); //create a toggle menu.
        handleMenuClicks(); //initialize onclick listeners for all the menu items.
        loadHomeFragment(); //load the HomeFragment into HomeActivity.

        //populate collapsible toolbar header with user details.
        View headerLayout = navigationView.getHeaderView(0);
        headerImage = headerLayout.findViewById(R.id.headerImage);
        userName = headerLayout.findViewById(R.id.txtUserName);
        userEmail = headerLayout.findViewById(R.id.txtEmail);

        //getting the logged in user.
        session = new UserSession(this);
        User user = session.isUserLoggedIn();

        //only update the toolbar header if the user is logged in.
        if(user != null){
            dao = new ProfileDAO(this);
            String userAvatar = dao.getAvatar(user.getMemberId());//getting avatar string from database.
            Profile userProfile = dao.getProfile(user.getMemberId());
            if(userAvatar != null){ //only set image if user Avatar has been set.
                setImage(userAvatar);
            }
            if(userProfile.getFullName() != null) //user has set the full name.
                userName.setText(userProfile.getFullName());
            else //user hasn't provided the full name so use the member id instead.
                userName.setText(userProfile.getMemberId());
            userEmail.setText(userProfile.getEmail());
        }

    }

    private void initViews() {
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);


    }

    private void createToggleMenu(){
        //creating menu toggle bar in the top part.
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
                        Intent intent = new Intent(HomeActivity.this,CreatePostActivity.class);
                        startActivity(intent);
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

    private void loadHomeFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String activity_type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);
        if(activity_type == null){
            transaction.replace(R.id.container, new HomeFragment()); //container is defined as a fragment inside this activity.
        }else if(activity_type.equals("post")){
            transaction.replace(R.id.container, new AllPostsFragment()); //load all posts fragment in home activity.
        }else if(activity_type.equals("home")){
            transaction.replace(R.id.container, new HomeFragment()); //load home fragment in home activity.
        }


        transaction.commit();
    }

    //helper method for setting the image of the avatar. can move this to a utils class in the future.
    private void setImage(String avatar){
        switch (avatar){
            case "bear": headerImage.setImageResource(R.mipmap.bear);
                break;
            case "beauty": headerImage.setImageResource(R.mipmap.beauty);
                break;
            case "blue": headerImage.setImageResource(R.mipmap.blue);
                break;
            case "cool": headerImage.setImageResource(R.mipmap.cool);
                break;
            case "dog": headerImage.setImageResource(R.mipmap.dog);
                break;
            case "duck": headerImage.setImageResource(R.mipmap.duck);
                break;
            case "girl": headerImage.setImageResource(R.mipmap.girl);
                break;
            case "purple": headerImage.setImageResource(R.mipmap.purple);
                break;
            case "terrible": headerImage.setImageResource(R.mipmap.terrible);
                break;
            default:
                break;
        }
    }
}