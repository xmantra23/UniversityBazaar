package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.samir.universitybazaar.Adapter.MyPostAdapter;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * @description This class displays all the posts that the user has created.
 */
public class MyPostsActivity extends AppCompatActivity {
    private RecyclerView postRecView;
    private MyPostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        postRecView = findViewById(R.id.postRecView);
        adapter = new MyPostAdapter(this);

        DatabaseHelper db = new DatabaseHelper(this);
        UserSession userSession = new UserSession(this);

        User user = userSession.isUserLoggedIn();
        if(user != null){ //user is logged in.
            ArrayList<Post> posts = new ArrayList<>();
            posts = db.getPostByMemberId(user.getMemberId()); //get all the posts whose creatorId matches the logged in users memberId.
            adapter.setPosts(posts); //initialize the recycler view with the retrieved posts from the database.
        }

        //populate the recycler view and display all the posts in the activity page using a linear layout.
        postRecView.setAdapter(adapter);

        //reverseLayout true means the most recent post will be in the top.
        postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));

    }
}