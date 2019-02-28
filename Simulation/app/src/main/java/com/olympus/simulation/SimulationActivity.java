package com.olympus.simulation;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SimulationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

    }

    public void clickPlusIcon(View view) {
        if (view.getId() == R.id.plusIconWaiting) {
            LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
            ImageView clientImage = new ImageView(getApplicationContext());
            clientImage.setImageResource(R.drawable.client);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(78, 100);
            clientImage.setLayoutParams(layoutParams);

            linearLayoutClients.addView(clientImage);
            //TODO: add backend client here
        }
        else if (view.getId() == R.id.plusIconProcedure) {
            LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
            ImageView roomImage = new ImageView(getApplicationContext());
            roomImage.setImageResource(R.drawable.procedure_room);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
            roomImage.setLayoutParams(layoutParams);
            roomImage.setPadding(10,0,0,0);

            linearLayoutRooms.addView(roomImage);
            //TODO: add backend procedure room here
        }
    }
}
