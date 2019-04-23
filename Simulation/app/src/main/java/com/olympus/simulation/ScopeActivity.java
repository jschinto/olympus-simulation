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
import android.widget.TextView;
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
            setValues(scope.getType());
        }
    }

    public void setValues(Scope_Type scopeType) {
        TextView textTitle = findViewById(R.id.scopeTitle);
        textTitle.setText("Scope "+scope.getId());

        Spinner edit1 = findViewById(R.id.scopeEdit1);
        EditText edit2 = findViewById(R.id.scopeEdit2);
        if (scopeTypeNames != null && scopeTypeNames.length > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, scopeTypeNames);
            edit1.setAdapter(adapter);
        }

        if (scopeType == null) {
            edit2.setVisibility(View.VISIBLE);
            TextView text2 = findViewById(R.id.scopeText2);
            text2.setVisibility(View.VISIBLE);
            addSetup();
            return;
        }

        for (int i=0; i < scopeTypeNames.length; i++) {
            if (scopeTypeNames[i].equals(scope.getType().getName())) {
                edit1.setSelection(i);
                break;
            }
        }
        edit2.setVisibility(View.INVISIBLE);
        TextView text2 = findViewById(R.id.scopeText2);
        text2.setVisibility(View.INVISIBLE);
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
                int amount = 0;
                try {
                    Spinner edit1 = findViewById(R.id.scopeEdit1);
                    type = edit1.getSelectedItem().toString();
                    EditText edit2 = findViewById(R.id.scopeEdit2);
                    amount = Integer.parseInt(edit2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Scopes need a type", Toast.LENGTH_LONG).show();
                    return;
                }
                if (type == null || type.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Name Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (amount <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Amount Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("scopeType", type);
                returnIntent.putExtra("amount", amount);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();

                //deleting
            } else {
                String type = null;
                int amount = -1;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("scopeType", type);
                returnIntent.putExtra("amount", amount);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        } else if (view.getId() == R.id.scopeButtonUpdate) {
            String type = null;
            try {
                Spinner edit1 = findViewById(R.id.scopeEdit1);
                type = edit1.getSelectedItem().toString();
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Scopes need a type", Toast.LENGTH_LONG).show();
                return;
            }
            if (type == null || type.equals("")) {
                Toast.makeText(getApplicationContext(), "Invalid Name Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra("scopeType", type);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else if (view.getId() == R.id.scopeButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
