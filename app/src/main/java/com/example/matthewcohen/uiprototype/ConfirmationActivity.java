package com.example.matthewcohen.uiprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfirmationActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Button start = (Button) findViewById(R.id.second_start);
        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeToRunningActivity();
            }
        });
    }

    public void changeToRunningActivity()
    {
        Intent changeToRunning = new Intent(this, RunningActivity.class);
        startActivity(changeToRunning);
    }

}
