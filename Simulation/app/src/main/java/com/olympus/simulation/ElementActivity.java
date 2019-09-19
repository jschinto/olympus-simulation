package com.olympus.simulation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ElementActivity extends AppCompatActivity {

    Element element;
    String mode; // "add" or "view"

    int[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);

        //get the element from the calling activity
        Intent fromIntent = getIntent();
        element = (Element) fromIntent.getSerializableExtra("element");

        //get the mode the activity is in (add or view)
        mode = fromIntent.getStringExtra("mode");
        if (mode == null || mode.equals("")) {
            Toast.makeText(getApplicationContext(), "NO MODE SPECIFIED", Toast.LENGTH_LONG).show();
        }
        //configure the buttons based on the mode
        else if (mode.equals("add")) {
            Button buttonAdd = findViewById(R.id.elementButtonAddDelete);
            buttonAdd.setText(R.string.buttonAdd);
            Button buttonEdit = findViewById(R.id.elementButtonEdit);
            buttonEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("view")) {
            Button buttonAdd = findViewById(R.id.elementButtonAddDelete);
            buttonAdd.setText(R.string.buttonDelete);
            Button buttonEdit = findViewById(R.id.elementButtonEdit);
            buttonEdit.setVisibility(View.VISIBLE);
        }

        //add all the text and edit boxes to the layout based on element type
        if (element.equals(Element.ELEMENT_CLIENT)) {

        } else if (element.equals(Element.ELEMENT_PROCEDURE)) {

        } else if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {
            setText(R.id.elementTextTitle, Element.ELEMENT_PROCEDUREROOM);
            ids = new int[2];
            ids[0] = addField("Travel Time", "number");
            ids[1] = addField("Cooldown Time", "number");
            if (mode.equals("view")) {
                ProcedureRoom procedureRoom = (ProcedureRoom) element;
                setText(ids[0], procedureRoom.getTravelTime());
                setText(ids[1], procedureRoom.getCooldownTime());
            }
        } else if (element.equals(Element.ELEMENT_SCOPE)) {

        } else if (element.equals(Element.ELEMENT_SCOPETYPE)) {

        }


    }

    //process button clicks
    public void buttonClick(View view) {

        if (view.getId() == R.id.elementButtonAddDelete) {
            //clicked the add button
            if (mode.equals("add")) {
                if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {
                    int travelTime = getTextInt(ids[0]);
                    int cooldownTime = getTextInt(ids[1]);
                    ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);
                    if (procedureRoom.validate()) {
                        leaveActivity(RESULT_FIRST_USER, procedureRoom);
                    } else {
                        makeToast("INVALID VALUES ENTERED");
                        return;
                    }
                }

             //clicked the delete button
            } else if (mode.equals("view")) {
                leaveActivity(RESULT_OK, new ProcedureRoom(-1,-1));
            }

         //clicked the update button
        } else if (view.getId() == R.id.elementButtonEdit) {
            int travelTime = getTextInt(ids[0]);
            int cooldownTime = getTextInt(ids[1]);
            ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);
            if (procedureRoom.validate()) {
                leaveActivity(RESULT_OK, procedureRoom);
            } else {
                makeToast("INVALID VALUES ENTERED");
                return;
            }

        //clicked the exit button
        } else if (view.getId() == R.id.elementButtonExit) {
            leaveActivity(RESULT_CANCELED, element);
        }
    }

    public int addField(String label, String type) {
        LinearLayout linearLayout = findViewById(R.id.elementLinearLayout);
        LinearLayout newLayout = new LinearLayout(getApplicationContext());
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newLayout.setLayoutParams(params);
        linearLayout.addView(newLayout);

        TextView labelView = new TextView(getApplicationContext());
        labelView.setText(label);
        newLayout.addView(labelView);

        if (type.equals("text")) {
            EditText fieldView = new EditText(getApplicationContext());
            fieldView.setInputType(InputType.TYPE_CLASS_TEXT);
            fieldView.setText("");
            int id = View.generateViewId();
            fieldView.setId(id);
            newLayout.addView(fieldView);
            return id;
        }
        else if (type.equals("number")) {
            EditText fieldView = new EditText(getApplicationContext());
            fieldView.setInputType(InputType.TYPE_CLASS_NUMBER);
            fieldView.setText("0");
            int id = View.generateViewId();
            fieldView.setId(id);
            newLayout.addView(fieldView);
            return id;
        }

        return 0;

    }

    public void setText(int id, String value) {
        TextView textView = findViewById(id);
        textView.setText(value);
    }

    public void setText(int id, int value) {
        setText(id, ""+value);
    }

    public String getTextString(int id) {
        TextView textView = findViewById(id);
        return textView.getText().toString();
    }

    public int getTextInt(int id) {
        TextView textView = findViewById(id);
        String text = textView.getText().toString();
        int x = -1;
        try {
            x = Integer.parseInt(text);
        } catch (Exception e) {
            makeToast("DATA NEEDS TO BE A NUMBER");
            return -1;
        }
        return x;
    }





    public void makeToast(String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
    }

    public void leaveActivity(int code, Element newElement) {
        if (code == RESULT_CANCELED) {
            setResult(RESULT_CANCELED);
            finish();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("element", newElement);
        setResult(code, returnIntent);
        finish();

    }
}
