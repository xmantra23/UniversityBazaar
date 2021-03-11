package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.samir.universitybazaar.Adapter.ClubNoticeCommentAdapter;
import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.ClubNotice;
import org.samir.universitybazaar.Models.ClubNoticeComment;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

import java.util.ArrayList;

/**
 * @author samir shrestha
 * This class displays the details about an announcement/notice once a user clicks on it on the clubs page.
 */
public class NoticeActivity extends AppCompatActivity {
    private TextView txtNoticeTitle,txtNoticeDescription,txtEdit,txtDelete,txtCreatedDate,txtAddCommentButton;
    private RecyclerView commentsRecView;
    private ClubNoticeCommentAdapter adapter;
    private ClubDAO cb;
    private int clubId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initViews();
        cb = new ClubDAO(this);
        //get the current logged in user
        UserSession session = new UserSession(this);
        User user = session.isUserLoggedIn();
        if(user != null){
            //get the notice id to search in the database and owner id to verify ownership of this notice
            Intent intent = getIntent();
            int noticeId = intent.getIntExtra(Constants.NOTICE_ID,-1);
            String ownerId = intent.getStringExtra(Constants.OWNER_ID);

            Log.d("noticeactivity", "onCreate: " + noticeId + " " + ownerId + " " + user.getMemberId());
            //initialize UI with the data retrieved from the database
            initializeUI(noticeId,ownerId,user.getMemberId());

            //initialize recycler view
            handleRecViews(noticeId);

            //handle button clicks
            clubId = intent.getIntExtra(Constants.CLUB_ID,-1); //getting the club id so that we can get the admin id.
            handleListeners(noticeId,ownerId,user.getMemberId(),clubId);
        }else{
            //if user is not logged in then navigate to the login page.
            Toast.makeText(this, "Please Login.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }



    private void initializeUI(int noticeId,String ownerId, String memberId) {
        if(!Utils.isOwner(ownerId,memberId)){
            //user doesn't own this notice, hide the edit and delete button
            txtEdit.setVisibility(View.GONE);
            txtDelete.setVisibility(View.GONE);
        }else{
            //user is the admin that created this notice. display both the buttons.
            txtEdit.setVisibility(View.VISIBLE);
            txtDelete.setVisibility(View.VISIBLE);
        }

        if(noticeId != -1){
            //noticeId is valid. Use this to get the ClubNotice object from the database that contains all the details for this activity.
            ClubNotice notice = cb.getClubNoticeById(noticeId);

            if(notice != null){ //check if we actually got a ClubNotice object from database query or it is just null.
                //initialize all the fields in the UI with the retrieved data from the ClubNotice object.
                txtNoticeTitle.setText(notice.getTitle());
                txtNoticeDescription.setText(notice.getDescription());
                txtCreatedDate.setText(notice.getCreatedDate());
            }

        }
    }

    private void handleListeners(int noticeId,String ownerId, String memberId,int clubId) {
        //handle edit, delete and comment button clicks.

        txtAddCommentButton.setOnClickListener(v->{
            Intent intent = new Intent(NoticeActivity.this,CommentOnNoticeActivity.class);
            String adminId = cb.getClubAdminId(clubId);
            intent.putExtra(Constants.CLUB_ADMIN_ID,adminId);
            intent.putExtra(Constants.NOTICE_ID,noticeId);
            intent.putExtra(Constants.CLUB_ID,clubId);
            startActivity(intent);
        });
    }

    private void handleRecViews(int noticeId) {
        //display all the comments for this notice.
        ArrayList<ClubNoticeComment> noticeComments = cb.getClubNoticeComments(noticeId);
        adapter = new ClubNoticeCommentAdapter(NoticeActivity.this);
        if(noticeComments != null){
            adapter.setNoticeComments(noticeComments);
            commentsRecView.setAdapter(adapter);
            commentsRecView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,true));
        }
    }

    private void initViews() {
        //initializing all the UI elements in activity_notice.xml
        txtNoticeTitle = findViewById(R.id.txtNoticeTitle);
        txtNoticeDescription = findViewById(R.id.txtNoticeDescription);
        txtEdit = findViewById(R.id.txtEdit);
        txtDelete = findViewById(R.id.txtDelete);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        txtAddCommentButton = findViewById(R.id.txtAddComment);
        commentsRecView = findViewById(R.id.commentsRecView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NoticeActivity.this,ClubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.CLUB_ID,clubId);
        startActivity(intent);
    }
}