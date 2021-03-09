package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

public class ClubActivity extends AppCompatActivity {
    private TextView txtClubTitle,txtClubDescription,txtCreatorName,txtCreatedDate;
    private TextView txtPost,txtEdit,txtDelete;
    private TextView txtViewAllNotice,txtViewAllPosts;
    private RecyclerView noticeRecView,postRecView;

    private UserSession session;
    private User user;
    private boolean isOwner = false; // flag to check if user owns this club.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        initializeViews(); //initialize all the elements in activity_club.xml

        session = new UserSession(this); //get the current session data.
        user = session.isUserLoggedIn(); //get the logged in user.
        if(user != null){ //check if user is logged in before doing anything.

            Intent intent = getIntent(); //receiving the intent from the caller activity.
            if(intent != null){
                int clubId = intent.getIntExtra(Constants.CLUB_ID,-1);//get the clubId from the calling intent.
                if(clubId != -1){//checking for valid clubId
                    ClubDAO cb = new ClubDAO(this);
                    Club club = cb.getClubById(clubId); //get the club with the clubId from the database.
                    if(club != null){
                        initializeUI(club); //set all the text fields in the club UI.
                        isOwner = Utils.isOwner(club.getOwnerId(),user.getMemberId()); //is true if user owns this club
                    }

                }
            }
            //only owner of the club can edit or delete this club.
            if(!isOwner){
                txtEdit.setVisibility(View.GONE);
                txtDelete.setVisibility(View.GONE);
            }

        }

    }

    private void initializeUI(Club club) {
        txtClubTitle.setText(club.getTitle());
        txtClubDescription.setText(club.getLongDescription());
        txtCreatorName.setText("Admin: " + club.getOwnerName());
        txtCreatedDate.setText("Formed: " + club.getCreatedDate());
        txtPost.setOnClickListener(v->{
            if(isOwner){
                Intent intent = new Intent(ClubActivity.this,CreateClubNoticeActivity.class);
                intent.putExtra(Constants.CLUB_ID,club.get_id());
                startActivity(intent);
            }
            // TODO: 3/8/2021 navigate to create announcement or create post based on if user is admin or a regular member.
        });
        txtEdit.setOnClickListener(v->{
            //// TODO: 3/8/2021 handle editing post. only admin should be allowed to edit
        });
        txtEdit.setOnClickListener(v->{
            // TODO: 3/8/2021 handle delete post. only admin should be allowed to delete.
        });

    }

    private void initializeViews() {
        txtClubTitle = findViewById(R.id.txtClubTitle);
        txtClubDescription = findViewById(R.id.txtClubDescription);
        txtCreatorName = findViewById(R.id.txtCreatorName);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        txtPost = findViewById(R.id.txtPost);
        txtEdit = findViewById(R.id.txtEdit);
        txtDelete = findViewById(R.id.txtDelete);
        txtViewAllNotice = findViewById(R.id.txtViewAllNotice);
        txtViewAllPosts = findViewById(R.id.txtViewAllPosts);
        noticeRecView = findViewById(R.id.noticeRecView);
        postRecView = findViewById(R.id.postRecView);
    }
}
