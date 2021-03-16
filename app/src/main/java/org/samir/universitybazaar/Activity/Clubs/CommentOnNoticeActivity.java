package org.samir.universitybazaar.Activity.Clubs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.ClubNoticeComment;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

/**
 * @author Samir Shrestha
 * This activity allows a user to comment on an announcement made by the admin in the clubs page.
 */
public class CommentOnNoticeActivity extends AppCompatActivity {
    private EditText edtTxtComment;
    private Button btnComment;
    private ClubDAO cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initViews();
        cb = new ClubDAO(CommentOnNoticeActivity.this);//for database access

        User user = Utils.getLoggedInUser(this);//get the logged in user
        if(user != null){
            //user is logged in. proceed to comment functionality
            handleListeners(user);
        }else{
            //redirect to login page if the user is not logged in.
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }

    private void initViews() {
        edtTxtComment = findViewById(R.id.edtTxtComment);
        btnComment = findViewById(R.id.btnComment);
    }

    private void handleListeners(User user){


        //add commment in the database when the user clicks on the comment button
        btnComment.setOnClickListener(v->{
            Intent intent = getIntent();
            int clubId = intent.getIntExtra(Constants.CLUB_ID,-1);
            int noticeId = intent.getIntExtra(Constants.NOTICE_ID,-1); //get the notice id.
            //get the logged in users id and full name.
            String commentOwnerId = user.getMemberId();
            String commentOwnerName = Utils.getUserFullName(this);

            //get the current system date
            String createdDate = Utils.getCurrentDate();
            String adminId = intent.getStringExtra(Constants.CLUB_ADMIN_ID); //get the id of the club owner.
            String comment = edtTxtComment.getText().toString(); //get the comment that the user typed.
            boolean isValidData = checkAllFields(noticeId,commentOwnerId,commentOwnerName,createdDate,adminId,comment);//verify if all the data are valid.
            if(isValidData){
                ClubNoticeComment c = new ClubNoticeComment(noticeId,comment,commentOwnerId,commentOwnerName,createdDate,adminId);
                if(cb.addNoticeComment(c)){
                    //comment was successfully added in the database.
                    Toast.makeText(this,"Successfully Commented.",Toast.LENGTH_SHORT).show();

                    //navigate back to the notification details page.
                    Intent intent2 = new Intent(CommentOnNoticeActivity.this, NoticeActivity.class);
                    intent2.putExtra(Constants.NOTICE_ID,noticeId); //passing the notice id
                    intent2.putExtra(Constants.OWNER_ID,adminId);//passing the creator id
                    intent2.putExtra(Constants.CLUB_ID,clubId); //passing the club id
                    startActivity(intent2);
                }else{
                    //comment failed
                    Toast.makeText(this, "Error. Couldn't Comment", Toast.LENGTH_SHORT).show();
                }
            }else{
                //comment failed. invalid data provided.
                Toast.makeText(this, "Error. Couldn't Comment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //utility method to check if all the fields are valid and there are no empty or null fields.
    private boolean checkAllFields(int noticeId,String commentOwnerId,String commentOwnerName,String createdDate,String adminId,String comment){
        if(noticeId != -1 && commentOwnerId != null && commentOwnerName != null && createdDate != null && adminId != null & !comment.equals("")){
            return true;
        }else{
            return false;
        }
    }
}