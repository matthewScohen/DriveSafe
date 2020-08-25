package com.example.matthewcohen.uiprototype;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button contactList = (Button) findViewById(R.id.contactListButton);
        contactList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeToContactListActivity();
            }
        });

    }

    public void changeToContactListActivity()
    {
        Intent changeToContactList = new Intent(this, ContactListActivity.class);
        startActivity(changeToContactList);
    }

    public void addContact()
    {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
    }

}
