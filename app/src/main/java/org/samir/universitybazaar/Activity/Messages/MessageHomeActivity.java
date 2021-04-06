package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import org.samir.universitybazaar.R;

public class MessageHomeActivity extends AppCompatActivity {
    private Button btnInbox, btnSentMessages, btnNewMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_home);

        initializeUI(); //initialize the UI components in the layout file for display.
        handleListeners();//handle button clicks for Inbox, Sent Messages and New Message click events.
    }

    private void handleListeners() {
        btnInbox.setOnClickListener(v->{
            // TODO: 4/5/2021 handle clicking inbox button
        });

        btnSentMessages.setOnClickListener(v->{
            // TODO: 4/5/2021 handle clicking sent messages button
        });

        btnNewMessage.setOnClickListener(v->{
            // TODO: 4/5/2021 handle clicking new message button
        });

    }

    private void initializeUI() {
        btnInbox = findViewById(R.id.btnInbox);
        btnSentMessages = findViewById(R.id.btnSentMessages);
        btnNewMessage = findViewById(R.id.btnNewMessage);
    }
}