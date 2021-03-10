package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.Models.ClubPost;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

/**
 * @author samir shrestha
 * this class basically displays all the information about a particular post when the user clicks on it in the Clubs Page.
 */
public class ClubPostActivity extends AppCompatActivity {
    private TextView txtPostTitle,txtPostDescription,txtEdit,txtDelete,txtCreatorName,txtCreatedDate;
    private RecyclerView commentsRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_post);
        initViews();

        //get the curretn logged in user
        UserSession session = new UserSession(this);
        User user = session.isUserLoggedIn();
        if(user != null){
            //get the post id to search in the database and owner id to verify ownership of this post
            Intent intent = getIntent();
            int postId = intent.getIntExtra(Constants.CLUB_POST_ID,-1);
            String ownerId = intent.getStringExtra(Constants.OWNER_ID);
            String adminId = intent.getStringExtra(Constants.CLUB_ADMIN_ID);

            //initialize UI with the data retrieved from the database
            initializeUI(postId,ownerId,user.getMemberId(),adminId);

            //initialize recycler view
            handleRecViews();

            //handle button clicks
            handleListeners();
        }else{
            //if user is not logged in then navigate to the login page.
            Toast.makeText(this, "Please Login.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void handleListeners() {
        // TODO: 3/9/2021 handle all the button clicks to edit and delete here.
    }

    private void handleRecViews() {
        // TODO: 3/9/2021 initialize the recycler view to display all the oomments here.
    }

    private void initializeUI(int postId,String ownerId, String memberId, String adminId) {
        ClubDAO cb = new ClubDAO(this);

        boolean isAdmin = false;
        boolean isOwner = false;
        //verify if the current user is an admin. admin can delete and edit any post in the club.
        if(adminId.equals(memberId)){
            //user is admin
            isAdmin = true;
        }

        //verify if the current users is the one who created this post. they should be able to edit and delete the post in club.
        if(Utils.isOwner(ownerId,memberId)){
            //user owns this club
            isOwner = true;
        }

        if(isAdmin || isOwner){
            //user is the owner that created this post or user is an admin. display both the buttons.
            txtEdit.setVisibility(View.VISIBLE);
            txtDelete.setVisibility(View.VISIBLE);
        }else{
            //user doesn't own this post and user is not an admin. hide the edit and delete button
            txtEdit.setVisibility(View.GONE);
            txtDelete.setVisibility(View.GONE);
        }

        if(postId != -1){
            //postId is valid. Use this to get the ClubPost object from the database that contains all the details for this activity.
            ClubPost post = cb.getClubPostById(postId);
            if(post != null){ //check if we actually got a ClubPost object from database query or it is just null.
                //initialize all the fields in the UI with the retrieved data from the ClubPost object.
                txtPostTitle.setText(post.getTitle());
                txtPostDescription.setText(post.getDescription());
                txtCreatorName.setText("Posted By: " + post.getCreatorName());
                txtCreatedDate.setText(post.getCreatedDate());
            }

        }
    }

    private void initViews() {
        txtPostTitle = findViewById(R.id.txtPostTitle);
        txtPostDescription = findViewById(R.id.txtPostDescription);
        txtEdit = findViewById(R.id.txtEdit);
        txtDelete = findViewById(R.id.txtDelete);
        txtCreatorName = findViewById(R.id.txtCreatorName);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        commentsRecView = findViewById(R.id.commentsRecView);
    }
}