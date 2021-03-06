package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.samir.universitybazaar.Adapter.ClubAdapter;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.util.ArrayList;
/**
 * @author Samir Shrestha
 * @description This class displays all the clubs that the user has subscribed to.
 */
public class MySubscriptionsActivity extends AppCompatActivity {
    private RecyclerView clubRecView;
    private ClubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscriptions);

        clubRecView = findViewById(R.id.clubRecView);
        adapter = new ClubAdapter(this);

        ClubDAO db = new ClubDAO(this);//handle for database access

        //get current logged in user
        UserSession userSession = new UserSession(this);
        User user = userSession.isUserLoggedIn();

        if(user != null){ //user is logged in.
            ArrayList<Club> clubs = new ArrayList<>();
            clubs = db.getSubscribedClubsByMemberId(user.getMemberId()); //get all the clubs whose memberId matches the logged in users memberId in the subscriptions list view.
            Log.d("subscriptions", "onCreate: " + clubs.toString());
            adapter.setClubs(clubs); //initialize the recycler view with the retrieved clubs from the database.
        }

        //populate the recycler view and display all the clubs in the activity page using a linear layout.
        clubRecView.setAdapter(adapter);

        //reverseLayout true means the most recent post will be in the top.
        clubRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
    }
}