package org.samir.universitybazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Authentication.RegisterActivity;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.User;

//Application entry point.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.register);
        Button login = findViewById(R.id.login);

        //getting the session of the current logged in user.
        UserSession userSession = new UserSession(this);
        User user = userSession.isUserLoggedIn();

        if(user != null){
            //user is logged int. navigate to the homepage.
            Intent intent = new Intent(this,TestActivity.class);
            startActivity(intent);
        }else{
            //user is not logged in. Initialize and display the landing page.
            button.setOnClickListener(v->{
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            });

            login.setOnClickListener(v->{
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }
}