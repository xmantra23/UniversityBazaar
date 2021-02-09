package org.samir.universitybazaar.Authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.samir.universitybazaar.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText edtTxtMemberId, edtTxtEmail, edtTxtPassword, edtTxtConfirmPassword, edtTxtFirstSecurity, edtTxtSecondSecurity,edtTxtThirdSecurity;
    private Button btnRegister;
    private TextView txtWarning, txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews(); //initializing all the elements in the activity_register.xml layout file.
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
    }


}