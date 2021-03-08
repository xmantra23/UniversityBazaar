package org.samir.universitybazaar.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.CommentDAO;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Comment;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Lu yifei
 * @Description This class displays a reply input page
 */
public class CommentReplyActivity extends AppCompatActivity {
    private UserSession userSession;
    private DatabaseHelper db;
    private EditText edtTxtReply;
    private Button btnReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_reply);

        userSession = new UserSession(this);
        db = new DatabaseHelper(this);

        initViews();
        handleListeners();
    }

    //initializes all the elements in the layout file.
    private void initViews(){
        edtTxtReply = findViewById(R.id.edtTxtReply);
        btnReply = findViewById(R.id.btnReply);
    }

    private void handleListeners() {
        btnReply.setOnClickListener(v->{
            int post_id = getIntent().getIntExtra(Constants.POST_ID,-1);
            int comment_id = getIntent().getIntExtra(Constants.Comment_ID,-1);
            User user = userSession.isUserLoggedIn(); //get current logged in user from the session.
            if(user != null) { //if user is logged in.
                //get reply comment
                CommentDAO commentDAO = new CommentDAO(CommentReplyActivity.this);
                Comment replyComment = commentDAO.getComment(comment_id);

                //build comment object
                Comment comment = new Comment();
                comment.setPostId(post_id);
                comment.setCommentReceiverId(replyComment.getCommentOwnerId());
                comment.setCommentReceiverName(replyComment.getCommentOwnerName());
                comment.setCommentText(edtTxtReply.getText().toString());

                //get the profile of the current logged in user from the database.
                ProfileDAO profileDAO = new ProfileDAO(CommentReplyActivity.this);
                Profile profile = profileDAO.getProfile(user.getMemberId());
                String commentOwnerName = profile.getFullName();

                //if the user doesn't have a full name then construct a username as: User + last four of the member id.
                if(commentOwnerName == null){
                    commentOwnerName = "User " + user.getMemberId().substring(6);
                }
                comment.setCommentOwnerName(commentOwnerName);

                comment.setCommentOwnerId(String.valueOf(user.getMemberId()));

                //Getting the current system date and formating it to mm/dd/yyyy format.
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String commentDate = df.format(date);
                comment.setCommentDate(commentDate);

                if(commentDAO.addComment(comment)){ //insert successful
                    Toast.makeText(this, "Reply successfully.", Toast.LENGTH_LONG).show();
                    //Redirect to PostActivity
                    Intent intent = new Intent(CommentReplyActivity.this,PostActivity.class);
                    intent.putExtra(Constants.POST_ID,post_id);
                    startActivity(intent);
                }else{ //insert failed
                    Toast.makeText(this, "Error. Please try again.", Toast.LENGTH_LONG).show();
                    //Redirect to PostActivity
                    Intent intent = new Intent(CommentReplyActivity.this,PostActivity.class);
                    intent.putExtra(Constants.POST_ID,post_id);
                    startActivity(intent);
                }
            }else{ //user is not logged in. Redirect to the login page.
                Toast.makeText(this, "You are not logged in.", Toast.LENGTH_LONG).show();
                //Redirect to LoginActivity
                Intent intent = new Intent(CommentReplyActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



}
