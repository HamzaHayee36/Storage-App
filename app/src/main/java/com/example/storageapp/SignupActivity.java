package com.example.storageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    EditText editTextUsernameSignup, editTextPasswordSignup;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsernameSignup = findViewById(R.id.editTextUsernameSignup);
        editTextPasswordSignup = findViewById(R.id.editTextPasswordSignup);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsernameSignup.getText().toString();
                String pin = editTextPasswordSignup.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Username", username);
                editor.putString("PIN", pin);
                editor.apply();

                Toast.makeText(SignupActivity.this, "Successfully Signed Up!", Toast.LENGTH_SHORT).show();
                finish();  // Close SignupActivity and return to MainActivity
            }
        });
    }
}
