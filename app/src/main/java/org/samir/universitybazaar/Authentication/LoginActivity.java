package org.samir.universitybazaar.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.R;
import org.samir.universitybazaar.TestActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText edtTxtMemberId, edtTxtPassword;
    private Button btnLogin;
    private TextView txtWarning, txtForgotPassword;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews(); //initialize all the elements in layout file.
        initBtnListeners(); //setup onclick listeners.
    }

    //initializes all the elements in layout file.
    private void initViews() {
        edtTxtMemberId = findViewById(R.id.edtTxtMemberId);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtWarning = findViewById(R.id.txtWarning);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
    }

    //sets up onclick listeners.
    private void initBtnListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        txtForgotPassword.setOnClickListener(v -> handleResetPassword());
    }

    //logs user into the system
    private void handleLogin() {
        String memberID = edtTxtMemberId.getText().toString().trim(); //get the member id
        String password = edtTxtPassword.getText().toString().trim(); //get the password

        if (memberID.equals("") || password.equals("")) { //if user doesn't provide member id or password display warning.
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Please enter all fields");
        } else {      //proceed to login
            databaseHelper = new DatabaseHelper(this); //Opens connection to the database.
            LoginUser loginUser = new LoginUser(memberID, password); // background interface class that will do the login task on a new thread.
            Thread loginUserTask = new Thread(loginUser);
            loginUserTask.start(); //starts a new thread to login the user. see below for implementation of the interface.
        }
    }

    private void handleResetPassword() {
        Intent intent = new Intent(LoginActivity.this, Reset1Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //clear the activity stack.
        startActivity(intent); //start home page activity.
    }

    //This class performs background task on a new thread to log user in.
    private class LoginUser implements Runnable {
        private String memberID;
        private String password;

        public LoginUser(String memberID, String password) {
            this.memberID = memberID;
            this.password = password;
        }

        @Override
        public void run() {
            if (databaseHelper.loginUser(memberID, password)) {
                //user is logged in. Redirect to home page.
                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);  //clear the activity stack.
                startActivity(intent); //start home page activity.
            } else {
                //login failed.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //display warning to the user.
                        txtWarning.setVisibility(View.VISIBLE);
                        txtWarning.setText("Invalid member id or password");
                    }
                });
            }
        }
    }

}