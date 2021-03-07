package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.samir.universitybazaar.Authentication.LoginActivity;
import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Database.UserSession;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Utils;

public class CreateClubActivity extends AppCompatActivity {

    private EditText edtTxtTitle,edtTxtShortDesc,edtTxtLongDesc;
    private Button btnCancel,btnCreate;
    private DatabaseHelper db;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        initViews();//initialize views.


        //get current logged in user and database instance.
        db = new DatabaseHelper(this);
        session = new UserSession(this);
        User user = session.isUserLoggedIn();

        if(user == null){
            //user is not logged in. Redirect back to login page.
            Intent intent = new Intent(CreateClubActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            //user is logged in
            initListeners(user);//initialize onclick listeners.
        }

    }

    private void initListeners(User user) {
        btnCreate.setOnClickListener(v->{
            Profile userProfile = db.getProfile(user.getMemberId()); //get the user profile of the current logged in user.

            //get all the details for creating a new club from the UI.
            String title = edtTxtTitle.getText().toString();
            String shortDescription = edtTxtShortDesc.getText().toString();
            String longDescription = edtTxtLongDesc.getText().toString();
            String ownerName = userProfile.getFullName();
            String ownerId = userProfile.getMemberId();
            String createdDate = Utils.getCurrentDate();
            int memberCount = 0; //club has no members when it is first created.

            //create a new club with all the details.
            Club club = new Club(title,shortDescription,longDescription,ownerName,ownerId,createdDate,memberCount);

            //save the new club to the database.
            ClubDAO cb = new ClubDAO(CreateClubActivity.this);
            boolean success = cb.addClub(club);

            //check if add was successful or not. Display appropriate message and redirect to homepage.
            if(success){
                Toast.makeText(this, "Club successfully created", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Error. Couldn't create club", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(CreateClubActivity.this,HomeActivity.class);
            startActivity(intent);
        });

        btnCancel.setOnClickListener(v->{
            //redirect to homepage.
            Intent intent = new Intent(CreateClubActivity.this,HomeActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtShortDesc = findViewById(R.id.edtTxtShortDesc);
        edtTxtLongDesc = findViewById(R.id.edtTxtLongDesc);
        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);
    }


}