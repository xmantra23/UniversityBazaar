package org.samir.universitybazaar.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.samir.universitybazaar.Database.DatabaseHelper;
import org.samir.universitybazaar.Models.User;
import org.samir.universitybazaar.R;

import static org.samir.universitybazaar.Authentication.Reset2Activity.MEMBER_ID;


public class Reset1Activity extends AppCompatActivity {
    private static final String TAG = "Reset1Activity";

    private EditText memberIdEditText, firstSecurityEditText, secondSecurityEditText, thirdSecurityEditText;
    private TextView warningTextView;
    private Button resetNextButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset1);
        initViews();//initialize all the elements in layout file.
        initListeners();//setup onclick listeners.
    }

    //initializes all the elements in layout file.
    private void initViews() {
        memberIdEditText = findViewById(R.id.resetTxtMemberId);
        firstSecurityEditText = findViewById(R.id.resetFirstSecurity);
        secondSecurityEditText = findViewById(R.id.resetSecondSecurity);
        thirdSecurityEditText = findViewById(R.id.resetThirdSecurity);
        warningTextView = findViewById(R.id.reset1Warning);
        resetNextButton = findViewById(R.id.reset1NextButton);
    }
    //sets up onclick listeners.
    private void initListeners() {
        resetNextButton.setOnClickListener((view) -> {
            validateSecurityQuestions();
        });
    }
    //input validation
    private void validateSecurityQuestions() {
        Log.d(TAG, "handleReset: started");//reset.
        //get the input from user and trim all whitespaces.
        new Thread(() -> {
            String memberId = memberIdEditText.getText().toString().trim();
            String firstSecurityQuestion = firstSecurityEditText.getText().toString().trim();
            String secondSecurityQuestion = secondSecurityEditText.getText().toString().trim();
            String thirdSecurityQuestion = thirdSecurityEditText.getText().toString().trim();
            if ("".equals(memberId) || "".equals(firstSecurityQuestion) || "".equals(secondSecurityQuestion) || "".equals(thirdSecurityQuestion)) {
                runOnUiThread(() -> {
                    warningTextView.setVisibility(View.VISIBLE);
                    warningTextView.setText("Please enter all the fields");
                });
            } else if (memberId.length() < 10 || !memberId.startsWith("1000")) {
                runOnUiThread(() -> {
                    warningTextView.setVisibility(View.VISIBLE);
                    warningTextView.setText("Invalid member ID.");
                });
            } else if (firstSecurityQuestion.length() < 10 || secondSecurityQuestion.length() < 10 || thirdSecurityQuestion.length() < 10) {
                runOnUiThread(() -> {
                    warningTextView.setVisibility(View.VISIBLE);
                    warningTextView.setText("Security answers must be at-least 10 chars long");
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
                if (!firstSecurityQuestion.equals(user.getFirstSecurityQuestion()) || !secondSecurityQuestion.equals(user.getSecondSecurityQuestion()) || !thirdSecurityQuestion.equals(user.getThirdSecurityQuestion())) {
                    runOnUiThread(() -> {
                        warningTextView.setVisibility(View.VISIBLE);
                        warningTextView.setText("Incorrect member id or security questions.");
                    });
                    return;
                }
                Intent intent = new Intent(Reset1Activity.this, Reset2Activity.class);
                intent.putExtra(MEMBER_ID, memberId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).start();

    }
}
