package org.samir.universitybazaar;



import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.samir.universitybazaar.Database.UserSession;
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

        UserSession userSession = new UserSession(this);
        User user = userSession.isUserLoggedIn();
        if(user != null){
            textView.setText("Welcome: " + user.getEmail());
            button.setOnClickListener(v -> {userSession.signOutUser();});
        }

    }
}