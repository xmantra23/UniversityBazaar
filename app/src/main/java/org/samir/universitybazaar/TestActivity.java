package org.samir.universitybazaar;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.EmailService.EmailHelper;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.Profile.ViewProfileActivity;

//This is just a test activity class to test the login functionality.
public class TestActivity extends AppCompatActivity {
    private Button button,user_profile;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        user_profile = findViewById(R.id.user_profile);

        //check if the user is logged in.
        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        User user = userSession.isUserLoggedIn(); //if the user is logged in then get that user as User object from the shared preferences.
        if(user != null){
            textView.setText("Welcome: " + user.getEmail());
            button.setOnClickListener(v -> {userSession.signOutUser();}); //destroys the shared preferences effectively ending the user session.
        }
        initListeners();
    }

    //initializes all onclick listeners
    private void initListeners() {
        user_profile.setOnClickListener((view)->{handleProfile(); });
    }

    private void handleProfile() {
        //redirect user to the profile page.
        Intent intent = new Intent(TestActivity.this, ViewProfileActivity.class);
        startActivity(intent);
    }
}