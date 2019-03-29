package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProcedureActivity extends AppCompatActivity {

    Procedure procedure;
    private boolean adding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adding = true;
        Intent fromIntent = getIntent();
        procedure = (Procedure) fromIntent.getSerializableExtra("procedure");
        if (procedure != null) {
            setValues(procedure.getMinTime(), procedure.getMaxTime());
        }
    }

    private void setValues(int minTime, int maxTime) {
        
    }
}
