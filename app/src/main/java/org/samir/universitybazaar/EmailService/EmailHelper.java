package org.samir.universitybazaar.EmailService;

import android.util.Log;

public class EmailHelper {
    private static final String SENDER_EMAIL = "universitybazaar.team4@gmail.com";
    private static final String EMAIL_PASSWORD = "ArlingtonTexas19";
    private String receiverEmail;
    private String emailSubject;
    private String emailBody;


    public EmailHelper(String receiverEmail,String emailSubject, String emailBody){
        this.receiverEmail = receiverEmail;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
    }

    public EmailHelper(){

    }

    public void sendConfirmationEmail(String receiverEmail,String userMemberId,String userPassword){
        String subject = "Welcome to University Bazaar";
        String messageBody = "Dear New User,\n\nWelcome to University Bazaar. This is to confirm that your account has been created\n\n" +
                "Your member id is: " + userMemberId + "\n" +
                "You password is: " + userPassword +"\n\n\n" +
                "Thank You,\nThe University Bazaar Team";
        try {
            GMailSender sender = new GMailSender(SENDER_EMAIL,EMAIL_PASSWORD);
            sender.sendMail(subject,messageBody,SENDER_EMAIL,receiverEmail);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }
}
