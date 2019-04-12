package com.olympus.simulation;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ScopeActivity extends AppCompatActivity {

    Scope scope;
    boolean adding;
    String[] scopeTypeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scope2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adding = true;
        Intent fromIntent = getIntent();
        scope = (Scope) fromIntent.getSerializableExtra("scope");
        scopeTypeNames = fromIntent.getStringArrayExtra("scopeTypeNames");
        if (scope != null) {
            setValues(scope.getType(), scope.getCleaningTime());
        }
    }

    public void setValues(Scope_Type scopeType, int cleaningTime) {
        Spinner edit1 = findViewById(R.id.scopeEdit1);
        EditText edit2 = findViewById(R.id.scopeEdit2);
        if (scopeTypeNames != null && scopeTypeNames.length > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, scopeTypeNames);
            edit1.setAdapter(adapter);
        }

        if (cleaningTime < 0) {
            edit2.setText("");
            addSetup();
            return;
        }

        edit2.setText("" + cleaningTime);

        viewSetup();
    }

    public void addSetup() {
        adding = true;
        Button buttonEdit = findViewById(R.id.scopeButtonUpdate);
        Button buttonAddDelete = findViewById(R.id.scopeButtonAddDelete);
        buttonEdit.setVisibility(View.INVISIBLE);
        buttonAddDelete.setText("Add");
    }

    public void viewSetup() {
        adding = false;
        Button buttonEdit = findViewById(R.id.scopeButtonUpdate);
        Button buttonAddDelete = findViewById(R.id.scopeButtonAddDelete);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonAddDelete.setText("Delete");
    }

    public void buttonClick(View view) {
        if (view.getId() == R.id.scopeButtonAddDelete) {

            if (adding) {
                String type = null;
                int clean = 0;
                try {
                    Spinner edit1 = findViewById(R.id.scopeEdit1);
                    type = edit1.getSelectedItem().toString();
                    EditText edit2 = findViewById(R.id.scopeEdit2);
                    clean = Integer.parseInt(edit2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (type == null || type.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Name Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (clean <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Price Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("scopeType", type);
                returnIntent.putExtra("cleaningTime", clean);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();

                //deleting
            } else {
                String type = "";
                int clean = -1;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("scopeType", type);
                returnIntent.putExtra("cleaningTime", clean);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        } else if (view.getId() == R.id.scopeButtonUpdate) {
            String type = null;
            int clean = 0;
            try {
                Spinner edit1 = findViewById(R.id.scopeEdit1);
                type = edit1.getSelectedItem().toString();
                EditText edit2 = findViewById(R.id.scopeEdit2);
                clean = Integer.parseInt(edit2.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (type == null || type.equals("")) {
                Toast.makeText(getApplicationContext(), "Invalid Name Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (clean <= 0) {
                Toast.makeText(getApplicationContext(), "Invalid Price Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra("scopeType", type);
            returnIntent.putExtra("cleaningTime", clean);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else if (view.getId() == R.id.scopeButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
