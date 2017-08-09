package com.uwaterlooece458.projectece458;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReceiveKeyActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public static final java.util.UUID MY_UUID
            = java.util.UUID.fromString("B10E7007-CCD4-BBD7-1AAA-5EC000000017");

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

        new AcceptThread().start();
//        Button fakeSuccess = (Button) findViewById(R.id.fake_success);
//        fakeSuccess.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                saveIncomingKey("Saved File Name", "Key!");
//                Intent intent = new Intent(ReceiveKeyActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

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

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("AcceptKeys", MY_UUID);
            } catch (IOException e) {
                Log.e("BLUETOOTHSECURITY", "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e("BLUETOOTHSECURITY", "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    //manageMyConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e("BLUETOOTHSECURITY", "Socket's close() method failed", e);
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("BLUETOOTHSECURITY", "Could not close the connect socket", e);
            }
        }
    }
}
