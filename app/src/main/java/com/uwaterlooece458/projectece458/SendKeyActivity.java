package com.uwaterlooece458.projectece458;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SendKeyActivity extends AppCompatActivity {
    private String keyData;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public static final java.util.UUID MY_UUID
            = java.util.UUID.fromString("B10E7007-CCD4-BBD7-1AAA-5EC0000000FF");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_key);
        Intent intent = getIntent();
        final String filename = intent.getExtras().getString("filename");
//        TextView title = (TextView) findViewById(R.id.send_title);
//        title.setText(filename);
        getSupportActionBar().setTitle(filename);
//        getActionBar().setTitle(filename);

        final Button sendButton = (Button) findViewById(R.id.sendKeyButton);


//        File keysDir = new File(getFilesDir(), "keys");
        File keysDir = getDir("keys", Context.MODE_PRIVATE);
        File keyFile = new File(keysDir, filename);
        ArrayList<Byte> byteList = new ArrayList<Byte>();
        final byte[] bytes;
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
            return;
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton.setVisibility(View.GONE);
                RelativeLayout spinner = (RelativeLayout) findViewById(R.id.sendingPanel);
                spinner.setVisibility(View.VISIBLE  );
                new AcceptThread(filename, bytes).start();
            }
        });
    }

    private byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;
        private byte[] mmBuffer; // mmBuffer store for the stream
        private byte[] contents;
        private String filename;
        private boolean performRun = true;

        public AcceptThread(String f, byte[] b) {
            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            filename = f;
            contents = b;
            Log.i("BLUETOOTHSECURITY", new String(b));
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("AcceptKeys", MY_UUID);
            } catch (IOException e) {
                Log.e("BLUETOOTHSECURITY", "Socket's listen() method failed", e);
            } catch (NullPointerException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SendKeyActivity.this, MainActivity.class);
                        intent.putExtra("BLUETOOTHERROR", "No Bluetooth, please connect your device via bluetooth");
                        startActivity(intent);
                    }
                });
                performRun = false;
            }
            mmServerSocket = tmp;
        }

        public void run() {
            if (!performRun) {
                Log.i("BLUETOOTHERROR", "Abort run");
                return;
            }
            BluetoothSocket socket = null;
            OutputStream tmpOut = null;
            InputStream tmpIn = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                    tmpOut = socket.getOutputStream();
                    tmpIn = socket.getInputStream();
                } catch (IOException e) {
                    Log.e("BLUETOOTHSECURITY", "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    //manageMyConnectedSocket(socket);
                    try{
                        mmBuffer = intToByteArray(filename.length());
                        tmpOut.write(mmBuffer);

                        mmBuffer = filename.getBytes();
                        tmpOut.write(mmBuffer);

                        mmBuffer = intToByteArray(contents.length);
                        tmpOut.write(mmBuffer);

                        tmpOut.write(contents);

                        Log.i("BLUETOOTHSECURITY", filename);
                        File keysDir = new File(getFilesDir(), "keys");
                        File keyFile = new File(keysDir, filename);
                        keyFile.delete();
//                        Log.i("BLUETOOTHSECURITY", s);
//                        mmBuffer = new byte[4];
//                        tmpIn.read(mmBuffer);
//                        Log.i("BLUETOOTHSECURITY", filename);
//                        cancel();

//                        Message readMsg = mHandler.obtainMessage(0, numBytes, -1, mmBuffer);
//                        readMsg.sendToTarget();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        mmServerSocket.close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SendKeyActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });

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
