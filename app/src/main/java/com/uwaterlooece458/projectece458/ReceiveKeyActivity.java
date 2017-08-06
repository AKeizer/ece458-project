package com.uwaterlooece458.projectece458;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReceiveKeyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_key);

        final Button button = (Button) findViewById(R.id.cancel_get_key);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveKeyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        
    }
}
