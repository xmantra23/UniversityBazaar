package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;

/**
 * @author Samir Shrestha
 * This is class displays a new message form which allows a user to send a new message to other members.
 */
public class NewMessageActivity extends AppCompatActivity {
    private EditText edtTxtSubject,edtTxtMessageBody;
    private TextView txtReceiverName;
    private Button btnSendMessage,btnCancelMessage;
    private String messageType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        //get the memberName and memberId from the calling intent.
        Intent incomingIntent = getIntent();
        String memberName = incomingIntent.getStringExtra(Constants.MEMBER_NAME);
        String memberId = incomingIntent.getStringExtra(Constants.MEMBER_ID);
        messageType = incomingIntent.getStringExtra(Constants.MESSAGE_TYPE);

        initializeUI(memberName,memberId); //initialize the components in the layout file.
        handleListeners(); //handle button clicks to send and cancel buttons.

    }

    private void handleListeners() {

        btnCancelMessage.setOnClickListener(v->{
            //go back to MessageHomeActivity
            Intent intent = new Intent(NewMessageActivity.this,MessageHomeActivity.class);
            startActivity(intent);
        });

    }

    private void initializeUI(String memberName,String memberId) {
        edtTxtSubject = findViewById(R.id.edtTxtSubject);
        edtTxtMessageBody = findViewById(R.id.edtTxtMessageBody);
        txtReceiverName = findViewById(R.id.txtReceiverName);
        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnCancelMessage = findViewById(R.id.btnCancelMessage);

        txtReceiverName.setText("Send to: " + memberName); //setting the name of the receiver in the UI.
    }
}