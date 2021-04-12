package org.samir.universitybazaar.Activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Models.Profile;


public class ViewProfileActivity extends AppCompatActivity {

    private static final String TAG = "ViewProfileActivity";
    private ImageView avatar;
    private TextView viewTxtName, viewTxtId, viewTxtEmail, viewTxtGender, viewTxtAddress, viewTxtPhone,viewTxtDOB;
    private Button updateProfile,profile2Home;
    private DatabaseHelper databaseHelper;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        user = userSession.isUserLoggedIn();

        initViews();
        initListeners(); //initializing on click listeners.
    }

    //initializes all the elements.
    private void initViews() {
        avatar = findViewById(R.id.avatar);

        viewTxtName = findViewById(R.id.viewTxtName);
        viewTxtId = findViewById(R.id.viewTxtId);
        viewTxtEmail = findViewById(R.id.viewTxtEmail);
        viewTxtGender = findViewById(R.id.viewTxtGender);
        viewTxtAddress = findViewById(R.id.viewTxtAddress);
        viewTxtPhone = findViewById(R.id.viewTxtPhone);
        viewTxtDOB = findViewById(R.id.viewTxtDOB);

        updateProfile = findViewById(R.id.updateProfile);
        profile2Home = findViewById(R.id.profile2Home);

        ProfileDAO profileDAO = new ProfileDAO(ViewProfileActivity.this);

        if(user != null){
            Profile profile = profileDAO.getProfile(user.getMemberId());
            String viewTxtId = null2String(profile.getMemberId());
            String viewTxtEmail = null2String(profile.getEmail());
            String viewTxtName = null2String(profile.getFullName());
            String viewTxtGender = null2String(profile.getGender());
            String viewTxtAddress = null2String(profile.getAddress());
            String viewTxtPhone = null2String(profile.getPhone());
            String viewTxtDOB = null2String(profile.getDob());
            String avatar = null2String(profile.getAvatar());

            this.viewTxtId.setText(viewTxtId);
            this.viewTxtEmail.setText(viewTxtEmail);
            this.viewTxtName.setText(viewTxtName);
            this.viewTxtGender.setText(viewTxtGender);
            this.viewTxtAddress.setText(viewTxtAddress);
            this.viewTxtPhone.setText(viewTxtPhone);
            this.viewTxtDOB.setText(viewTxtDOB);

//            if image name is not set then choose default else set image from database.
            if(avatar == null ||avatar.equals("")){
                this.avatar.setImageResource(R.mipmap.girl); //default image.
            }else{
                setImage(avatar); //set image from database.
            }

        }
    }

    //initializes all onclick listeners
    private void initListeners() {
        updateProfile.setOnClickListener((view)->{handleUpdateProfile();});
        profile2Home.setOnClickListener((view)->{handleProfile2Home();});
    }

    private void handleUpdateProfile() {
        //redirect user to the edit profile page.
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void handleProfile2Home() {
        //redirect user to the edit profile page.
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private String null2String(String str){
        if(str == null){
            return "NULL";
        }else{
            return str;
        }
    }


    private void setImage(String avatar){
        switch (avatar){
            case "bear": this.avatar.setImageResource(R.mipmap.bear);
                break;
            case "beauty": this.avatar.setImageResource(R.mipmap.beauty);
                break;
            case "blue": this.avatar.setImageResource(R.mipmap.blue);
                break;
            case "cool": this.avatar.setImageResource(R.mipmap.cool);
                break;
            case "dog": this.avatar.setImageResource(R.mipmap.dog);
                break;
            case "duck": this.avatar.setImageResource(R.mipmap.duck);
                break;
            case "girl": this.avatar.setImageResource(R.mipmap.girl);
                break;
            case "purple": this.avatar.setImageResource(R.mipmap.purple);
                break;
            case "terrible": this.avatar.setImageResource(R.mipmap.terrible);
                break;
            default:
                break;
        }
    }
}
