package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

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
            Intent intent = new Intent(MessageTypeActivity.this,SelectClubActivity.class);
            startActivity(intent);
        });
        
        btnAllUsers.setOnClickListener(v->{
            Intent intent = new Intent(MessageTypeActivity.this,NewMessageActivity.class);
            intent.putExtra(Constants.MEMBER_NAME,"All Users");
            intent.putExtra(Constants.MEMBER_ID,-1);
            intent.putExtra(Constants.MESSAGE_TYPE,Constants.ALL_MESSAGE);
            startActivity(intent);
        });
    }

    private void initializeUI() {
        btnMember = findViewById(R.id.btnMember);
        btnClubMembers = findViewById(R.id.btnClubMembers);
        btnAllUsers = findViewById(R.id.btnAllUsers);
    }
}