package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.samir.universitybazaar.R;

public class MessageTypeActivity extends AppCompatActivity {
    private Button btnMember, btnClubMembers, btnAllUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_type);
        
        initializeUI(); //initialize UI in the layout file
        handleListeners(); //handle button clicks for all the buttons in this activity.
    }

    private void handleListeners() {
        btnMember.setOnClickListener(v->{
            Intent intent = new Intent(MessageTypeActivity.this,SearchMemberActivity.class);
            startActivity(intent);
        });
        
        btnClubMembers.setOnClickListener(v->{
            // TODO: 4/5/2021 navigate to club selecting activity 
        });
        
        btnAllUsers.setOnClickListener(v->{
            // TODO: 4/5/2021 navigate to  new message activty. 
        });
    }

    private void initializeUI() {
        btnMember = findViewById(R.id.btnMember);
        btnClubMembers = findViewById(R.id.btnClubMembers);
        btnAllUsers = findViewById(R.id.btnAllUsers);
    }
}