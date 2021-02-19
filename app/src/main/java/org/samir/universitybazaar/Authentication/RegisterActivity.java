package org.samir.universitybazaar.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.EmailService.EmailHelper;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

/**
 * @author Samir Shrestha
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity"; //tag for logging. not important.

    private EditText edtTxtMemberId, edtTxtEmail, edtTxtPassword, edtTxtConfirmPassword, edtTxtFirstSecurity, edtTxtSecondSecurity,edtTxtThirdSecurity;
    private Button btnRegister;
    private TextView txtWarning, txtLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews(); //initializing all the elements in the activity_register.xml layout file.
        initListeners(); //initializing on click listeners.
    }

    //initializes all the elements.
    private void initViews() {
        edtTxtMemberId = findViewById(R.id.edtTxtMemberId);
        edtTxtEmail = findViewById(R.id.edtTxtEmail);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        edtTxtConfirmPassword = findViewById(R.id.edtTxtConfirmPassword);
        edtTxtFirstSecurity = findViewById(R.id.edtTxtFirstSecurity);
        edtTxtSecondSecurity = findViewById(R.id.edtTxtSecondSecurity);
        edtTxtThirdSecurity = findViewById(R.id.edtTxtThirdSecurity);
        txtWarning = findViewById(R.id.txtWarning);
        txtLogin = findViewById(R.id.txtLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    //initializes all onclick listeners
    private void initListeners() {
        txtLogin.setOnClickListener((view)->{handleLoginText();});
        btnRegister.setOnClickListener((view)->{handleRegister();});

    }

    private void handleLoginText() {
        //redirect user to the login page.
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    private void handleRegister(){
        Log.d(TAG,"handleRegister: started"); //logging.
        //get the input from user and trim all whitespaces.
        String memberId = edtTxtMemberId.getText().toString().trim();
        String email = edtTxtEmail.getText().toString().trim();
        String password = edtTxtPassword.getText().toString().trim();
        String confirmPassword = edtTxtConfirmPassword.getText().toString().trim();
        String firstSecurityQuestion = edtTxtFirstSecurity.getText().toString().trim();
        String secondSecurityQuestion = edtTxtSecondSecurity.getText().toString().trim();
        String thirdSecurityQuestion = edtTxtThirdSecurity.getText().toString().trim();

        //input validation
        if(memberId.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("") || firstSecurityQuestion.equals("")||
        secondSecurityQuestion.equals("")|| thirdSecurityQuestion.equals("")){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Please enter all the fields");
        }else if(memberId.length() < 10 || !memberId.startsWith("1000")){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Invalid member ID.");
        }else if(password.length() < 8 || password.length() > 50){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("password must be at-least 8 chars long");
        }else if(!password.equals(confirmPassword)){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Passwords donot match");
        }else if(firstSecurityQuestion.length() < 10 || secondSecurityQuestion.length() < 10 || thirdSecurityQuestion.length() < 10){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Security answers must be at-least 10 chars long");
        }else if(!isValidEmailAddress(email)){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Please provide a valid email address");
        }else{
            //All the inputs are valid. proceed to registration.
            txtWarning.setVisibility(View.GONE);
            databaseHelper = new DatabaseHelper(this);

            //create a new user with all the user provided details.
            User user = new User();
            user.setMemberId(memberId);
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstSecurityQuestion(firstSecurityQuestion);
            user.setSecondSecurityQuestion(secondSecurityQuestion);
            user.setThirdSecurityQuestion(thirdSecurityQuestion);

            //do registration activity in a background thread process. See Register User for details.
            RegisterUser newUser = new RegisterUser(user);
            Thread thread = new Thread(newUser);
            thread.start(); //registration background task started.
        }
    }

    //validates if an email id is a valid email or not
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    //handles background process to register the user
    private class RegisterUser implements Runnable{
        private User user;
        public RegisterUser(User user){
            this.user = user;
        }

        @Override
        public void run() {
            if(!databaseHelper.doesUserExist(user)){
                // User hasn't registered before. Member id and email are unique.
                if(!databaseHelper.registerUser(user)){
                    //Registration failed. Display Error to the user.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtWarning.setVisibility(View.VISIBLE);
                            txtWarning.setText("Couldn't register. Please try again");
                        }
                    });
                }else{
                    //Registration successful.Send confirmation email, display success message and redirect to the login page.
                    EmailHelper email = new EmailHelper(RegisterActivity.this);
                    email.sendConfirmationEmail(user.getEmail(),user.getMemberId(),user.getPassword()); //send confirmation email.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
            }else{
                // User with the provided member id and email already exists. Display error message to the user.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtWarning.setVisibility(View.VISIBLE);
                        txtWarning.setText("User already exists");
                    }
                });
            }
        }
    }
}