package com.olympus.simulation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

        } else if (element.equals(Element.ELEMENT_NURSE)) {
            setText(R.id.elementTextTitle, "Nurse");
            ids = new int[1];
            ids[0] = addField("Post Procedure Time", "number");
            if (mode.equals("view")) {
                Nurse nurse = (Nurse)element;
                setText(ids[0], nurse.getPostProcedureTime());
            }
        } else if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {
            setText(R.id.elementTextTitle, "Procedure Room");
            ids = new int[3];
            ids[0] = addField("Travel Time", "number");
            ids[1] = addField("Cooldown Time", "number");

            String[] towerTypes = fromIntent.getStringArrayExtra("towerTypes");
            ids[2] = addField("Tower Types", "checkbox", towerTypes);

            if (mode.equals("view")) {
                ProcedureRoom procedureRoom = (ProcedureRoom) element;
                setText(ids[0], procedureRoom.getTravelTime());
                setText(ids[1], procedureRoom.getCooldownTime());
                String[] checked = procedureRoom.getTowerTypeNames();
                setChecked(ids[2], checked);
            }
        } else if (element.equals(Element.ELEMENT_SCOPE)) {

        } else if (element.equals(Element.ELEMENT_SCOPETYPE)) {

        }
        else if(element.equals(Element.ELEMENT_LEAKTESTERTYPE)){
            setText(R.id.elementTextTitle, Element.ELEMENT_LEAKTESTERTYPE);
            ids = new int[4];
            ids[0] = addField("Leak Tester Type Name", "text");
            ids[1] = addField("Time to Complete", "number");
            ids[2] = addField("Required Attention Time", "number");
            ids[3] = addField("Price", "number");
            if(mode.equals("view")){
                LeakTester_Type leakTesterType = (LeakTester_Type) element;
                setText(ids[0], leakTesterType.getName());
                setText(ids[1], leakTesterType.getTimeToComplete());
                setText(ids[2], leakTesterType.getRequiredAttentionTime());
                setText(ids[3], leakTesterType.getPrice());
            }
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
                    String [] checked = getChecked(ids[2]);
                    ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);
                    if (procedureRoom.validate()) {
                        leaveActivity(RESULT_FIRST_USER, procedureRoom, checked);
                    } else {
                        makeToast("INVALID VALUES ENTERED");
                        return;
                    }
                } else if (element.equals(Element.ELEMENT_NURSE)) {
                    int postProcedureTime = getTextInt(ids[0]);
                    Nurse nurse = new Nurse(postProcedureTime);
                    if (nurse.validate()) {
                        leaveActivity(RESULT_FIRST_USER, nurse);
                    } else {
                        makeToast("INVALID VALUES ENTERED");
                        return;
                    }

                }
                else if(element.equals(Element.ELEMENT_LEAKTESTERTYPE)){
                    String name = getTextString(ids[0]);
                    int timeToComplete = getTextInt(ids[1]);
                    int requiredAttentionTime = getTextInt(ids[2]);
                    int price = getTextInt(ids[3]);
                    LeakTester_Type leakTesterType = new LeakTester_Type(name, timeToComplete, requiredAttentionTime, price);
                    leaveActivity(RESULT_FIRST_USER, leakTesterType);
                }
             //clicked the delete button
            } else if (mode.equals("view")) {
                if(element.equals(Element.ELEMENT_PROCEDUREROOM)) {
                    leaveActivity(RESULT_OK, new ProcedureRoom(-1, -1));
                }
                else if(element.equals(Element.ELEMENT_LEAKTESTERTYPE)) {
                    leaveActivity(RESULT_OK, new LeakTester_Type("", -1, -1, -1));
                }

                if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {
                    leaveActivity(RESULT_OK, new ProcedureRoom(-1, -1));
                } else if (element.equals(Element.ELEMENT_NURSE)) {
                    leaveActivity(RESULT_OK, new Nurse(-1));

                }
            }

         //clicked the update button
        } else if (view.getId() == R.id.elementButtonEdit) {

            if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {

                int travelTime = getTextInt(ids[0]);
                int cooldownTime = getTextInt(ids[1]);
                String [] checked = getChecked(ids[2]);
                ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);
                if (procedureRoom.validate()) {
                    leaveActivity(RESULT_OK, procedureRoom, checked);
                } else {
                    makeToast("INVALID VALUES ENTERED");
                    return;
                }

            } else if (element.equals(Element.ELEMENT_NURSE)) {
                int postProcedureTime = getTextInt(ids[0]);
                Nurse nurse = new Nurse(postProcedureTime);
                if (nurse.validate()) {
                    leaveActivity(RESULT_OK, nurse);
                } else {
                    makeToast("INVALID VALUES ENTERED");
                    return;
                }

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
        } else {
            return 0;
        }

    }

    public int addField(String label, String type, String[] values) {
        LinearLayout linearLayout = findViewById(R.id.elementLinearLayout);
        LinearLayout newLayout = new LinearLayout(getApplicationContext());
        newLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newLayout.setLayoutParams(params);
        linearLayout.addView(newLayout);

        TextView labelView = new TextView(getApplicationContext());
        labelView.setText(label);
        newLayout.addView(labelView);

        if (type.equals("checkbox")) {
            LinearLayout checkBoxes = new LinearLayout(getApplicationContext());

            LinearLayout.LayoutParams checkboxParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBoxes.setLayoutParams(checkboxParams);
            checkBoxes.setOrientation(LinearLayout.VERTICAL);

            for (int i=0; i < values.length; i++) {
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(values[i]);
                checkBox.setChecked(false);
                checkBoxes.addView(checkBox);
            }

            int id = View.generateViewId();
            checkBoxes.setId(id);
            newLayout.addView(checkBoxes);
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

    public void setChecked(int id, String[] values) {
        LinearLayout checkboxes = findViewById(id);
        for (int i=0; i < checkboxes.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) checkboxes.getChildAt(i);
            String text = checkBox.getText().toString();
            for (int j=0; j < values.length; j++) {
                if (values[j].equals(text)) {
                    checkBox.setChecked(true);
                    break;
                }
            }
        }

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

    public String[] getChecked(int id) {
        LinearLayout checkboxes = findViewById(id);
        ArrayList<String> valueList = new ArrayList<String>();
        for (int i=0; i < checkboxes.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) checkboxes.getChildAt(i);
            if (checkBox.isChecked()) {
                valueList.add(checkBox.getText().toString());
            }
        }
        String[] values = new String[valueList.size()];
        values = valueList.toArray(values);
        return values;
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

    public void leaveActivity(int code, Element newElement, String[] list) {
        if (code == RESULT_CANCELED) {
            setResult(RESULT_CANCELED);
            finish();
        }
        Intent returnIntent = new Intent();
        returnIntent.putExtra("element", newElement);
        returnIntent.putExtra("list", list);
        setResult(code, returnIntent);
        finish();
    }
}
