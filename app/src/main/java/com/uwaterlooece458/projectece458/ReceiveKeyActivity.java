package com.uwaterlooece458.projectece458;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;

public class ReceiveKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_key);

        Button button = (Button) findViewById(R.id.cancel_get_key);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveKeyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button fakeSuccess = (Button) findViewById(R.id.fake_success);
        fakeSuccess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveIncomingKey("Saved File Name", "Key!");
                Intent intent = new Intent(ReceiveKeyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveIncomingKey(String filename, String contents) {
//        File file = new File(this.context.getFilesDir(), filename);
        File keysDir = new File(getFilesDir(), "keys");
        File keyFile = new File(keysDir, filename);
        try {
            keyFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(keyFile);
            outputStream.write(contents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
