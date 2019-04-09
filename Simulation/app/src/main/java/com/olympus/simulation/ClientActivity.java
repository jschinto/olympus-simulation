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
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ClientActivity extends AppCompatActivity {

    // text fields with these numbers correspond to these attributes
    // 1 : arrival time
    // 2: amount to add
    // 3 : procedures added
    // 4 : procedures to add

    Client client;
    String[] procedures;

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
        if (client != null) {
            setValues(client.getArrivalTime(), client.getProcedureList());
        }
    }

    private void setValues(int arrivalTime, ArrayList<Procedure> procedureList) {
        EditText edit1 = findViewById(R.id.clientEdit1);
        TextView text2 = findViewById(R.id.clientText2);
        EditText edit2 = findViewById(R.id.clientEdit2);

        TextView text3 = findViewById(R.id.clientText3);
        Spinner spinner3 = findViewById(R.id.clientSpinner3);
        TextView text4 = findViewById(R.id.clientText4);
        LinearLayout linearLayout4 = findViewById(R.id.clientCheckbox4);

        if (arrivalTime < 0  || procedureList == null) {
            if (arrivalTime >= 0) {
                edit1.setText(Time.convertToString(arrivalTime));
            } else {
                edit1.setText("");
            }
            text2.setVisibility(View.VISIBLE);
            edit2.setVisibility(View.VISIBLE);
            edit2.setText("1");
            text3.setVisibility(View.INVISIBLE);
            spinner3.setVisibility(View.INVISIBLE);
            text4.setVisibility(View.VISIBLE);
            linearLayout4.setVisibility(View.VISIBLE);

            LinearLayout clientCheckboxGroup4 = findViewById(R.id.clientCheckbox4);
            for (int i=0; i < procedures.length; i++) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(procedures[i]);
                clientCheckboxGroup4.addView(checkBox);
            }
            addSetup();
            return;
        }
        edit1.setText(Time.convertToString(arrivalTime));
        text2.setVisibility(View.INVISIBLE);
        edit2.setVisibility(View.INVISIBLE);

        text3.setVisibility(View.VISIBLE);
        spinner3.setVisibility(View.VISIBLE);
        text4.setVisibility(View.INVISIBLE);
        linearLayout4.setVisibility(View.INVISIBLE);
        String[] procedureNames = new String[procedureList.size()];
        for (int i=0; i < procedureNames.length; i++) {
            procedureNames[i] = procedureList.get(i).getName();
        }
        if (procedureList != null && procedureList.size() > 0) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, procedureNames);
            spinner3.setAdapter(adapter);
        }
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
                    EditText edit1 = findViewById(R.id.clientEdit1);
                    arrivalString = edit1.getText().toString();
                    EditText edit2 = findViewById(R.id.clientEdit2);
                    amount = Integer.parseInt(edit2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!Time.validateTime(arrivalString)) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                int arrivalTime = Time.convertToInt(arrivalString);

                if (arrivalTime < 0 || amount <= 0 || amount >= 101) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                //get checked procedures from checkboxes
                LinearLayout clientCheckbox4 = findViewById(R.id.clientCheckbox4);
                ArrayList<String> procedureNames = new ArrayList<String>();
                for (int i=0; i < clientCheckbox4.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox)clientCheckbox4.getChildAt(i);
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
                EditText edit1 = findViewById(R.id.clientEdit1);
                arrivalString = edit1.getText().toString();
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            if(!Time.validateTime(arrivalString)) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            int arrivalTime = Time.convertToInt(arrivalString);

            if (arrivalTime < 0) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }


            //get checked procedures from checkboxes
            LinearLayout clientCheckbox4 = findViewById(R.id.clientCheckbox4);
            ArrayList<String> procedureNames = new ArrayList<String>();
            for (int i=0; i < clientCheckbox4.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox)clientCheckbox4.getChildAt(i);
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
            returnIntent.putExtra("procedures", procedureNames.toArray());
            setResult(RESULT_OK, returnIntent);
            finish();

        } else if (view.getId() == R.id.clientButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
