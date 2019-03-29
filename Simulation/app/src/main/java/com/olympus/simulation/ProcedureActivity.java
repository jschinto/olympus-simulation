package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

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
        EditText edit1 = findViewById(R.id.procedureEdit1);
        EditText edit2 = findViewById(R.id.procedureEdit2);
        EditText edit3 = findViewById(R.id.procedureEdit3);
        if (minTime <= 0 || maxTime <= 0 || minTime > maxTime) {
            edit1.setText("");
            edit2.setText("");
            edit3.setText("");
            addSetup();
            return;
        }
    }

    private void addSetup() {
        adding = true;
    }

    private void viewSetup() {
        adding = false;
    }
}
