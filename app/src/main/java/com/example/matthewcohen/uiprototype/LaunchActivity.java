package com.example.matthewcohen.uiprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void changeToConfirmationActivity(View v)
    {
        Intent changeToConfirmation = new Intent(this, ConfirmationActivity.class);
        startActivity(changeToConfirmation);
    }

    public void changeToHIWActivity(View v)
    {
        Intent changeToHIW = new Intent(this, HowItWorks.class);
        startActivity(changeToHIW);
    }

    public void changeToSettingsActivity(View v)
    {
        Intent changeToSettings = new Intent(this, Settings.class);
        startActivity(changeToSettings);
    }

}
