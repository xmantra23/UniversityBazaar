package org.samir.universitybazaar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Activity.MyClubsActivity;
import org.samir.universitybazaar.Activity.MySubscriptionsActivity;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Activity.MyPostsActivity;
import org.samir.universitybazaar.Profile.ViewProfileActivity;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

/**
 * @author Samir Shrestha
 * @Description This class is basically the main content of the HomeActivity. It has the main content in the middle and includes a bottom
 * navigation bar in the bottom of the fragment. The main content in the middle contains buttons to navigate to other activities,
 * whereas clicking on the bottom navigation icon should make the page stay on HomeActivity but only reload the middle fragment part with the
 * fragment corresponding to the bottom navigation icon that was pressed.
 */
public class HomeFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private ImageView manageClubArrow,manageSubArrow,managePostArrow,manageSaleArrow,managePurchaseArrow,manageLoanArrow;
    private TextView txtProfile,txtLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initViews(view);
        initBottomNavView(view);
        handleListeners(view);
        return view;
    }

    private void handleListeners(View view) {
        //navigate to myclubsactivity and show all clubs that the user has created.
        manageClubArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyClubsActivity.class);
            startActivity(intent);
        });

        //navigate to MySubscriptionsActivity and show all the clubs that the user is subscribed to.
        manageSubArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MySubscriptionsActivity.class);
            startActivity(intent);
        });

        //navigate to mypostsactivity and show all posts that the user has created.
        managePostArrow.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MyPostsActivity.class);
            startActivity(intent);
        });
        manageSaleArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle sale arrow pressed
        });
        managePurchaseArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle purchase arrow pressed
        });
        manageLoanArrow.setOnClickListener(v->{
            // TODO: 2/18/2021 handle loan arrow pressed
        });

        //profile button was pressed. navigate to ViewProfileActivity
        txtProfile.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
            startActivity(intent);
        });

        //logout button was pressed. Call the signOutUser() method in UserSession to destroy the session data about the current user.
        txtLogout.setOnClickListener(v->{
            UserSession userSession = new UserSession(getContext());
            userSession.signOutUser();
        });
    }

    private void initViews(View view) {
        bottomNavigationView = view.findViewById(R.id.bottomNavView);
        manageClubArrow = view.findViewById(R.id.manageClubArrow);
        manageSubArrow = view.findViewById(R.id.manageSubArrow);
        managePostArrow = view.findViewById(R.id.managePostArrow);
        manageSaleArrow = view.findViewById(R.id.manageSaleArrow);
        managePurchaseArrow = view.findViewById(R.id.managePurchaseArrow);
        manageLoanArrow = view.findViewById(R.id.manageLoanArrow);
        txtProfile = view.findViewById(R.id.txtProfile);
        txtLogout = view.findViewById(R.id.txtLogout);
    }

    //handles the bottom navigation view icon presses.
    private void initBottomNavView(View view) {
        bottomNavigationView.setSelectedItemId(R.id.home); //highlight the home icon in the bottom navigation view.

        //all cases will redirect to home activity but we are providing information about which icon was pressed before starting the intent.
        // for example if home icon is pressed navigate to home activity but also provide "home" as the activity name in the intent.
        //if group icon is pressed then navigate to home activity but send "group" as the activity name.
        //This will be used later in the HomeActivity to load the appropriate fragment in the body of the activity based on the activity name.
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            switch(item.getItemId()){
                case R.id.home:
                    //In homepage. No action required.
                    break;
                case R.id.post:
                    //Navigate to HomeActivity but send "post" as the activity name so that AllPostsFragment can be loaded into the HomeActivity.
                    intent.putExtra(Constants.ACTIVITY_NAME,"post");
                    startActivity(intent);
                    break;
                case R.id.group:
                    //Navigate to HomeActivity but send "group" as the activity name so that AllClubsFragment can be loaded into the HomeActivity.
                    intent.putExtra(Constants.ACTIVITY_NAME,"group");
                    startActivity(intent);
                    break;
                case R.id.market:
                    // TODO: 2/18/2021 redirect to the markeplace activity
                    break;
                default:
                    break;
            }
            return false;
        });
    }
}
