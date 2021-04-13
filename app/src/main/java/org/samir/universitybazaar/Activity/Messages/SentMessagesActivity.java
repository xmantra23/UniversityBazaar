package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import org.samir.universitybazaar.Adapter.InboxMessageAdapter;
import org.samir.universitybazaar.Adapter.SentMessagesAdapter;
import org.samir.universitybazaar.Database.MessageDAO;
import org.samir.universitybazaar.Models.Message;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Utils;

import java.util.ArrayList;

public class SentMessagesActivity extends AppCompatActivity {
    private SentMessagesAdapter adapter;
    private RecyclerView sentMessagesRecView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_messages);

        initializeUI();
        handleRecView();
    }

    private void handleRecView() {
        User user = Utils.getLoggedInUser(this);
        if(user != null){
            MessageDAO db = new MessageDAO(this);
            ArrayList<Message> messages = db.getSentMessages(user.getMemberId());

            if(messages != null){
                adapter.setMessages(messages);
                sentMessagesRecView.setAdapter(adapter);
                sentMessagesRecView.setLayoutManager(new LinearLayoutManager(SentMessagesActivity.this,RecyclerView.VERTICAL,true));
            }
        }
    }

    private void initializeUI() {
        adapter = new SentMessagesAdapter(this);
        sentMessagesRecView = findViewById(R.id.sentMessagesRecView);
    }
}