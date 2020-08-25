package com.example.matthewcohen.uiprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class HowItWorks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);
        TextView t3 = (TextView) findViewById(R.id.textViewDesc);
        t3.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
