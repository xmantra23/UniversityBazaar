package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.samir.universitybazaar.Activity.HomeActivity;
import org.samir.universitybazaar.Adapter.SentMessagesAdapter;
import org.samir.universitybazaar.R;

public class MessageHomeActivity extends AppCompatActivity {
    private Button btnInbox, btnSentMessages, btnNewMessage,btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_home);

        initializeUI(); //initialize the UI components in the layout file for display.
        handleListeners();//handle button clicks for Inbox, Sent Messages and New Message click events.
    }

    private void handleListeners() {
        btnInbox.setOnClickListener(v->{
            Intent intent = new Intent(MessageHomeActivity.this,InboxActivity.class);
            startActivity(intent);
        });

        btnSentMessages.setOnClickListener(v->{
            Intent intent = new Intent(MessageHomeActivity.this, SentMessagesActivity.class);
            startActivity(intent);
        });

        //navigate to activity that allows user to select what type of message are they trying to send.
        btnNewMessage.setOnClickListener(v->{
            Intent intent = new Intent(MessageHomeActivity.this,MessageTypeActivity.class);
            startActivity(intent);
        });

        //navigate to home activity
        btnHome.setOnClickListener(v->{
            Intent intent = new Intent(MessageHomeActivity.this,HomeActivity.class);
            startActivity(intent);
        });

    }

    private void initializeUI() {
        btnInbox = findViewById(R.id.btnInbox);
        btnSentMessages = findViewById(R.id.btnSentMessages);
        btnNewMessage = findViewById(R.id.btnNewMessage);
        btnHome = findViewById(R.id.btnHome);
    }

    @Override
    public void onBackPressed() {
        //navigate to the HomePage if user presses back button
        Intent intent = new Intent(MessageHomeActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}