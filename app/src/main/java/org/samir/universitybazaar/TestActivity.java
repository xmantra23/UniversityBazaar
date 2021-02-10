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

        Button email = findViewById(R.id.button2);

        //check if the user is logged in.
        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        User user = userSession.isUserLoggedIn(); //if the user is logged in then get that user as User object from the shared preferences.
        if(user != null){
            textView.setText("Welcome: " + user.getEmail());
            button.setOnClickListener(v -> {userSession.signOutUser();}); //destroys the shared preferences effectively ending the user session.
            email.setOnClickListener(v->{
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EmailHelper emailHelper = new EmailHelper();
                        emailHelper.sendConfirmationEmail("samir.shrestha1@yahoo.com","100795680","password");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TestActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                thread.start();
            });
        }
    }
}