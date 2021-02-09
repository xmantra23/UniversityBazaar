package org.samir.universitybazaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.samir.universitybazaar.Authentication.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(v->{
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}