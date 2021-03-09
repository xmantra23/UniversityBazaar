package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

/**
 * @author samir shrestha
 * This activity displays a form to allow an admin to create announcements within a club.
 */
public class CreateClubNoticeActivity extends AppCompatActivity {
    private EditText edtTxtTitle ,edtTxtDescription;
    private Button btnCancel,btnPost;
    private UserSession session;
    private User user;
    private ClubDAO cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club_notice);
        initViews(); //initialize elements in the layout file.

        //get the current logged in user details.
        session = new UserSession(this);
        user = session.isUserLoggedIn();
        if(user != null){
            //get a handle for database operations.
            cb = new ClubDAO(this);

            //get the clubId of the ClubActivity that called this Activity.
            Intent intent = getIntent();
            int clubId = intent.getIntExtra(Constants.CLUB_ID,-1);
            handleListeners(clubId,user.getMemberId());
        }
    }

    private void initViews() {
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtDescription = findViewById(R.id.edtTxtDesc);
        btnCancel = findViewById(R.id.btnCancel);
        btnPost = findViewById(R.id.btnPost);
    }
    private void handleListeners(int clubId,String userId) {
        btnCancel.setOnClickListener(v->{
            //navigate back to the ClubActivity
            Intent intent = new Intent(CreateClubNoticeActivity.this,ClubActivity.class);
            intent.putExtra(Constants.CLUB_ID,clubId);
            startActivity(intent);
        });

        btnPost.setOnClickListener(v->{
            //add notice in the database and navigate back to the ClubActivity
            String title = edtTxtTitle.getText().toString();
            String description = edtTxtDescription.getText().toString();
            String createdDate = Utils.getCurrentDate();
            ClubNotice notice = new ClubNotice(clubId,title,description,userId,createdDate);
            if(cb.addNoticeInClub(notice)){
                //add was successful, display success
                Toast.makeText(this, "Notice Posted Successfully", Toast.LENGTH_SHORT).show();
            }else{
                //add failed. dispaly error.
                Toast.makeText(this, "Error. Couldn't post notice", Toast.LENGTH_SHORT).show();
            }

            //navigate back to the ClubActivity
            Intent intent = new Intent(CreateClubNoticeActivity.this,ClubActivity.class);
            intent.putExtra(Constants.CLUB_ID,clubId);
            startActivity(intent);
        });
    }
}