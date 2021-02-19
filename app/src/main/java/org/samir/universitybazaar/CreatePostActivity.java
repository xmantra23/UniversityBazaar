package org.samir.universitybazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Post;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreatePostActivity extends AppCompatActivity {

    private EditText edtTxtTitle,edtTxtDescription;
    private RadioGroup categoryRadioGroup;
    private RadioButton category;
    private Button btnCancel,btnPost;
    private UserSession userSession;
    private ProfileDAO profileDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        userSession = new UserSession(this);
        profileDAO = new ProfileDAO(this);
        initViews();
        handleListeners();
    }

    private void handleListeners() {
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                category = group.findViewById(checkedId);
            }
        });

        btnCancel.setOnClickListener(v->{
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        });

        btnPost.setOnClickListener(v->{
            User user = userSession.isUserLoggedIn();
            if(user != null){
                Post post = new Post();
                post.setTitle(edtTxtTitle.getText().toString());
                post.setDescription(edtTxtDescription.getText().toString());
                post.setCategory(category.getText().toString());

                Profile profile = profileDAO.getProfile(user.getMemberId());
                String creatorName = profile.getFullName();
                if(creatorName.equals("") || creatorName == null){
                    creatorName = "User " + user.getMemberId().substring(6);
                }
                post.setCreatorName(creatorName);
                post.setCreatorId(user.getMemberId());
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String createdDate = df.format(date);
                post.setCreatedDate(createdDate);
                Log.d("post", "post details " + post.toString());
            }

        });
    }

    private void initViews() {
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtDescription = findViewById(R.id.edtTxtDescription);
        categoryRadioGroup = findViewById(R.id.categoryRadioGroup);
        btnCancel = findViewById(R.id.btnCancel);
        btnPost = findViewById(R.id.btnPost);
        category = findViewById(categoryRadioGroup.getCheckedRadioButtonId());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreatePostActivity.this,HomeActivity.class);
        startActivity(intent);
    }
}