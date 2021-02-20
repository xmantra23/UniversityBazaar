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
        if(user != null){
            ArrayList<Post> posts = new ArrayList<>();
            posts = db.getPostByMemberId(user.getMemberId());
            adapter.setPosts(posts);
        }

        postRecView.setAdapter(adapter);
        postRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));

    }
}