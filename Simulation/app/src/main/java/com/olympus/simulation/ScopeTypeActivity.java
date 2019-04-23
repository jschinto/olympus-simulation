package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ScopeTypeActivity extends AppCompatActivity {

    // 1 : name
    // 2 : price
    // 3 : procedure list


    Scope_Type scope_type;
    boolean adding;
    String[] procedureNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scope_type);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adding = true;
        Intent fromIntent = getIntent();
        scope_type = (Scope_Type) fromIntent.getSerializableExtra("scopeType");
        procedureNames = fromIntent.getStringArrayExtra("procedureNames");
        if (scope_type != null) {
            setValues(scope_type.getName(),scope_type.getCleaningTime(), scope_type.getPrice(), scope_type.getProcedureList());
        }
    }

    public void setValues(String name, int cleaningTime, int price, ArrayList<Procedure> procedureList) {
        EditText edit1 = findViewById(R.id.scopeTypeEdit1);
        EditText edit2 = findViewById(R.id.scopeTypeEdit2);
        EditText edit4 = findViewById(R.id.scopeTypeEdit4);
        LinearLayout checkbox3  = findViewById(R.id.scopeTypeCheckbox3);
        if (procedureNames != null) {
            for (int i = 0; i < procedureNames.length; i++) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(procedureNames[i]);
                checkBox.setChecked(false);
                checkbox3.addView(checkBox);
            }
        }

        if (price <= 0 || procedureList == null) { //adding
            edit1.setText("");
            edit2.setText("");
            edit4.setText("");
            addSetup();
            return;
        }

        edit1.setText(name);
        edit2.setText(String.valueOf(price));
        edit4.setText(String.valueOf(cleaningTime));
        //viewing

        ArrayList<String> procedureListNames = new ArrayList<String>();
        for (int i=0; i < procedureList.size(); i++) {
            procedureListNames.add(procedureList.get(i).getName());
        }
        for (int i=0; i < procedureNames.length; i++) {
            if (procedureListNames.contains(procedureNames[i])) {
                CheckBox checkBox = (CheckBox) checkbox3.getChildAt(i);
                checkBox.setChecked(true);
            }
        }
        viewSetup();
    }

    public void addSetup() {
        adding = true;
        Button buttonEdit = findViewById(R.id.scopeTypeButtonEdit);
        Button buttonAddDelete = findViewById(R.id.scopeTypeButtonAddDelete);
        buttonEdit.setVisibility(View.INVISIBLE);
        buttonAddDelete.setText("Add");
    }

    public void viewSetup() {
        adding = false;
        Button buttonEdit = findViewById(R.id.scopeTypeButtonEdit);
        Button buttonAddDelete = findViewById(R.id.scopeTypeButtonAddDelete);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonAddDelete.setText("Delete");
    }

    public void buttonClick(View view) {
        if (view.getId() == R.id.scopeTypeButtonAddDelete) {

            if (adding) {
                String name = null;
                int price = 0;
                int cleaningTime = 0;
                try {
                    EditText edit1 = findViewById(R.id.scopeTypeEdit1);
                    name = edit1.getText().toString();
                    EditText edit2 = findViewById(R.id.scopeTypeEdit2);
                    price = Integer.parseInt(edit2.getText().toString());
                    EditText edit4 = findViewById(R.id.scopeTypeEdit4);
                    cleaningTime = Integer.parseInt(edit4.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name == null || name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Invalid Name Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (price <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Price Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(cleaningTime <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Cleaning Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                //get checked procedures from checkboxes
                LinearLayout clientCheckbox3 = findViewById(R.id.scopeTypeCheckbox3);
                ArrayList<String> procedureList = new ArrayList<String>();
                for (int i=0; i < clientCheckbox3.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox)clientCheckbox3.getChildAt(i);
                    if (checkBox.isChecked()) {
                        String procedureName = checkBox.getText().toString();
                        procedureList.add(procedureName);
                    }
                }
                if (procedureList == null || procedureList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "A scope type needs a valid procedure", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent returnIntent = new Intent();
                String[] procedureListArray = new String[procedureList.size()];
                returnIntent.putExtra("procedures", procedureList.toArray(procedureListArray));
                returnIntent.putExtra("name", name);
                returnIntent.putExtra("price", price);
                returnIntent.putExtra("cleaningTime", cleaningTime);
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();


                //deleting
            } else {
                scope_type.setPrice(-1);
                scope_type.setCleaningTime(-1);
                scope_type.setProcedureList(null);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("scopeType", scope_type);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

        } else if (view.getId() == R.id.scopeTypeButtonEdit) {
            String name = null;
            int price = 0;
            int cleaningTime = 0;
            try {
                EditText edit1 = findViewById(R.id.scopeTypeEdit1);
                name = edit1.getText().toString();
                EditText edit2 = findViewById(R.id.scopeTypeEdit2);
                price = Integer.parseInt(edit2.getText().toString());
                EditText edit4 = findViewById(R.id.scopeTypeEdit4);
                cleaningTime = Integer.parseInt(edit4.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (name == null || name.equals("")) {
                Toast.makeText(getApplicationContext(), "Invalid Name Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (price <= 0) {
                Toast.makeText(getApplicationContext(), "Invalid Price Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if(cleaningTime <= 0) {
                Toast.makeText(getApplicationContext(), "Invalid Cleaning Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            //get checked procedures from checkboxes
            LinearLayout clientCheckbox3 = findViewById(R.id.scopeTypeCheckbox3);
            ArrayList<String> procedureList = new ArrayList<String>();
            for (int i=0; i < clientCheckbox3.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox)clientCheckbox3.getChildAt(i);
                if (checkBox.isChecked()) {
                    String procedureName = checkBox.getText().toString();
                    procedureList.add(procedureName);
                }
            }
            if (procedureList == null || procedureList.size() <= 0) {
                Toast.makeText(getApplicationContext(), "A scope type needs a valid procedure", Toast.LENGTH_LONG).show();
                return;
            }
            String oldName = scope_type.getName();
            scope_type.setName(name);
            scope_type.setPrice(price);
            scope_type.setCleaningTime(cleaningTime);
            Intent returnIntent = new Intent();
            String[] procedureListArray = new String[procedureList.size()];
            returnIntent.putExtra("oldName", oldName);
            returnIntent.putExtra("procedures", procedureList.toArray(procedureListArray));
            returnIntent.putExtra("scopeType", scope_type);
            setResult(RESULT_OK, returnIntent);
            finish();

        } else if (view.getId() == R.id.scopeTypeButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
