package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {

    // text fields with these numbers correspond to these attributes
    // 1 : arrival time
    // 2: amount to add
    // 3 : procedures

    Client client;
    String[] procedures;
    int startTime;
    int endTime;

    private boolean adding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adding = true;
        Intent fromIntent = getIntent();
        client = (Client) fromIntent.getSerializableExtra("client");
        procedures = fromIntent.getStringArrayExtra("procedures");
        startTime = fromIntent.getIntExtra("startTime", 0);
        endTime = fromIntent.getIntExtra("endTime", Time.convertToInt("23:59"));

        if (client != null) {
            setValues(client.getArrivalTime(), client.getProcedureList());
        }
    }

    private void setValues(int arrivalTime, ArrayList<Procedure> procedureList) {
        TimePicker edit1 = findViewById(R.id.clientEdit1);
        edit1.setIs24HourView(true);
        TextView text2 = findViewById(R.id.clientText2);
        EditText edit2 = findViewById(R.id.clientEdit2);

        LinearLayout clientCheckbox3 = findViewById(R.id.clientCheckbox3);
        if (procedures != null) {
            for (int i = 0; i < procedures.length; i++) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(procedures[i]);
                checkBox.setChecked(false);
                clientCheckbox3.addView(checkBox);
            }
        }

        if (arrivalTime < 0 || procedureList == null) {
            text2.setVisibility(View.VISIBLE);
            edit2.setVisibility(View.VISIBLE);
            if (arrivalTime >= 0) {
                edit1.setCurrentHour(arrivalTime / 60);
                edit1.setCurrentMinute(arrivalTime % 60);
            } else {
                //edit1.setText("");
            }
            edit2.setText("1");

            TextView textTitle = findViewById(R.id.clientTitle);
            textTitle.setText("Patient");
            addSetup();
            return;
        }

        text2.setVisibility(View.INVISIBLE);
        edit2.setVisibility(View.INVISIBLE);
        edit1.setCurrentHour(arrivalTime / 60);
        edit1.setCurrentMinute(arrivalTime % 60);

        ArrayList<String> procedureListNames = new ArrayList<String>();
        for (int i = 0; i < procedureList.size(); i++) {
            procedureListNames.add(procedureList.get(i).getName());
        }
        for (int i = 0; i < procedures.length; i++) {
            if (procedureListNames.contains(procedures[i])) {
                CheckBox checkBox = (CheckBox) clientCheckbox3.getChildAt(i);
                checkBox.setChecked(true);
            }
        }
        TextView textTitle = findViewById(R.id.clientTitle);
        textTitle.setText("Patient " + client.getId());
        viewSetup();
    }

    private void addSetup() {
        adding = true;
        Button buttonEdit = findViewById(R.id.clientButtonEdit);
        Button buttonAddDelete = findViewById(R.id.clientButtonAddDelete);
        buttonEdit.setVisibility(View.INVISIBLE);
        buttonAddDelete.setText("Add");
    }

    private void viewSetup() {
        adding = false;
        Button buttonEdit = findViewById(R.id.clientButtonEdit);
        Button buttonAddDelete = findViewById(R.id.clientButtonAddDelete);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonAddDelete.setText("Delete");
    }

    public void buttonClick(View view) {
        if (view.getId() == R.id.clientButtonAddDelete) {

            if (adding) {
                String arrivalString = "";
                int amount = 0;
                try {
                    TimePicker edit1 = findViewById(R.id.clientEdit1);
                    String hr = (edit1.getCurrentHour()) + "";
                    if (hr.length() == 1) {
                        hr = "0" + hr;
                    }
                    String min = (edit1.getCurrentMinute()) + "";
                    if (min.length() == 1) {
                        min = "0" + min;
                    }
                    arrivalString = hr + ":" + min;
                    EditText edit2 = findViewById(R.id.clientEdit2);
                    amount = Integer.parseInt(edit2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Time.validateTime(arrivalString)) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                int arrivalTime = Time.convertToInt(arrivalString);

                if (arrivalTime < startTime || arrivalTime > endTime || amount <= 0 || amount >= 101) {
                    Toast.makeText(getApplicationContext(), "Invalid Time or Amount Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                //get checked procedures from checkboxes
                LinearLayout clientCheckbox4 = findViewById(R.id.clientCheckbox3);
                ArrayList<String> procedureNames = new ArrayList<String>();
                for (int i = 0; i < clientCheckbox4.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) clientCheckbox4.getChildAt(i);
                    if (checkBox.isChecked()) {
                        String name = checkBox.getText().toString();
                        procedureNames.add(name);
                    }
                }

                if (procedureNames == null || procedureNames.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "A customer needs a valid procedure", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent returnIntent = new Intent();
                String[] procedureNamesArray = new String[procedureNames.size()];
                returnIntent.putExtra("procedures", procedureNames.toArray(procedureNamesArray));
                returnIntent.putExtra("arrivalTime", arrivalTime);
                returnIntent.putExtra("amount", amount);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();

                //deleting
            } else {
                client.setArrivalTime(-1);
                client.setProcedureList(null);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("client", client);
                setResult(RESULT_OK, returnIntent);
                finish();

            }

        } else if (view.getId() == R.id.clientButtonEdit) {
            String arrivalString = "";
            try {
                TimePicker edit1 = findViewById(R.id.clientEdit1);
                String hr = (edit1.getCurrentHour()) + "";
                if (hr.length() == 1) {
                    hr = "0" + hr;
                }
                String min = (edit1.getCurrentMinute()) + "";
                if (min.length() == 1) {
                    min = "0" + min;
                }
                arrivalString = hr + ":" + min;
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            if (!Time.validateTime(arrivalString)) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            int arrivalTime = Time.convertToInt(arrivalString);

            if (arrivalTime < startTime || arrivalTime > endTime) {
                Toast.makeText(getApplicationContext(), "Invalid Arrival Time!", Toast.LENGTH_LONG).show();
                return;
            }


            //get checked procedures from checkboxes
            LinearLayout clientCheckbox3 = findViewById(R.id.clientCheckbox3);
            ArrayList<String> procedureNames = new ArrayList<String>();
            for (int i = 0; i < clientCheckbox3.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) clientCheckbox3.getChildAt(i);
                if (checkBox.isChecked()) {
                    String name = checkBox.getText().toString();
                    procedureNames.add(name);
                }
            }

            if (procedureNames == null || procedureNames.size() <= 0) {
                Toast.makeText(getApplicationContext(), "A customer needs a valid procedure", Toast.LENGTH_LONG).show();
                return;
            }

            client.setArrivalTime(arrivalTime);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("client", client);
            String[] procedureNamesArray = new String[procedureNames.size()];
            returnIntent.putExtra("procedures", procedureNames.toArray(procedureNamesArray));
            setResult(RESULT_OK, returnIntent);
            finish();

        } else if (view.getId() == R.id.clientButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
