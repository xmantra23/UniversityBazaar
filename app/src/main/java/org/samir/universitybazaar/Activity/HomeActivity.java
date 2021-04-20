package org.samir.universitybazaar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.samir.universitybazaar.Activity.Clubs.CreateClubActivity;
import org.samir.universitybazaar.Activity.Loan.LoanItemActivity;
import org.samir.universitybazaar.Activity.Posts.CreatePostActivity;
import org.samir.universitybazaar.Activity.Sale.SellItemActivity;
import org.samir.universitybazaar.Activity.Search.SearchClubsActivity;
import org.samir.universitybazaar.Activity.Search.SearchPostsActivity;
import org.samir.universitybazaar.Adapter.AdvertisementAdapter;
import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Fragments.AllClubsFragment;
import org.samir.universitybazaar.Fragments.AllItemFragment;
import org.samir.universitybazaar.Fragments.AllPostsFragment;
import org.samir.universitybazaar.Fragments.HomeFragment;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samir Shrestha
 * @description This class displays the home activity page after the user logs in.
 * The home activity page consists of a top collapsible menu, a fragment in the body and a bottom navigation bar.
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private UserSession session;
    private ProfileDAO dao;
    private ImageView headerImage;
    private TextView userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews(); //initialize all the views.
        createToggleMenu(); //create a toggle menu.
        handleMenuClicks(); //initialize onclick listeners for all the menu items.
        loadHomeFragment(); //load the fragment into HomeActivity.

        //populate collapsible toolbar header with user details. This is the little image icon and user details at the top of the navigation drawer.
        View headerLayout = navigationView.getHeaderView(0);
        headerImage = headerLayout.findViewById(R.id.headerImage);
        userName = headerLayout.findViewById(R.id.txtUserName);
        userEmail = headerLayout.findViewById(R.id.txtEmail);

        //getting the logged in user.
        session = new UserSession(this);
        User user = session.isUserLoggedIn();

        //only update the toolbar header if the user is logged in. If the user is not logged in default values are set.
        if (user != null) {
            dao = new ProfileDAO(this);
            String userAvatar = dao.getAvatar(user.getMemberId());//getting avatar name from the database.
            Profile userProfile = dao.getProfile(user.getMemberId()); //get the profile details for the current logged in user.
            if (userAvatar != null) { //only set image if user Avatar has been set i.e. the user has updated their profile with all the details.
                setImage(userAvatar);
            }
            if (userProfile.getFullName() != null) //user has set the full name.
                userName.setText(userProfile.getFullName());
            else //user hasn't provided the full name so display the member id instead.
                userName.setText(userProfile.getMemberId());
            userEmail.setText(userProfile.getEmail());//display the users email in the navigation drawer.
        }
    }

    //initializes all the views in the layout file.
    private void initViews() {
        drawer = findViewById(R.id.drawer); //this is the main layout of the HomeActivity. This kind of layout is needed for the toggle menu action.
        //this is the toggle menu. it contains a header part(navigation_drawer_header.xml) and a menu part(navigation_drawer_menu.xml)
        navigationView = findViewById(R.id.navigationView);
        //this toolbar replaces the default system provided toolbar. This toolbar will contain a hamburger icon, pressing which will release the above
        //navigation view which contains the header and the menu.
        toolbar = findViewById(R.id.toolbar);
    }

    private void createToggleMenu() {
        setSupportActionBar(toolbar); //make the user provided toolbar the top toolbar.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close); //this lets us toggle the menu.
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void handleMenuClicks() {
        //setting onclick listeners for the menu items in the drawer toolbar.
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newPost:
                        //Redirect to the create a new post activity. This lets users create a new post.
                        Intent intent = new Intent(HomeActivity.this, CreatePostActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.newClub:
                        // Redirect to the create a new club activity. This lets users create a new club.
                        Intent intent2 = new Intent(HomeActivity.this, CreateClubActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.sellItem:
                        Intent intent3 = new Intent(HomeActivity.this, SellItemActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.loanItem:
                        Intent intent4 = new Intent(HomeActivity.this, LoanItemActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.searchPosts:
                        //Redirect to SearchPostsActivity
                        Intent intent5 = new Intent(HomeActivity.this, SearchPostsActivity.class);
                        startActivity(intent5);
                        break;
                    case R.id.searchClubs:
                        //Redirect to SearchPostsActivity
                        Intent intent6 = new Intent(HomeActivity.this, SearchClubsActivity.class);
                        startActivity(intent6);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void loadHomeFragment() {
        //load a fragment in the HomePage body based on which button was pressed in the bottom navigation view.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //determine which icon was pressed in the bottom navigation bar.
        String activity_type = getIntent().getStringExtra(Constants.ACTIVITY_NAME);

        /*
         *load the correct fragment based on which icon was pressed. If the user has visited the homepage for the first time then
         * this will be null so load a HomeFragment but if the user has pressed on any of the icons in the bottom navigation view then
         * load the fragment corresponding to that icon in the body of the HomePage Activity.
         */
        if (activity_type == null) {
            transaction.replace(R.id.container, new HomeFragment()); //container is defined as a fragment inside this activity.
        } else if (activity_type.equals(Constants.POST)) {
            transaction.replace(R.id.container, new AllPostsFragment()); //load all posts fragment in home activity.
        } else if (activity_type.equals(Constants.HOME)) {
            transaction.replace(R.id.container, new HomeFragment()); //load home fragment in home activity.
        } else if (activity_type.equals(Constants.GROUP)) {
            transaction.replace(R.id.container, new AllClubsFragment()); //load all clubs/groups fragment in home activity.
        } else if (activity_type.equals(Constants.ITEM)) {
            transaction.replace(R.id.container, new AllItemFragment()); //load all clubs/groups fragment in home activity.
        }


        transaction.commit();//finalizes loading the fragment.
    }

    //helper method for setting the image of the avatar. can move this to a utils class in the future.
    private void setImage(String avatar) {
        switch (avatar) {
            case "bear":
                headerImage.setImageResource(R.mipmap.bear);
                break;
            case "beauty":
                headerImage.setImageResource(R.mipmap.beauty);
                break;
            case "blue":
                headerImage.setImageResource(R.mipmap.blue);
                break;
            case "cool":
                headerImage.setImageResource(R.mipmap.cool);
                break;
            case "dog":
                headerImage.setImageResource(R.mipmap.dog);
                break;
            case "duck":
                headerImage.setImageResource(R.mipmap.duck);
                break;
            case "girl":
                headerImage.setImageResource(R.mipmap.girl);
                break;
            case "purple":
                headerImage.setImageResource(R.mipmap.purple);
                break;
            case "terrible":
                headerImage.setImageResource(R.mipmap.terrible);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}