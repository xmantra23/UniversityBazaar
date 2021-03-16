package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import org.samir.universitybazaar.Adapter.ClubNoticeAdapter;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author samir shrestha
 * @description this activity displays all the announcements for a given club inside the club activity page
 */
public class ClubNoticesActivity extends AppCompatActivity {
    private RecyclerView noticesRecView;//lists all the announcements for the club
    private ClubNoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_notices);
        initViews();//initialize views.

        //get the clubId from the calling intent
        Intent intent = getIntent();
        int clubId = intent.getIntExtra(Constants.CLUB_ID,-1);
        if(clubId != -1){
            handleRecViews(clubId);
        }
    }

    private void handleRecViews(int clubId) {
        adapter = new ClubNoticeAdapter(this);
        ClubDAO cb = new ClubDAO(this);
        //get all the announcement for this clubId from the database.
        ArrayList<ClubNotice> allNotices = cb.getClubNotices(clubId);
        //display all the announcements in the ClubNoticeActivity, but only if allNotices array list is not empty
        if(allNotices != null){
            adapter.setNotices(allNotices);;
            noticesRecView.setAdapter(adapter);
            //display the latest item first.
            noticesRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
        }

    }

    private void initViews() {
        noticesRecView = findViewById(R.id.noticesRecView);
    }
}