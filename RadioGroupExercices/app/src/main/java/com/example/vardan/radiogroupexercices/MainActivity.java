package com.example.vardan.radiogroupexercices;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 1001;
    private RadioButton internal;
    private EditText editText1;
    private EditText editText2;
    private RadioButton external;
    private String path;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.edit_text1);
        editText2 = findViewById(R.id.edit_text2);
        final Button button1 = findViewById(R.id.save_button);
        final Button button2 = findViewById(R.id.read_button);
        internal = findViewById(R.id.internal_button);
        external = findViewById(R.id.external_button);
        requestPermissions();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pathFile() != null) {
                    final File file = new File(pathFile());
                    try {
                        final FileOutputStream outputStream = new FileOutputStream(file);
                        outputStream.write(editText2.getText().toString().getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText1.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "line is empty", Toast.LENGTH_LONG).show();
                } else {
                    final File file1 = new File(pathFile());
                    if (pathFile() != null & file1.exists()) {
                        try {
                            final FileInputStream fileInputStream = new FileInputStream(file1);
                            final DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
                            String buffer;
                            while ((buffer = bufferedReader.readLine()) != null) {
                                data = buffer;
                            }
                            dataInputStream.close();
                            editText2.setText(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "file is empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String pathFile() {
        if (internal.isChecked()) {
            path = getFilesDir().getAbsolutePath() + "/" + editText1.getText().toString();

        } else if (external.isChecked()) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath() + "/" + editText1.getText().toString();
        }
        return path;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                }
        }
    }
}
