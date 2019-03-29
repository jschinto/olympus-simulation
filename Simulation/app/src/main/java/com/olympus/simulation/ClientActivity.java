package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
            setValues(client.getArrivalTime(), client.getProcedure());
        }
    }

    private void setValues(int arrivalTime, Procedure procedure) {
        EditText edit1 = findViewById(R.id.clientEdit1);
        TextView text2 = findViewById(R.id.clientText2);
        EditText edit2 = findViewById(R.id.clientEdit2);
        if (arrivalTime < 0  || procedure == null) {
            if (arrivalTime >= 0) {
                edit1.setText(Time.convertToString(arrivalTime));
            } else {
                edit1.setText("");
            }
            text2.setVisibility(View.VISIBLE);
            edit2.setVisibility(View.VISIBLE);
            edit2.setText("1");
            addSetup();
            return;
        }
        edit1.setText(Time.convertToString(arrivalTime));
        text2.setVisibility(View.INVISIBLE);
        edit2.setVisibility(View.INVISIBLE);
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

                /*
                Spinner spinner4 = findViewById(R.id.spinner4);
                Procedure procedure = simulation_manager.getProcedureByName((String)spinner4.getSelectedItem());*/
                Procedure procedure = new Procedure("Upper", 3,7);

                if (procedure == null) {
                    Toast.makeText(getApplicationContext(), "A customer needs a valid procedure", Toast.LENGTH_LONG).show();
                    return;
                }


                client = new Client(procedure, arrivalTime);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("client", client);
                returnIntent.putExtra("amount", amount);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();

                //deleting
            } else {
                client.setArrivalTime(-1);
                client.setProcedure(null);
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
/*
            Spinner spinner4 = findViewById(R.id.spinner4);
            Procedure procedure = simulation_manager.getProcedureByName((String)spinner4.getSelectedItem());*/
            Procedure procedure = new Procedure("Upper", 3, 7);

            if (procedure == null) {
                Toast.makeText(getApplicationContext(), "A customer needs a valid procedure", Toast.LENGTH_LONG).show();
                return;
            }

            client.setArrivalTime(arrivalTime);
            client.setProcedure(procedure);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("client", client);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else if (view.getId() == R.id.clientButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
