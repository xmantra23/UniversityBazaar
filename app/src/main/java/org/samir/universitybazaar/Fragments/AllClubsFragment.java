package org.samir.universitybazaar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Adapter.ClubAdapter;
import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * //displays all the clubs in the system as a list inside the homeactivity page.
 */
public class AllClubsFragment extends Fragment {

    private BottomNavigationView bottomNavigationView; //The bottom navigation icons.
    private RecyclerView clubsRecView; //lists all the club items.
    private ClubAdapter adapter;
    private ClubDAO db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_clubs,container,false);
        initViews(view);
        initBottomNavView(view); //initialize and then handle the bottom icons.
        handleRecyclerView(view); //initialize and set all the data for the recycler view containing all the posts.
        return view;
    }

    private void handleRecyclerView(View view) {
        adapter = new ClubAdapter(getActivity()); //create a new ClubAdapter instance

        db = new ClubDAO(getActivity());

        ArrayList<Club> allClubs = db.getAllClubs(); //get all the clubs in the clubs table from the database.

        adapter.setClubs(allClubs); //initialize all the clubs for the adapter with the posts retrieved from the database.

        clubsRecView.setAdapter(adapter); //initialize the recycler view.
        //set linear layout with vertical orientation. reverseLayout is true means the most recent clubs is on the top of the list.
        clubsRecView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,true));
    }

    private void initViews(View view) {
        bottomNavigationView = view.findViewById(R.id.bottomNavView);
        clubsRecView = view.findViewById(R.id.clubsRecView);
    }

    //handles the bottom navigation view icon presses.
    private void initBottomNavView(View view) {
        bottomNavigationView.setSelectedItemId(R.id.group); //highlight the clubs icon in the bottom navigation view.

        //all cases will redirect to home activity but we are providing information about which icon was pressed in the intent.
        // for example if home icon is pressed navigate to home activity but also provide "home" as the activity name.
        //if group icon is pressed then navigate to home activity but send "group" as the activity name.
        //This will be used later in the HomeActivity to load the appropriate fragment in the body of the activity.
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra(Constants.ACTIVITY_NAME,"home");  //sending signal that HomeFragment should be loaded in the HomeActivity's fragment.
                    startActivity(intent);
                    break;
                case R.id.post:
                    Intent intent2 = new Intent(getActivity(), HomeActivity.class);
                    intent2.putExtra(Constants.ACTIVITY_NAME,"post");  //sending signal that AllPostFragment should be loaded in the HomeActivity's fragment.
                    startActivity(intent2);
                    break;
                case R.id.group:
                    //In allclubs activity. No action required.
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

