package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.samir.universitybazaar.Models.Message;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

public class SentMessageDetailsActivity extends AppCompatActivity {
    private TextView txtSubject,txtMessageBody,txtReceiverName,txtSentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_message_details);

        //get the message object that was clicked on before this activity was called.
        Bundle data = getIntent().getExtras();
        Message message = (Message) data.getParcelable(Constants.MESSAGE);

        initializeUI();

        if(message != null){
            //set all the details for the message in the activity.
            txtSubject.setText(message.getSubject());
            txtMessageBody.setText(message.getMessage());
            txtReceiverName.setText("Send By: "+message.getReceiverName());
            txtSentDate.setText(message.getMessageDate());
        }
    }

    private void initializeUI() {
        txtSubject = findViewById(R.id.txtSubject);
        txtMessageBody = findViewById(R.id.txtMessageBody);
        txtReceiverName = findViewById(R.id.txtReceiverName);
        txtSentDate = findViewById(R.id.txtSentDate);
    }
}