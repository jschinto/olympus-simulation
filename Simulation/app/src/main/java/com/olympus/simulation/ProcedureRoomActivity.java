package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProcedureRoomActivity extends AppCompatActivity {

    // text fields with these numbers correspond to these attributes
    // 1 : travel time
    // 2: cooldown time


    ProcedureRoom procedureRoom; //procedure room being represented
    private boolean adding; //true if adding new element, false if viewing existing element


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure_room);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adding = true;
        Intent fromIntent = getIntent();
        procedureRoom = (ProcedureRoom) fromIntent.getSerializableExtra("procedureRoom");
        if (procedureRoom != null) {
            setValues(procedureRoom.getTravelTime(), procedureRoom.getCooldownTime());
        }

    }

    private void setValues(int travelTime, int cooldownTime) {
        EditText edit1 = findViewById(R.id.procedureRoomEdit1);
        EditText edit2 = findViewById(R.id.procedureRoomEdit2);
        if (travelTime <= 0 && cooldownTime <= 0) {//default value 0 to indicate user is in adding mode
            edit1.setText("");
            edit2.setText("");
            addSetup();
            return;
        }
        edit1.setText(String.valueOf(travelTime));
        edit2.setText(String.valueOf(cooldownTime));
        viewSetup();
    }

    private void addSetup() {
        adding = true;
        Button buttonEdit = findViewById(R.id.procedureRoomButtonEdit);
        Button buttonAddDelete = findViewById(R.id.procedureRoomButtonAddDelete);
        buttonEdit.setVisibility(View.INVISIBLE);
        buttonAddDelete.setText("Add");
    }

    private void viewSetup() {
        adding = false;
        Button buttonEdit = findViewById(R.id.procedureRoomButtonEdit);
        Button buttonAddDelete = findViewById(R.id.procedureRoomButtonAddDelete);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonAddDelete.setText("Delete");
    }

    public void buttonClick(View view) {
        if (view.getId() == R.id.procedureRoomButtonAddDelete) {
            if (adding) {
                int travelTime = 0;
                int cooldownTime = 0;
                try {
                    EditText edit1 = findViewById(R.id.procedureRoomEdit1);
                    travelTime = Integer.parseInt(edit1.getText().toString());
                    EditText edit2 = findViewById(R.id.procedureRoomEdit2);
                    cooldownTime = Integer.parseInt(edit2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (cooldownTime <= 0 || travelTime <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("procedureRoom", procedureRoom);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();

            } else {//deleting
                procedureRoom.setCooldownTime(0);
                procedureRoom.setTravelTime(0);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("procedureRoom", procedureRoom);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

        } else if (view.getId() == R.id.procedureRoomButtonEdit) {
            int travelTime = 0;
            int cooldownTime = 0;
            try {
                EditText edit1 = findViewById(R.id.procedureRoomEdit1);
                travelTime = Integer.parseInt(edit1.getText().toString());
                EditText edit2 = findViewById(R.id.procedureRoomEdit2);
                cooldownTime = Integer.parseInt(edit2.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (cooldownTime <= 0 || travelTime <= 0) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            procedureRoom.setTravelTime(travelTime);
            procedureRoom.setCooldownTime(cooldownTime);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("procedureRoom", procedureRoom);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else if (view.getId() == R.id.procedureRoomButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
