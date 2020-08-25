package com.example.matthewcohen.uiprototype;

import android.app.Service;
import android.content.*;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.telephony.SmsManager;
import android.widget.Toast;

public class CrashMonitor extends Service implements SensorEventListener{


    private SensorManager mSensorManager;
    private Sensor accel;
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private SensorEvent accelEvent;
    private int amountOfGs;
    private SmsManager sms;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
        System.out.println("Accuracy changed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mSensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        handleCommand(intent); //mine

        return START_STICKY;
    }

    public void handleCommand(Intent intent) {
        System.out.println("in handler");
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        accelEvent = event;
        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        // Do something with this sensor value.
        double end = Math.sqrt((Math.pow(ax, 2) + Math.pow(ay, 2) + Math.pow(az, 2)));
        if(end > 9.8 * amountOfGs) {
            //panic here cause they dead
            sms.sendTextMessage("9419930123", null, "test", null, null);
            System.out.println("accelaration large");
        }
    }


    protected void onResume() {

        mSensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
    }


    protected void onPause() {

        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onCreate() {
        amountOfGs = 1;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        System.out.println(accel);
        Toast.makeText(this, "Accelerometer Loaded", Toast.LENGTH_LONG).show();
        System.out.println("service made");
        sms = SmsManager.getDefault();
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        System.out.println("stopped");
    }

    /*@Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        System.out.println("started by user");
    }*/
}
