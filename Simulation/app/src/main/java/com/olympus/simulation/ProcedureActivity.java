package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProcedureActivity extends AppCompatActivity {

    // 1 : name
    // 2 : expected time
    // 3 : doctor post procedure time

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
            setValues(procedure.getName(), procedure.getTime(), procedure.getDoctorPostProcedureTime());
        }
    }

    private void setValues(String name, int time, int doctorPostProcedureTime) {
        EditText edit1 = findViewById(R.id.procedureEdit1);
        EditText edit2 = findViewById(R.id.procedureEdit2);
        EditText edit3 = findViewById(R.id.procedureEdit3);
        if (time <= 0) {
            edit1.setText("");
            edit2.setText("");
            edit3.setText("");
            addSetup();
            return;
        }
        edit1.setText(name);
        edit2.setText(String.valueOf(time));
        edit3.setText(String.valueOf(doctorPostProcedureTime));
        viewSetup();
    }

    private void addSetup() {
        adding = true;
        Button buttonEdit = findViewById(R.id.procedureButtonEdit);
        Button buttonAddDelete = findViewById(R.id.procedureButtonAddDelete);
        buttonEdit.setVisibility(View.INVISIBLE);
        buttonAddDelete.setText("Add");
    }

    private void viewSetup() {
        adding = false;
        Button buttonEdit = findViewById(R.id.procedureButtonEdit);
        Button buttonAddDelete = findViewById(R.id.procedureButtonAddDelete);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonAddDelete.setText("Delete");
    }

    public void buttonClick(View view) {
        if (view.getId() == R.id.procedureButtonAddDelete) {
            if (adding) {
                String name = null;
                int time = 0;
                int doctorPostProcedureTime = 0;
                try {
                    EditText editData1 = findViewById(R.id.procedureEdit1);
                    name = editData1.getText().toString();
                    EditText editData2 = findViewById(R.id.procedureEdit2);
                    time = Integer.parseInt(editData2.getText().toString());
                    EditText editData3 = findViewById(R.id.procedureEdit3);
                    doctorPostProcedureTime = Integer.parseInt(editData3.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name == null || name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Name!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (time <= 0 || doctorPostProcedureTime <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Time Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                Procedure procedure = new Procedure(name, time, doctorPostProcedureTime);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("procedure", procedure);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();

                //deleting
            } else {
                procedure.setTime(0);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("procedure", procedure);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        } else if (view.getId() == R.id.procedureButtonEdit) {
            String name = null;
            int time = 0;
            int doctorPostProcedureTime = 0;
            try {
                EditText editData1 = findViewById(R.id.procedureEdit1);
                name = editData1.getText().toString();
                EditText editData2 = findViewById(R.id.procedureEdit2);
                time = Integer.parseInt(editData2.getText().toString());
                EditText editData3 = findViewById(R.id.procedureEdit3);
                doctorPostProcedureTime = Integer.parseInt(editData3.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (name == null || name.equals("")) {
                Toast.makeText(getApplicationContext(), "Invalid Name!", Toast.LENGTH_LONG).show();
                return;
            }
            if (time <= 0 || doctorPostProcedureTime <= 0) {
                Toast.makeText(getApplicationContext(), "Invalid Time Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            String oldName = procedure.getName();
            procedure.setName(name);
            procedure.setTime(time);
            procedure.setDoctorPostProcedureTime(doctorPostProcedureTime);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("procedure", procedure);
            returnIntent.putExtra("oldName", oldName);
            setResult(RESULT_OK, returnIntent);
            finish();


        } else if (view.getId() == R.id.procedureButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
