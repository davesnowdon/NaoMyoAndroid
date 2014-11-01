package com.davesnowdon.naomyo;

import android.content.Context;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

public class MyoDeviceListener extends AbstractDeviceListener {
    private Context context;

    public MyoDeviceListener(Context context) {
        this.context = context;
    }

    @Override
    public void onConnect(Myo myo, long timestamp) {
        Toast.makeText(context, "Myo Connected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnect(Myo myo, long timestamp) {
        Toast.makeText(context, "Myo Disconnected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        Toast.makeText(context, "Pose: " + pose, Toast.LENGTH_SHORT).show();

        //TODO: Do something awesome.
    }
}
