package org.samir.universitybazaar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Activity.MyPostsActivity;
import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Profile.ViewProfileActivity;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

public class AllPostsFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView postRecView;
    private MyPostAdapter adapter;
    private DatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_posts,container,false);
        initViews(view);
        initBottomNavView(view);
        handleRecyclerView(view);
        return view;
    }

    private void handleRecyclerView(View view) {
        adapter = new MyPostAdapter(getActivity());
        db = new DatabaseHelper(getActivity());
        ArrayList<Post> allPosts = db.getAllPosts();
        adapter.setPosts(allPosts);
        postRecView.setAdapter(adapter);
        postRecView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true));
    }

    private void initViews(View view) {
        bottomNavigationView = view.findViewById(R.id.bottomNavView);
        postRecView = view.findViewById(R.id.postRecView);
    }

    private void initBottomNavView(View view) {
        bottomNavigationView.setSelectedItemId(R.id.post);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra(Constants.ACTIVITY_NAME,"home");
                    startActivity(intent);
                    break;
                case R.id.post:
                    // TODO: 2/18/2021 redirect to all posts activity
                    break;
                case R.id.group:
                    //TODO: 2/18/2021 redirect to all clubs activity
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
