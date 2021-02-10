package org.samir.universitybazaar;



import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.EmailService.EmailHelper;
import org.samir.universitybazaar.Models.User;

//This is just a test activity class to test the login functionality.
public class TestActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        //check if the user is logged in.
        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        User user = userSession.isUserLoggedIn(); //if the user is logged in then get that user as User object from the shared preferences.
        if(user != null){
            textView.setText("Welcome: " + user.getEmail());
            button.setOnClickListener(v -> {userSession.signOutUser();}); //destroys the shared preferences effectively ending the user session.
        }
    }
}