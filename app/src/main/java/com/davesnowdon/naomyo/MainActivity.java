package com.davesnowdon.naomyo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aldebaran.qimessaging.Session;
import com.aldebaran.qimessaging.helpers.al.ALMemory;
import com.aldebaran.qimessaging.helpers.al.ALMotion;
import com.aldebaran.qimessaging.helpers.al.ALTextToSpeech;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.scanner.ScanActivity;

import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {
    private static  final String TAG = "MainActivity";

    private Context context;

    // NAO
    private Session naoSession;
    private ALMotion motionProxy;
    private ALMemory memoryProxy;
    private ALTextToSpeech ttsProxy;

    // MYO
    AbstractDeviceListener myoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // MYO initialisation
        Hub hub = Hub.getInstance();
        if (!hub.init(this)) {
            Log.e(TAG, "Could not initialize the Hub.");
            finish();
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onConnectNao(View view) {
        Log.i(TAG, "Connect clicked");
        EditText ipView = (EditText) findViewById(R.id.robot_ip_edit);
        final String addr = ipView.getText().toString();
        Log.i(TAG, "Robot IP text field: " + addr);

        if (isNotBlank(addr)) {
            doConnect(addr);
        } else {
            Log.i(TAG, "Robot address was blank");
            Toast.makeText(context, "Please enter a robot ip or a robot name", Toast.LENGTH_SHORT).show();
        }
    }

    private void doConnect(final String addr) {
        if (isNotBlank(addr)) {
            Log.i(TAG, "Robot address: " + addr);

            Thread connectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    naoSession = new Session();
                    try {
                        naoSession.connect("tcp://" + addr + ":9559").sync(500, TimeUnit.MILLISECONDS);

                        motionProxy = new ALMotion(naoSession);
                        ttsProxy = new ALTextToSpeech(naoSession);
                        ttsProxy.setAsynchronous(true);
                        memoryProxy = new ALMemory(naoSession);
                        Log.i(TAG, "Connected to "+addr);
                        ttsProxy.say("Hello android");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error", e);
                    }
                }
            });
            connectThread.start();
        }
    }

    public void onConnectMyo(View view) {
        Hub hub = Hub.getInstance();
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);

        myoListener = new MyoDeviceListener(context);
        hub.addListener(myoListener);
    }

    boolean isNotBlank(String str) {
        return (null != str) && !str.trim().equals("");
    }
}
