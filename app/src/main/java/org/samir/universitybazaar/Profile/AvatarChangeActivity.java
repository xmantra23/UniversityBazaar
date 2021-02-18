package org.samir.universitybazaar.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

public class AvatarChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private User user;
    private Button back2Profile;
    private ImageView bear,beauty,blue,cool,dog,duck,girl,purple,terrible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_change);

        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        user = userSession.isUserLoggedIn();

        initViews();
        initListeners();
    }

    //initializes all the elements.
    private void initViews() {

        bear = findViewById(R.id.bear);
        beauty = findViewById(R.id.beauty);
        blue = findViewById(R.id.blue);
        cool =findViewById(R.id.cool);
        dog =findViewById(R.id.dog);
        duck =findViewById(R.id.duck);
        girl =findViewById(R.id.girl);
        purple =findViewById(R.id.purple);
        terrible =findViewById(R.id.terrible);

        back2Profile =findViewById(R.id.back2Profile);


        initializeListeners(girl);
        initializeListeners(beauty);
        initializeListeners(blue);
        initializeListeners(cool);
        initializeListeners(dog);
        initializeListeners(duck);
        initializeListeners(girl);
        initializeListeners(purple);
        initializeListeners(terrible);
    }

    private void initListeners() {
        back2Profile.setOnClickListener((view)->{handleBack2Profile();});
    }

    private void handleBack2Profile() {
        Intent intent = new Intent(AvatarChangeActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void initializeListeners(ImageView imageView){
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,AvatarActivity.class);
        switch (v.getId()){
            case R.id.bear:
                intent.putExtra("image","bear");
                break;
            case R.id.beauty:
                intent.putExtra("image","beauty");
                break;
            case R.id.blue:
                intent.putExtra("image","blue");
                break;
            case R.id.cool:
                intent.putExtra("image","cool");
                break;
            case R.id.dog:
                intent.putExtra("image","dog");
                break;
            case R.id.duck:
                intent.putExtra("image","duck");
                break;
            case R.id.girl:
                intent.putExtra("image","girl");
                break;
            case R.id.purple:
                intent.putExtra("image","purple");
                break;
            case R.id.terrible:
                intent.putExtra("image","terrible");
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
