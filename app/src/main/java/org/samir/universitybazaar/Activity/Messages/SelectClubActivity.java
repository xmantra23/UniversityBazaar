package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.samir.universitybazaar.Adapter.SelectClubAdapter;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Utils;

import java.util.ArrayList;

public class SelectClubActivity extends AppCompatActivity {
    private SelectClubAdapter adapter;
    private RecyclerView clubsRecView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_club);

        clubsRecView = findViewById(R.id.clubsRecView);
        adapter = new SelectClubAdapter(this);

        ClubDAO db = new ClubDAO(this);
        User user = Utils.getLoggedInUser(this);
        ArrayList<Club> clubs = db.getClubsByMemberId(user.getMemberId());
        adapter.setClubs(clubs);
        clubsRecView.setAdapter(adapter);
        clubsRecView.setLayoutManager(new GridLayoutManager(this,2));
    }
}