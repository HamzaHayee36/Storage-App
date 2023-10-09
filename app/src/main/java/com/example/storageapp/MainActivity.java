package com.example.storageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    TextView signupText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        signupText = findViewById(R.id.signupText);
        loginButton = findViewById(R.id.loginButton);

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = editTextUsername.getText().toString();
                String enteredPIN = editTextPassword.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
                String savedUsername = sharedPreferences.getString("Username", "");
                String savedPIN = sharedPreferences.getString("PIN", "");

                if(enteredUsername.equals(savedUsername) && enteredPIN.equals(savedPIN)) {
                    Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // Navigate to ImageSelectActivity
                    Intent intent = new Intent(MainActivity.this, ImageSelectAndSendActivity.class);
                    startActivity(intent);
                    finish(); // Optional: Finish the MainActivity so user can't navigate back using back button

                } else {
                    Toast.makeText(MainActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
