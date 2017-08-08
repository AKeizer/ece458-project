package com.uwaterlooece458.projectece458;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class SendKeyActivity extends AppCompatActivity {
    private String keyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_key);
        Intent intent = getIntent();
        String filename = intent.getExtras().getString("filename");
        TextView title = (TextView) findViewById(R.id.send_title);
        title.setText(filename);
        File keysDir = new File(getFilesDir(), "keys");
        File keyFile = new File(keysDir, filename);
        ArrayList<Byte> byteList = new ArrayList<Byte>();
        byte[] bytes;
        try {
            FileInputStream fIn = new FileInputStream(keyFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int bytesRead = fIn.read(b);
            while(bytesRead != -1) {
                bos.write(b, 0, bytesRead);
                bytesRead = fIn.read(b);
            }
            bytes = bos.toByteArray();
            bos.close();
            fIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
