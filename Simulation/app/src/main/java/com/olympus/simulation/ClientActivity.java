package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ClientActivity extends AppCompatActivity {

    // text fields with these numbers correspond to these attributes
    // 1 : arrival time
    // 2: amount to add
    // 3 : procedures to add

    Client client;

    private boolean adding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adding = true;
        Intent fromIntent = getIntent();
        client = (Client) fromIntent.getSerializableExtra("client");
        if (client != null) {
            setValues(client.getArrivalTime());
        }
    }

    private void setValues(int arrivalTime) {

    }
}
