package com.olympus.simulation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SimulationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        LinearLayout linearLayoutWaiting = findViewById(R.id.LinearLayoutWaiting);
        LinearLayout linearLayoutProcedure = findViewById(R.id.LinearLayoutProcedure);
    }
}
