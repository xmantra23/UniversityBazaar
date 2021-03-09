package org.samir.universitybazaar.Fragments;

import android.content.Intent;
import android.os.Bundle;
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
import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @description This fragment displays all the posts inside the HomePage Activity.
 */

public class AllPostsFragment extends Fragment {

    private BottomNavigationView bottomNavigationView; //The bottom navigation icons.
    private RecyclerView postRecView; //lists all the posts items.
    private MyPostAdapter adapter;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_posts,container,false);
        initViews(view);
        initBottomNavView(view); //initialize and then handle the bottom icons.
        handleRecyclerView(view); //initialize and set all the data for the recycler view containing all the posts.
        return view;
    }

    private void handleRecyclerView(View view) {
        adapter = new MyPostAdapter(getActivity()); //create a new MyPostAdapter instance

        db = new DatabaseHelper(getActivity());
        ArrayList<Post> allPosts = db.getAllPosts(); //get all the posts in the posts table from the database.

        adapter.setPosts(allPosts); //initialize all the posts for the adapter with the posts retrieved from the database.

        postRecView.setAdapter(adapter); //initialize the recycler view.
        //set linear layout with vertical orientation. reverseLayout is true means the most recent posts is on the top of the list.
        postRecView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true));
    }

    private void initViews(View view) {
        bottomNavigationView = view.findViewById(R.id.bottomNavView);
        postRecView = view.findViewById(R.id.postRecView);
    }

    //handles the bottom navigation view icon presses.
    private void initBottomNavView(View view) {
        bottomNavigationView.setSelectedItemId(R.id.post); //highlight the posts icon in the bottom navigation view.

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
                    // In allposts activity. No action required.
                    break;
                case R.id.group:
                    Intent intent2 = new Intent(getActivity(), HomeActivity.class);
                    intent2.putExtra(Constants.ACTIVITY_NAME,"group");  //sending signal that HomeFragment should be loaded in the HomeActivity's fragment.
                    startActivity(intent2);
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
