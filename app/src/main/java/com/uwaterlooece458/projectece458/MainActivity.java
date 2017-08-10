package com.uwaterlooece458.projectece458;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent starterIntent = getIntent();
        if (starterIntent.hasExtra("BLUETOOTHERROR")) {
            String msg = starterIntent.getExtras().getString("BLUETOOTHERROR");
            View parentlayout = findViewById(R.id.mainContent);
            Snackbar.make(parentlayout, msg, Snackbar.LENGTH_LONG).setAction("CLOSE", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReceiveKeyActivity.class);
                startActivity(intent);
            }
        });

        String[] sample = {};

        File keysDir = new File(getFilesDir(), "keys");
        if (!keysDir.exists()) {
            keysDir.mkdir();
        }

        if (keysDir.list() == null) {
            mAdapter = new FileDisplayAdapter(sample, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SendKeyActivity.class);
                    TextView tv = (TextView) view;
                    String filename = tv.getText().toString();
                    intent.putExtra("filename", filename);
                    startActivity(intent);
                }
            });

        } else {
            mAdapter = new FileDisplayAdapter(keysDir.list(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, SendKeyActivity.class);
                    TextView tv = (TextView) view;
                    String filename = tv.getText().toString();
                    intent.putExtra("filename", filename);
                    startActivity(intent);
                }
            });
        }


        mRecyclerView.setAdapter(mAdapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
