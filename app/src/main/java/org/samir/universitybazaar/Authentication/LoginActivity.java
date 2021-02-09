package org.samir.universitybazaar.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Models.User;
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
        initViews();
        initBtnListeners();
    }

    private void initViews() {
        edtTxtMemberId = findViewById(R.id.edtTxtMemberId);
        edtTxtPassword = findViewById(R.id.edtTxtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtWarning = findViewById(R.id.txtWarning);
        txtForgotPassword = findViewById(R.id.txtForgotPassword);
    }

    private void initBtnListeners() {
        btnLogin.setOnClickListener(v->{handleLogin();});
        // TODO: 2/9/2021 link txtforgotpassword to forgotpassword activity.
        txtForgotPassword.setOnClickListener(v->{});
    }

    private void handleLogin(){
        String memberID = edtTxtMemberId.getText().toString().trim();
        String password = edtTxtPassword.getText().toString().trim();

        if(memberID.equals("") || password.equals("")){
            txtWarning.setVisibility(View.VISIBLE);
            txtWarning.setText("Please enter all fields");
        }else{
            databaseHelper = new DatabaseHelper(this);
            LoginUser loginUser = new LoginUser(memberID,password);
            Thread loginUserTask= new Thread(loginUser);
            loginUserTask.start();
        }
    }

    private class LoginUser implements Runnable{
        private String memberID;
        private String password;

        public LoginUser(String memberID, String password){
            this.memberID = memberID;
            this.password = password;
        }
        @Override
        public void run() {
            if(databaseHelper.loginUser(memberID,password)){
                //user is logged in. Redirect to home page.
                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtWarning.setVisibility(View.VISIBLE);
                        txtWarning.setText("Invalid member id or password");
                    }
                });
            }
        }
    }

}