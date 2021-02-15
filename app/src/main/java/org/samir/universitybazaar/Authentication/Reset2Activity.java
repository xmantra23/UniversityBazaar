package org.samir.universitybazaar.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

public class Reset2Activity extends AppCompatActivity {
    private static final String TAG = "Reset2Activity";
    public static final String MEMBER_ID = "member.id";

    private EditText firstPasswordEditText, secondPasswordEditText;
    private TextView warningTextView;
    private Button reset2NextButton;
    private DatabaseHelper databaseHelper;
    private String memberId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset2);
        memberId = getIntent().getStringExtra(MEMBER_ID);
        initViews();//initialize all the elements in layout file.
        initListeners();//setup onclick listeners.
    }
    //initializes all the elements in layout file.
    private void initViews() {
        firstPasswordEditText = findViewById(R.id.resetNewPassword);
        secondPasswordEditText = findViewById(R.id.resetConfirmPassword);
        warningTextView = findViewById(R.id.reset2Warning);
        reset2NextButton = findViewById(R.id.reset2NextButton);
    }
    //sets up onclick listeners.
    private void initListeners() {
        reset2NextButton.setOnClickListener((view) -> {
            validatePassword();
        });
    }
    //input validation
    private void validatePassword() {
        Log.d(TAG, "handleReset2: started");
        new Thread(() -> {
            String firstPassword = firstPasswordEditText.getText().toString().trim();
            String secondPassword = secondPasswordEditText.getText().toString().trim();
            if ("".equals(firstPassword) || "".equals(secondPassword)) {
                runOnUiThread(() -> {
                    warningTextView.setVisibility(View.VISIBLE);
                    warningTextView.setText("Please enter all the fields");
                });
            } else if (firstPassword.length() < 8 || firstPassword.length() > 50) {
                runOnUiThread(() -> {
                    warningTextView.setVisibility(View.VISIBLE);
                    warningTextView.setText("password must be at-least 8 chars long");
                });

            } else if (!firstPassword.equals(secondPassword)) {
                runOnUiThread(() -> {
                    warningTextView.setVisibility(View.VISIBLE);
                    warningTextView.setText("Passwords donot match");
                });
            } else {
                databaseHelper = new DatabaseHelper(this);
                User user = databaseHelper.getUserByMemberId(memberId);
                if (user == null) {
                    runOnUiThread(() -> {
                        warningTextView.setVisibility(View.VISIBLE);
                        warningTextView.setText("User not found.");
                    });
                    return;
                }
                user.setPassword(firstPassword);
                databaseHelper.updateUser(user);
                runOnUiThread(() -> Toast.makeText(Reset2Activity.this, "Reset password Successful", Toast.LENGTH_SHORT).show());//Reset password Successful
                Intent intent = new Intent(Reset2Activity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).start();
    }
}
