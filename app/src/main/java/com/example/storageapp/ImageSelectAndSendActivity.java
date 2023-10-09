package com.example.storageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageSelectAndSendActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;
    private static final long THRESHOLD_SIZE = 1 * 1024 * 1024;  // 1 MB
    Button selectImageButton, acceptSendButton, rejectButton;
    TextView errorMessageTextView;  // New TextView for error messages
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select_and_send);

        selectImageButton = findViewById(R.id.selectImageButton);
        acceptSendButton = findViewById(R.id.acceptSendButton);
        rejectButton = findViewById(R.id.rejectButton);
        errorMessageTextView = findViewById(R.id.errorMessageTextView);  // Initialize the TextView

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_IMAGE);
            }
        });

        acceptSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, selectedImageUri);
                sendIntent.setType("image/*");
                startActivity(Intent.createChooser(sendIntent, "Send Image"));

                // Log the details
                String logEntry = "Sent image of size: " + getSizeFromUri(selectedImageUri) + " bytes at " + System.currentTimeMillis();
                logDetails(logEntry);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptSendButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                errorMessageTextView.setVisibility(View.GONE);  // Hide the error message
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                long imageSize = getSizeFromUri(selectedImageUri);
                if (imageSize <= THRESHOLD_SIZE) {
                    acceptSendButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                    errorMessageTextView.setVisibility(View.GONE);  // Hide the error message
                } else {
                    errorMessageTextView.setText("File size is too large!");  // Set the error message
                    errorMessageTextView.setVisibility(View.VISIBLE);  // Show the error message
                }
            }
        }
    }

    private long getSizeFromUri(Uri uri) {
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            return imageStream.available();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void logDetails(String details) {
        try {
            FileOutputStream fos = openFileOutput("log.txt", MODE_APPEND);
            fos.write(details.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
