package org.samir.universitybazaar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import org.samir.universitybazaar.R;

public class CreateClubActivity extends AppCompatActivity {

    private EditText edtTxtTitle,edtTxtShortDesc,edtTxtLongDesc;
    private Button btnCancel,btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        initViews();
    }

    private void initViews() {
        edtTxtTitle = findViewById(R.id.edtTxtTitle);
        edtTxtShortDesc = findViewById(R.id.edtTxtShortDesc);
        edtTxtLongDesc = findViewById(R.id.edtTxtLongDesc);
        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);
    }


}