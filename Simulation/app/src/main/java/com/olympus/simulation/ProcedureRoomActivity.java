package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ProcedureRoomActivity extends AppCompatActivity {

    ProcedureRoom procedureRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_room);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent fromIntent = getIntent();
        procedureRoom = (ProcedureRoom) fromIntent.getSerializableExtra("procedureRoom");
        if (procedureRoom != null) {
            setValues(procedureRoom.getTravelTime(), procedureRoom.getCooldownTime());
        }

    }

    private void setValues(int travelTime, int cooldownTime) {
        EditText edit1 = findViewById(R.id.procedureRoomEdit1);
        EditText edit2 = findViewById(R.id.procedureRoomEdit2);
        if (travelTime == 0 && cooldownTime == 0) {//default values for user to enter info when adding
            edit1.setText("");
            edit2.setText("");
            return;
        }
        edit1.setText(String.valueOf(travelTime));
        edit2.setText(String.valueOf(cooldownTime));
    }
}
