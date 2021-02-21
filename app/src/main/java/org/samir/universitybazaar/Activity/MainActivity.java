package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Authentication.RegisterActivity;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

/**
 * @author Samir Shrestha
 * @description This class is the application entry point.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button register = findViewById(R.id.register);
        Button login = findViewById(R.id.login);

        //getting the session of the current logged in user.
        UserSession userSession = new UserSession(this);
        User user = userSession.isUserLoggedIn();

        if(user != null){
            //user is logged in. navigate to the homepage directly.
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }else{
            //user is not logged in. Show the application entry page. Allow user to either register or login.
            register.setOnClickListener(v->{
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            });

            login.setOnClickListener(v->{
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }
}