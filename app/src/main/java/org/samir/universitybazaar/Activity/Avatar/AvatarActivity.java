package org.samir.universitybazaar.Activity.Avatar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Activity.Profile.EditProfileActivity;
import org.samir.universitybazaar.Database.ProfileDAO;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

public class AvatarActivity extends AppCompatActivity {
    private User user;
    private TextView txtWarning;
    private ImageView avatarView;
    private Button back2List,saveAvatar;
    private String avatar;
    ProfileDAO profileDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        UserSession userSession = new UserSession(this); //gets the handle to the shared preferences
        user = userSession.isUserLoggedIn();


        initViews();
        initListeners();
    }

    //initializes all the elements.
    private void initViews() {
        back2List = findViewById(R.id.back2List);
        saveAvatar = findViewById(R.id.saveAvatar);
        avatarView = findViewById(R.id.avatarView);

        txtWarning = findViewById(R.id.txtWarning);

        //get image name from database
        profileDAO = new ProfileDAO(AvatarActivity.this);

        //click change button then it should get the new image name to show
        avatar = getIntent().getStringExtra("image");
        if(avatar != null){
            if(!avatar.equals("")){
                setImage(avatar);
            }
        }
    }

    private void initListeners() {
        back2List.setOnClickListener(v->{
            Intent intent = new Intent(this,AvatarChangeActivity.class);
            startActivity(intent);
        });

        //when save is pressed, save image name to the database.
        saveAvatar.setOnClickListener(v->{

            AvatarActivity.SaveAvatar saveAvatar = new AvatarActivity.SaveAvatar(user.getMemberId(),avatar);
            Thread thread = new Thread(saveAvatar);
            thread.start(); //update background task started.
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });
    }

    //handles background process to save the profile
    private class SaveAvatar implements Runnable{
        private String memberId,avatar;
        public SaveAvatar(String memberId,String avatar){
            this.memberId = memberId;
            this.avatar = avatar;
        }

        @Override
        public void run() {
            if (!profileDAO.updateAvatar(memberId,avatar)) {
                //database operation err
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtWarning.setVisibility(View.VISIBLE);
                        txtWarning.setText("Couldn't update avatar. Please try again");
                    }
                });
            }
        }

    }

    private void setImage(String avatar){
        switch (avatar){
            case "bear": avatarView.setImageResource(R.mipmap.bear);
                break;
            case "beauty": avatarView.setImageResource(R.mipmap.beauty);
                break;
            case "blue": avatarView.setImageResource(R.mipmap.blue);
                break;
            case "cool": avatarView.setImageResource(R.mipmap.cool);
                break;
            case "dog": avatarView.setImageResource(R.mipmap.dog);
                break;
            case "duck": avatarView.setImageResource(R.mipmap.duck);
                break;
            case "girl": avatarView.setImageResource(R.mipmap.girl);
                break;
            case "purple": avatarView.setImageResource(R.mipmap.purple);
                break;
            case "terrible": avatarView.setImageResource(R.mipmap.terrible);
                break;
            default:
                break;
        }
    }

}
