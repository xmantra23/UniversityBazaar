package org.samir.universitybazaar.Activity.Posts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Samir Shrestha
 * @description This class is responsible for getting the user input from the user, create a new post and then finally add it to the database.
 * after that it redirects back to the HomePage activity.
 */
public class CreatePostActivity extends AppCompatActivity {

    private EditText edtTxtTitle,edtTxtDescription;
    private RadioGroup categoryRadioGroup;
    private RadioButton category; // this will be the selected radiobutton in the radio group.
    private Button btnCancel,btnPost;
    private UserSession userSession;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        userSession = new UserSession(this);
        db = new DatabaseHelper(this);

        initViews();
        handleListeners();
    }

    //handles onclick listeners
    private void handleListeners() {

        //get the current checked radiobutton anytime user clicks on a new radiobutton
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                category = group.findViewById(checkedId);
            }
        });

        //redirect back to HomePage if user clicks on cancel button.
        btnCancel.setOnClickListener(v->{
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

        //create a new post and save it in the database once the user clicks on the post button.
        btnPost.setOnClickListener(v->{
            User user = userSession.isUserLoggedIn(); //get current logged in user from the session.
            if(user != null){ //if user is logged in.

                //get all the details user typed in the user interface and then create and build a post object.
                Post post = new Post();
                post.setTitle(edtTxtTitle.getText().toString());
                post.setDescription(edtTxtDescription.getText().toString());
                post.setCategory(category.getText().toString());

                Profile profile = db.getProfile(user.getMemberId()); //get the profile of the current logged in user from the database.
                String creatorName = profile.getFullName();

                //if the user doesn't have a full name then construct a username as: User + last four of the member id.
                if(creatorName == null){
                        creatorName = "User " + user.getMemberId().substring(6);
                }

                post.setCreatorName(creatorName); //set the name of the user who created this post.
                post.setCreatorId(user.getMemberId());//this will be used as the id to search for a given users posts in the database.

                //Getting the current system date and formating it to mm/dd/yyyy format.
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(date);

                post.setCreatedDate(createdDate);//setting the current system date as the post created date.

                //adding the new post in the database.
                if(db.addPost(post)){ //insert successful
                    Toast.makeText(this, "Post successfully created.", Toast.LENGTH_LONG).show();
                    //Redirect to HomeActivity
                    Intent intent = new Intent(CreatePostActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else{ //insert failed
                    Toast.makeText(this, "Error. Please try again.", Toast.LENGTH_LONG).show();
                    //Redirect to HomeActivity
                    Intent intent = new Intent(CreatePostActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }else{ //user is not logged in. Redirect to the login page.
                Toast.makeText(this, "You are not logged in.", Toast.LENGTH_LONG).show();
                //Redirect to LoginActivity
                Intent intent = new Intent(CreatePostActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //initializes all the elements in the layout file.
    private void initViews() {
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtDescription = findViewById(R.id.edtTxtDescription);
        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        btnCancel = findViewById(R.id.btnCancel);
        btnPost = findViewById(R.id.btnPost);
        category = findViewById(categoryRadioGroup.getCheckedRadioButtonId());//set category to the default selected radio button
    }

    //if users selects back button then navigate to the home activity.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreatePostActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}