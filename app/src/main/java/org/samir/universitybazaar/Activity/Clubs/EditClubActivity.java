package org.samir.universitybazaar.Activity.Clubs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Database.ClubDAO;
import org.samir.universitybazaar.Models.Club;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

import java.util.ArrayList;

/**
 * @author minyi lu
 * @discription allow the owner of club edit the information
 */

public class EditClubActivity extends AppCompatActivity {
    private TextView edtTxtTitle, edtTxtShortDesc, edtTxtLongDesc;
    private Button btnCancel, btnEdit;
    private int club_id;
    private ClubDAO cb;
    private Club club;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_club);

        club_id = getIntent().getIntExtra(Constants.CLUB_ID,-1); // get club id
        cb = new ClubDAO(EditClubActivity.this);

        initViews();
        initTexts();
        handleListeners();
    }

    //initializes all the elements in the layout file.
    private void initViews(){
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtShortDesc = findViewById(R.id.edtTxtShortDesc);
        edtTxtLongDesc = findViewById(R.id.edtTxtLongDesc);
        btnCancel = findViewById(R.id.btnCancel);
        btnEdit = findViewById(R.id.btnEdit);
    }

    //get information from db and set them in the edtTxtTitle, edtTxtShortDesc, edtTxtLongDesc
    private void initTexts(){
        System.out.println("id:"+club_id);
        if (club_id>=0){

            club = cb.getClubById(club_id);
            edtTxtTitle.setText(club.getTitle());
            edtTxtShortDesc.setText(club.getShortDescription());
            edtTxtLongDesc.setText(club.getLongDescription());
        } else {
            Toast.makeText(EditClubActivity.this, "Cannot find this club", Toast.LENGTH_LONG).show();
        }

    }

    private void handleListeners() {
        //cancel edit and return back to ClubActivity
        btnCancel.setOnClickListener(v->{
            Intent intent = new Intent(EditClubActivity.this, ClubActivity.class);
            intent.putExtra(Constants.CLUB_ID,club_id);
            startActivity(intent);
        });
        //update the information of club and return back to ClubActivity
        btnEdit.setOnClickListener(v->{
            System.out.println("edit");
            club.setTitle(edtTxtTitle.getText().toString());
            club.setShortDescription(edtTxtShortDesc.getText().toString());
            club.setLongDescription(edtTxtLongDesc.getText().toString());
            boolean flag = cb.updateClub(club);
            if (flag){
                Toast.makeText(EditClubActivity.this, "Save Club Information Successfully!", Toast.LENGTH_LONG).show();
                //navigate back to the ClubActivity
                Intent intent = new Intent(EditClubActivity.this,ClubActivity.class);
                intent.putExtra(Constants.CLUB_ID,club_id);
                startActivity(intent);
            }else {
                Toast.makeText(EditClubActivity.this, "Save Club Information Failed! Please Try again!", Toast.LENGTH_LONG).show();
            }

        });
    }
}
