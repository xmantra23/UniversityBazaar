package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.samir.universitybazaar.Models.Message;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

public class InboxMessageActivity extends AppCompatActivity {
    private TextView txtSubject,txtMessageBody,txtSenderName,txtReceivedDate;
    private Button btnReply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_message);

        //get the message object that was clicked on before this activity was called.
        //Here this activity will be called from inside the InboxMessageAdapter
        Bundle data = getIntent().getExtras();
        Message message = (Message) data.getParcelable(Constants.MESSAGE);

        initializeUI();

        if(message != null){
            //set all the details for the message in the activity.
            txtSubject.setText(message.getSubject());
            txtMessageBody.setText(message.getMessage());
            txtSenderName.setText("Send By: "+message.getSenderName());
            txtReceivedDate.setText(message.getMessageDate());

            btnReply.setOnClickListener(v->{
                //reply to the sender of this message.
                String receiverName = message.getSenderName();
                String receiverId = message.getSenderId();

                //prepare to send the data to the NewMessageActivity via intent extras.
                Intent  intent = new Intent(InboxMessageActivity.this,NewMessageActivity.class);
                intent.putExtra(Constants.MEMBER_NAME,receiverName);
                intent.putExtra(Constants.MEMBER_ID,receiverId);
                intent.putExtra(Constants.MESSAGE_TYPE,Constants.SINGLE_MESSAGE);
                startActivity(intent);
            });
        }



    }

    private void initializeUI() {
        txtSubject = findViewById(R.id.txtSubject);
        txtMessageBody = findViewById(R.id.txtMessageBody);
        txtSenderName = findViewById(R.id.txtSenderName);
        txtReceivedDate = findViewById(R.id.txtReceivedDate);
        btnReply = findViewById(R.id.btnReply);
    }
}