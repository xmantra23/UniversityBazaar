package org.samir.universitybazaar.Activity.Clubs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

/**
 * @author Samir Shrestha
 * this activity allows normal users to add a post inside a club activity page.
 */
public class PostInClubActivity extends AppCompatActivity {

    private EditText edtTxtTitle ,edtTxtDescription;
    private Button btnCancel,btnPost;
    private UserSession session;
    private User user;//will hold the current logged in user object. null if user not logged in.
    private ClubDAO cb;//database handle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_in_club);
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
            Intent intent = new Intent(PostInClubActivity.this, ClubActivity.class);
            intent.putExtra(Constants.CLUB_ID,clubId);
            startActivity(intent);
        });

        btnPost.setOnClickListener(v->{
            //add post for this club in the database and navigate back to the ClubActivity
            String title = edtTxtTitle.getText().toString();
            String description = edtTxtDescription.getText().toString();

            //get the full name of the current logged in user
            DatabaseHelper db = new DatabaseHelper(this);
            Profile profile = db.getProfile(user.getMemberId());
            String creatorName = profile.getFullName();

            //get all the details about the new post so that we can create a ClubPost object to pass to the database.
            String creatorId = user.getMemberId();
            String createdDate = Utils.getCurrentDate();
            String adminId = cb.getClubAdminId(clubId);

            //create a clubpost object
            ClubPost clubPost = new ClubPost(clubId,title,description,creatorName,creatorId,createdDate,adminId);
            if(cb.addPostInClub(clubPost)){
                //add was successful, display success
                Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show();
            }else{
                //add failed. display error.
                Toast.makeText(this, "Error. Couldn't post", Toast.LENGTH_SHORT).show();
            }

            //navigate back to the ClubActivity
            Intent intent = new Intent(PostInClubActivity.this,ClubActivity.class);
            intent.putExtra(Constants.CLUB_ID,clubId);
            startActivity(intent);
        });
    }

}