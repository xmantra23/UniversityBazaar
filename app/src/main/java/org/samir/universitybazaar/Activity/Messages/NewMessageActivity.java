package org.samir.universitybazaar.Activity.Messages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.samir.universitybazaar.Database.MessageDAO;
import org.samir.universitybazaar.Models.Message;
import org.samir.universitybazaar.Models.Profile;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.Utility.Constants;
import org.samir.universitybazaar.Utility.Utils;

import java.util.ArrayList;

/**
 * @author Samir Shrestha
 * This is class displays a new message form which allows a user to send a new message to other members.
 */
public class NewMessageActivity extends AppCompatActivity {
    private EditText edtTxtSubject,edtTxtMessageBody;
    private TextView txtReceiverName;
    private Button btnSendMessage,btnCancelMessage;
    private String messageType,memberName,memberId;
    private User user;
    private MessageDAO db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        //get the memberName and memberId from the calling intent.
        //note: when sending message to club members the memberName refers to clubName
        // and memberId refers to clubId. clubId has been converted to string for compatibility issues.
        Intent incomingIntent = getIntent();
        memberName = incomingIntent.getStringExtra(Constants.MEMBER_NAME);
        memberId = incomingIntent.getStringExtra(Constants.MEMBER_ID);
        messageType = incomingIntent.getStringExtra(Constants.MESSAGE_TYPE);

        initializeUI(memberName,memberId); //initialize the components in the layout file.
        user = Utils.getLoggedInUser(this);
        if(user != null){  //user is logged in
            handleListeners(); //handle button clicks to send and cancel buttons.
        }


    }

    private void handleListeners() {
        btnSendMessage.setOnClickListener(v->{
            handleSendingMessage();
        });

        btnCancelMessage.setOnClickListener(v->{
            //go back to MessageHomeActivity
            Intent intent = new Intent(NewMessageActivity.this,MessageHomeActivity.class);
            startActivity(intent);
        });

    }

    private void handleSendingMessage() {
        db = new MessageDAO(this);

        //get all the data to construct the message object
        String messageDate = Utils.getCurrentDate();
        String subject = edtTxtSubject.getText().toString();
        String messageBody = edtTxtMessageBody.getText().toString();
        String senderId = user.getMemberId();
        String senderName = Utils.getUserFullName(this);

        if(messageType.equals(Constants.SINGLE_MESSAGE)){
            //handle sending a message to a single user.
            String receiverId = memberId;
            String receiverName = memberName;
            Message message = new Message(subject,messageBody,senderId,senderName,receiverId,receiverName,messageDate,0);
            if(db.addSingleMessage(message)){
                Toast.makeText(this, "Message successfully send.", Toast.LENGTH_SHORT).show();
                db.addToSentMessages(message,Constants.SINGLE_MESSAGE); //adding to the sent_messages table and signifying that it is a single message.
                //after sending the message navigate to the MessageHomeActivity
                Intent intent = new Intent(NewMessageActivity.this,MessageHomeActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Error couldn't send message.", Toast.LENGTH_SHORT).show();
            }
        }else if(messageType.equals(Constants.ALL_MESSAGE)){
            //send message to everybody in the system.
            boolean success = true;
            ArrayList<String> allMembersId = db.getAllUsersId(senderId);

            //go through all users id and send message to everyone.
            for(String receiverId : allMembersId){
                Message message = new Message(subject,messageBody,senderId,senderName,receiverId,"user",messageDate,0);
                if(!db.addSingleMessage(message)){
                    success = false;
                };
            }

            //records the sent message in the database in the sent_messages table.
            Message sentMessage = new Message(subject,messageBody,senderId,senderName,"none","All Users",messageDate,0);
            db.addToSentMessages(sentMessage,Constants.ALL_MESSAGE);

            //see if messages were successfully sent to everyone or not.
            if(success){
                Toast.makeText(this, "Successfully Messaged All Users", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Error sending to all users",Toast.LENGTH_SHORT).show();
            }
            //after sending the message navigate to the MessageHomeActivity
            Intent intent = new Intent(NewMessageActivity.this,MessageHomeActivity.class);
            startActivity(intent);
        }else if(messageType.equals(Constants.CLUB_MESSAGE)){
            //send message to everybody in th club
            boolean success = true;
            int clubId = Integer.parseInt(memberId); //converting the string clubid that we got from the calling intent to int so that we can query the database.
            String clubName = memberName; // memberName in this context refers to the club name. Please don't get confused.
            ArrayList<String> allClubMembersId = db.getAllClubMembersId(clubId);

            //go through all the members in the club and send message to everybody in the club.
            int count = 0; // counter for keeping track of how many messages were sent.
            for(String receiverId : allClubMembersId){
                Message message = new Message(subject,messageBody,senderId,senderName,receiverId,clubName,messageDate,0);
                if(!db.addSingleMessage(message)){
                    success = false;
                }else{
                    count += 1;
                }
            }

            //records the sent message in the database in the sent_messages table.
            if(count > 0) {
                //only update the database if at least one message was sent.
                Message sentMessage = new Message(subject, messageBody, senderId, senderName, memberId, clubName, messageDate, 0);
                db.addToSentMessages(sentMessage, Constants.CLUB_MESSAGE);
            }
            //see if messages were successfully sent to everyone or not.
            if(success){
                Toast.makeText(this, "Successfully Messaged All Club Members", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Error sending to all club members",Toast.LENGTH_SHORT).show();
            }
            //after sending the message navigate to the MessageHomeActivity
            Intent intent = new Intent(NewMessageActivity.this,MessageHomeActivity.class);
            startActivity(intent);
        }

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