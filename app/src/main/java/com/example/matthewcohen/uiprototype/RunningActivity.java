package com.example.matthewcohen.uiprototype;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class RunningActivity extends AppCompatActivity
{
    final double eyeClosedThreshold = 0.40;
    final double drowsyThreshold = 0.15;
    final double stopAlarmThreshold = 0.145;
    PERCLOSList eyeData;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        eyeData = new PERCLOSList(3600); //3600 is 2 minutes at 30fps
        startService(new Intent(this, CrashMonitor.class));

        createCameraSource();
        changeText(context.getString(R.string.FaceLost));
    }

    public void changeText(String s)
    {
        TextView textView = (TextView) findViewById(R.id.runningTextView);
        textView.setText(s);
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public void createCameraSource()
    {
        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(true)
                .setProminentFaceOnly(true)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).build();
        if(!faceDetector.isOperational())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Error setting up face detection").show();
            return;
        }
        faceDetector.setProcessor(new LargestFaceFocusingProcessor(faceDetector, new FaceTracker()));
        try
        {
            CameraSource cameraSource = new CameraSource.Builder(getApplicationContext(), faceDetector)
                    .setRequestedPreviewSize(640, 480)
                    .setFacing(CameraSource.CAMERA_FACING_FRONT)
                    .setRequestedFps(30.0f).build().start();
        }catch(SecurityException e) //If missing permissions
        {
            requestCameraPermission();
        }catch(IOException e)
        {

        }
    }

    public void requestCameraPermission()
    {
        //This does not work yet, you can manually turn on permissions in settings > app > UIPrototype > permissions
        System.out.println("Requesting Permissions");
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 0);
    }


    private class FaceTracker extends Tracker<Face>
    {
        private MediaPlayer mp;

        public FaceTracker()
        {
            mp = MediaPlayer.create(context, R.raw.alarm);
            mp.setLooping(true);
        }

        @Override
        public void onNewItem (int id, Face item)
        {
            System.out.println("Face Found");
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    changeText(RunningActivity.this.getString(R.string.FaceFound));
                }
            });
        }

        @Override
        public void onMissing(Detector.Detections<Face> detections)
        {
            System.out.println("Face Lost");
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    changeText(RunningActivity.this.getString(R.string.FaceLost));
                }
            });

        }

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face item)
        {
            System.out.println("Left Eye: " + item.getIsLeftEyeOpenProbability());
            System.out.println("Right Eye: " + item.getIsRightEyeOpenProbability());
            //If both eyes are visible
            if((item.getIsLeftEyeOpenProbability() >= 0 && item.getIsRightEyeOpenProbability() >= 0))
            {
                //If either eye is closed
                if(item.getIsLeftEyeOpenProbability() <= eyeClosedThreshold || item.getIsRightEyeOpenProbability() <= eyeClosedThreshold)
                    eyeData.add(new Boolean(false));
                else
                    eyeData.add(new Boolean(true));
            }
            System.out.println(eyeData.getPERCLOS());
            if(eyeData.getCurrentDataSize() >= eyeData.getMaxDataSize() / 8 && eyeData.getPERCLOS() > drowsyThreshold) //when 2 minutes have passed since start and they are drowsy, play sound
            {
                //play sound. Can add more functionality in here
                System.out.println("Playing sound");
                mp.start();
            }
            if(mp.isPlaying() && eyeData.getPERCLOS() < stopAlarmThreshold)
            {
                //Stop sound when PERCLOS drops below stopAlarmThreshold
                System.out.println("Stopping sound");
                mp.pause();
            }
        }

    }
}
