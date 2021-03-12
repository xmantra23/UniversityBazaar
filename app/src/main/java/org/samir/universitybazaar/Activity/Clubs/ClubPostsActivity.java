package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import org.samir.universitybazaar.Adapter.ClubAdapter;
import org.samir.universitybazaar.Adapter.ClubNoticeAdapter;
import org.samir.universitybazaar.Adapter.ClubPostsAdapter;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author samir shrestha
 * @decription This class basically lists all the member posts for a club in a new activity page.
 */
public class ClubPostsActivity extends AppCompatActivity {
    private RecyclerView memberPostsRecView;//lists all the member posts for the club
    private ClubPostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_posts);
        initViews();//initialize views.

        //get the clubId from the calling intent
        Intent intent = getIntent();
        int clubId = intent.getIntExtra(Constants.CLUB_ID,-1);
        if(clubId != -1){
            handleRecViews(clubId);
        }
    }

    private void handleRecViews(int clubId) {
        adapter = new ClubPostsAdapter(this);
        ClubDAO cb = new ClubDAO(this);
        //get all the posts in this club for this clubId from the database.
        ArrayList<ClubPost> allMemberPosts = cb.getClubPosts(clubId);
        //display all the member posts in the ClubPostsActivity page, but only if allMemberPosts array list is not empty
        if(allMemberPosts != null){
            adapter.setClubPosts(allMemberPosts);;
            memberPostsRecView.setAdapter(adapter);
            //display the latest item first.
            memberPostsRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
        }

    }

    private void initViews() {
        memberPostsRecView = findViewById(R.id.memberPostsRecView);
    }
}