package com.example.storageapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.io.FileInputStream;

public class ViewLogsActivity extends AppCompatActivity {

    TextView logsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);

        logsTextView = findViewById(R.id.logsTextView);

        try {
            FileInputStream fis = openFileInput("log.txt");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            String logs = new String(data, "UTF-8");
            logsTextView.setText(logs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
