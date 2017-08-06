package com.uwaterlooece458.projectece458;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SendKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_key);
        Intent intent = getIntent();
        String filename = intent.getExtras().getString("filename");
        TextView title = (TextView) findViewById(R.id.send_title);
        title.setText(filename);

    }
}
