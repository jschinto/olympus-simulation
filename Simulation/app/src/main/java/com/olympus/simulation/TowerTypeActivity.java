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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TowerTypeActivity extends AppCompatActivity {

    // text fields with these numbers correspond to these attributes
    // 1 : travel time
    // 2: cooldown time


    Tower_Type tower_type; //procedure room being represented
    String[] scope_names;
    private boolean adding; //true if adding new element, false if viewing existing element


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tower_type);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        adding = true;
        Intent fromIntent = getIntent();
        tower_type = (Tower_Type) fromIntent.getSerializableExtra("towerType");
        scope_names = fromIntent.getStringArrayExtra("scopeTypes");
        setValues(tower_type.getName(), tower_type.getPrice(), tower_type.getScopeTypes());
    }

    private void setValues(String name, int price, ArrayList<Scope_Type> types) {
        EditText edit1 = findViewById(R.id.towerTypeEdit1);
        EditText edit2 = findViewById(R.id.towerTypeEdit2);
        LinearLayout checkBox = findViewById(R.id.towerTypeCheckbox1);
        for(int i = 0; i < scope_names.length; i++){
            CheckBox check = new CheckBox(getApplicationContext());
            check.setText(scope_names[i]);
            boolean result = false;
            if(types != null) {
                for (int j = 0; j < types.size(); j++) {
                    if (types.get(j).getName().equals(scope_names[i])) {
                        result = true;
                        break;
                    }
                }
            }
            check.setChecked(result);
            checkBox.addView(check);
        }

        if (name == null) {
            edit1.setText("");
            edit2.setText("");
            addSetup();
            return;
        }
        edit1.setText(String.valueOf(name));
        edit2.setText(String.valueOf(price));
        viewSetup();
    }

    private void addSetup() {
        adding = true;
        Button buttonEdit = findViewById(R.id.towerTypeButtonEdit);
        Button buttonAddDelete = findViewById(R.id.towerTypeButtonAddDelete);
        buttonEdit.setVisibility(View.INVISIBLE);
        buttonAddDelete.setText("Add");
    }

    private void viewSetup() {
        adding = false;
        Button buttonEdit = findViewById(R.id.towerTypeButtonEdit);
        Button buttonAddDelete = findViewById(R.id.towerTypeButtonAddDelete);
        buttonEdit.setVisibility(View.VISIBLE);
        buttonAddDelete.setText("Delete");
    }

    public void buttonClick(View view) {
        if (view.getId() == R.id.towerTypeButtonAddDelete) {
            if (adding) {
                String name = "";
                int price = 0;
                try {
                    EditText edit1 = findViewById(R.id.towerTypeEdit1);
                    name = edit1.getText().toString();
                    EditText edit2 = findViewById(R.id.towerTypeEdit2);
                    price = Integer.parseInt(edit2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (price <= 0 || name == "") {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                //get checked procedures from checkboxes
                LinearLayout checkbox = findViewById(R.id.towerTypeCheckbox1);
                ArrayList<String> scopeTypeList = new ArrayList<String>();
                for (int i=0; i < checkbox.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox)checkbox.getChildAt(i);
                    if (checkBox.isChecked()) {
                        String scopeTypeName = checkBox.getText().toString();
                        scopeTypeList.add(scopeTypeName);
                    }
                }
                if (scopeTypeList == null || scopeTypeList.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "A Tower Type needs a valid Scope Type", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent returnIntent = new Intent();
                String[] scopeTypeArray = new String[scopeTypeList.size()];
                returnIntent.putExtra("name", name);
                returnIntent.putExtra("price", price);
                returnIntent.putExtra("scopeTypes", scopeTypeList.toArray(scopeTypeArray));
                setResult(RESULT_FIRST_USER, returnIntent);
                finish();

                //deleting
            } else {
                tower_type.setName(null);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("towerType", tower_type);
                setResult(RESULT_OK, returnIntent);
                finish();
            }

        } else if (view.getId() == R.id.towerTypeButtonEdit) {
            String name = "";
            int price = 0;
            try {
                EditText edit1 = findViewById(R.id.towerTypeEdit1);
                name = edit1.getText().toString();
                EditText edit2 = findViewById(R.id.towerTypeEdit2);
                price = Integer.parseInt(edit2.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (price <= 0 || name == "") {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            //get checked procedures from checkboxes
            LinearLayout checkbox = findViewById(R.id.towerTypeCheckbox1);
            ArrayList<String> scopeTypeList = new ArrayList<String>();
            for (int i=0; i < checkbox.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox)checkbox.getChildAt(i);
                if (checkBox.isChecked()) {
                    String scopeTypeName = checkBox.getText().toString();
                    scopeTypeList.add(scopeTypeName);
                }
            }
            if (scopeTypeList == null || scopeTypeList.size() <= 0) {
                Toast.makeText(getApplicationContext(), "A Tower Type needs a valid Scope Type", Toast.LENGTH_LONG).show();
                return;
            }

            String oldName = tower_type.getName();
            Intent returnIntent = new Intent();
            String[] scopeTypeArray = new String[scopeTypeList.size()];
            returnIntent.putExtra("oldName", oldName);
            returnIntent.putExtra("name", name);
            returnIntent.putExtra("price", price);
            returnIntent.putExtra("scopeTypes", scopeTypeList.toArray(scopeTypeArray));
            setResult(RESULT_FIRST_USER, returnIntent);
            finish();

        } else if (view.getId() == R.id.towerTypeButtonExit) {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
