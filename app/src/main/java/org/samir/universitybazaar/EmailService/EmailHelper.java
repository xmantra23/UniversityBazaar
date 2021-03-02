package org.samir.universitybazaar.EmailService;

import android.content.Context;
import android.util.Log;

import org.samir.universitybazaar.R;

/**
 * @author Samir Shrestha
 */
public class EmailHelper {
    private static final String SENDER_EMAIL = "universitybazaar.team4@gmail.com";
    private String receiverEmail;
    private String emailSubject;
    private String emailBody;
    private Context context;


    public EmailHelper(Context context,String receiverEmail,String emailSubject, String emailBody){
        this.receiverEmail = receiverEmail;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.context = context;
    }

    public EmailHelper(Context context){
        this.context = context;
    }

    public boolean sendConfirmationEmail(String receiverEmail,String userMemberId,String userPassword){
        String subject = "Welcome to University Bazaar";
        String messageBody = "Dear New User,\n\nWelcome to University Bazaar. This is to confirm that your account has been created\n\n" +
                "Your member id is: " + userMemberId + "\n" +
                "You password is: " + userPassword +"\n\n\n" +
                "Thank You,\nThe University Bazaar Team";
        try {
            GMailSender sender = new GMailSender(SENDER_EMAIL,context.getString(R.string.password));
            sender.sendMail(subject,messageBody,SENDER_EMAIL,receiverEmail);
            return true;
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            return false;
        }
    }

    public boolean sendEmail(){
        try {
            GMailSender sender = new GMailSender(SENDER_EMAIL,context.getString(R.string.password));
            sender.sendMail(emailSubject,emailBody,SENDER_EMAIL,receiverEmail);
            return true;
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            return false;
        }
    }
}
