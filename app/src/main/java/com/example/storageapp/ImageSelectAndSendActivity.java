package com.example.storageapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageSelectAndSendActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1;
    private static final long THRESHOLD_SIZE = 1 * 1024 * 1024;  // 1 MB
    Button selectImageButton, acceptSendButton, rejectButton, viewLogsButton;
    TextView errorMessageTextView;
    ImageView selectedImageView;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select_and_send);

        selectImageButton = findViewById(R.id.selectImageButton);
        acceptSendButton = findViewById(R.id.acceptSendButton);
        rejectButton = findViewById(R.id.rejectButton);
        errorMessageTextView = findViewById(R.id.errorMessageTextView);
        selectedImageView = findViewById(R.id.selectedImageView);
        viewLogsButton = findViewById(R.id.viewLogsButton);

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

                long timestamp = System.currentTimeMillis();
                String logEntry = "\nSent image of size: " + getSizeFromUri(selectedImageUri) + " bytes at " + formatTimestamp(timestamp);
                logDetails(logEntry);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptSendButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                errorMessageTextView.setVisibility(View.GONE);
                selectedImageView.setImageDrawable(null);
            }
        });

        viewLogsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageSelectAndSendActivity.this, ViewLogsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                selectedImageView.setImageURI(selectedImageUri);
                long imageSize = getSizeFromUri(selectedImageUri);
                if (imageSize <= THRESHOLD_SIZE) {
                    acceptSendButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                    errorMessageTextView.setVisibility(View.GONE);
                } else {
                    errorMessageTextView.setText("File size is too large!");
                    errorMessageTextView.setVisibility(View.VISIBLE);
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

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp);
        return sdf.format(date);
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
