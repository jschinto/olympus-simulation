package com.olympus.simulation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ElementActivity extends AppCompatActivity {

    Element element;
    String mode; // "add" or "view" or "actor"
    int[] ids;

    private final int labelLength = 75;
    private final int textLength = 100;

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
            buttonAdd.setVisibility(View.VISIBLE);
            Button buttonEdit = findViewById(R.id.elementButtonEdit);
            buttonEdit.setVisibility(View.INVISIBLE);
        } else if (mode.equals("view")) {
            Button buttonAdd = findViewById(R.id.elementButtonAddDelete);
            buttonAdd.setText(R.string.buttonDelete);
            buttonAdd.setVisibility(View.VISIBLE);
            Button buttonEdit = findViewById(R.id.elementButtonEdit);
            buttonEdit.setVisibility(View.VISIBLE);
        } else if (mode.equals("actor")) {
            Button buttonAdd = findViewById(R.id.elementButtonAddDelete);
            buttonAdd.setVisibility(View.INVISIBLE);
            Button buttonEdit = findViewById(R.id.elementButtonEdit);
            buttonEdit.setVisibility(View.VISIBLE);
        }

        //add all the text and edit boxes to the layout based on element type
        if (element.equals(Element.ELEMENT_DOCTOR)) {
            setText(R.id.elementTextTitle, "Doctors");
            ids = new int[2];
            String[] procedures = fromIntent.getStringArrayExtra("procedures");
            ids[0] = addField("Procedures", "checkbox", procedures);
            if (mode.equals("view")) {
                Doctor doctor = (Doctor)element;
                String[] checked = doctor.getProceduresNames();
                setChecked(ids[0], checked);
            }
        } else if (element.equals(Element.ELEMENT_NURSE)) {
            setText(R.id.elementTextTitle, "Nurses");
            ids = new int[2];
            ids[0] = addField("Number of Nurses", "number");
            ids[1] = addField("Post Procedure Time", "number");
            int num = fromIntent.getIntExtra("number", -1);
            int cooldown = fromIntent.getIntExtra("cooldown", -1);
            setText(ids[0], num);
            setText(ids[1], cooldown);
        } else if (element.equals(Element.ELEMENT_TECHNICIAN)) {
            setText(R.id.elementTextTitle, "Technicians");
            ids = new int[1];
            ids[0] = addField("Number of Technicians", "number");
            int num = fromIntent.getIntExtra("number", -1);
            setText(ids[0], num);
        }else if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {
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
        } else if (element.equals(Element.ELEMENT_SINK)) {
            setText(R.id.elementTextTitle, "Manual Cleaning Station");
            ids = new int[1];
            String[] leakTesterTypes = fromIntent.getStringArrayExtra("leakTesterTypes");
            ids[0] = addField("Leak Tester Type", "spinner", leakTesterTypes);
            if (mode.equals("view")) {
                ManualCleaningStation manualCleaningStation = (ManualCleaningStation) element;
                setSpinner(ids[0], manualCleaningStation.getCurrentLeakTester().getName());
            }
        } else if(element.equals(Element.ELEMENT_LEAKTESTERTYPE)){
            setText(R.id.elementTextTitle, "Leak Tester Type");
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
        } else if (element.equals(Element.ELEMENT_REPROCESSORTYPE)) {
            setText(R.id.elementTextTitle, "Reprocessor Type");
            ids = new int[5];
            ids[0] = addField("Name", "text");
            ids[1] = addField("Number of Scopes", "number");
            ids[2] = addField("Cycle Time", "number");
            ids[3] = addField("Wait Time", "number");
            ids[4] = addField("Price", "number");
            if (mode.equals("view")) {
                Reprocessor_Type reprocessor_type = (Reprocessor_Type) element;
                setText(ids[0], reprocessor_type.getName());
                setText(ids[1], reprocessor_type.getNumScopes());
                setText(ids[2], reprocessor_type.getCycleTime());
                setText(ids[3], reprocessor_type.getWaitTime());
                setText(ids[4], reprocessor_type.getPrice());
            }
        } else if (element.equals(Element.ELEMENT_REPROCESSOR)) {
            setText(R.id.elementTextTitle, "Reprocessor");
            ids = new int[1];
            String[] reprocessorTypes = fromIntent.getStringArrayExtra("reprocessorTypes");
            ids[0] = addField("Reprocessor Type", "spinner", reprocessorTypes );
            if (mode.equals("view")) {
                Reprocessor reprocessor = (Reprocessor) element;
                setSpinner(ids[0], reprocessor.getType().getName());
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
                } else if (element.equals(Element.ELEMENT_DOCTOR)) {
                    String[] checked = getChecked(ids[0]);
                    if (checked == null) {
                        makeToast("INVALID VALUES ENTERED");
                        return;
                    }
                    Doctor doctor = new Doctor(null);
                    leaveActivity(RESULT_FIRST_USER, doctor, checked);
                }
                else if(element.equals(Element.ELEMENT_LEAKTESTERTYPE)){
                    String name = getTextString(ids[0]);
                    int timeToComplete = getTextInt(ids[1]);
                    int requiredAttentionTime = getTextInt(ids[2]);
                    int price = getTextInt(ids[3]);
                    LeakTester_Type leakTesterType = new LeakTester_Type(name, timeToComplete, requiredAttentionTime, price);
                    if (!leakTesterType.validate()) {
                        makeToast("INVALID VALUES ENTERED");
                        return;
                    }
                    leaveActivity(RESULT_FIRST_USER, leakTesterType);
                } else if (element.equals(Element.ELEMENT_SINK)) {
                    String[] list = new String[1];
                    String leakTesterType = getSpinner(ids[0]);
                    list[0] = leakTesterType;
                    ManualCleaningStation manualCleaningStation = new ManualCleaningStation(null);
                    leaveActivity(RESULT_FIRST_USER, manualCleaningStation, list);

                } else if (element.equals(Element.ELEMENT_REPROCESSORTYPE)) {
                    String name = getTextString(ids[0]);
                    int numScopes = getTextInt(ids[1]);
                    int cycleTime = getTextInt(ids[2]);
                    int waitTime = getTextInt(ids[3]);
                    int price = getTextInt(ids[4]);
                    Reprocessor_Type reprocessor_type = new Reprocessor_Type(name, numScopes, cycleTime, waitTime, price);
                    if (!reprocessor_type.validate()) {
                        makeToast("INVALID VALUES ENTERED");
                        return;
                    }
                    leaveActivity(RESULT_FIRST_USER, reprocessor_type);
                } else if (element.equals(Element.ELEMENT_REPROCESSOR)) {
                    String[] list = new String[1];
                    String reprocessorType = getSpinner(ids[0]);
                    list[0] = reprocessorType;
                    Reprocessor reprocessor = new Reprocessor(null);
                    leaveActivity(RESULT_FIRST_USER, reprocessor, list);
                }
             //clicked the delete button
            } else if (mode.equals("view")) {
                if(element.equals(Element.ELEMENT_PROCEDUREROOM)) {
                    leaveActivity(RESULT_OK, new ProcedureRoom(-1, -1));
                }
                else if(element.equals(Element.ELEMENT_LEAKTESTERTYPE)) {
                    leaveActivity(RESULT_OK, new LeakTester_Type("", -1, -1, -1));
                }
                else if (element.equals(Element.ELEMENT_SINK)) {
                    leaveActivity(RESULT_OK, new ManualCleaningStation(null));
                }
                else if (element.equals(Element.ELEMENT_DOCTOR)) {
                    leaveActivity(RESULT_OK, new Doctor(null), null);
                }
                else if (element.equals(Element.ELEMENT_REPROCESSORTYPE)) {
                    leaveActivity(RESULT_OK, new Reprocessor_Type(null, 0,0,0,0));
                }
                else if (element.equals(Element.ELEMENT_REPROCESSOR)) {
                    leaveActivity(RESULT_OK, new Reprocessor(null));
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

            } else if (element.equals(Element.ELEMENT_DOCTOR)) {
                String[] checked = getChecked(ids[0]);
                if (checked == null) {
                    makeToast("INVALID VALUES ENTERED");
                    return;
                }
                Doctor doctor = new Doctor(null);
                leaveActivity(RESULT_OK, doctor, checked);
            } else if (element.equals(Element.ELEMENT_LEAKTESTERTYPE)) {
                String name = getTextString(ids[0]);
                int timeToComplete = getTextInt(ids[1]);
                int requiredAttentionTime = getTextInt(ids[2]);
                int price = getTextInt(ids[3]);
                LeakTester_Type leakTesterType = new LeakTester_Type(name, timeToComplete, requiredAttentionTime, price);
                if (!leakTesterType.validate()) {
                    makeToast("INVALID VALUES ENTERED");
                    return;
                }
                leaveActivity(RESULT_OK, leakTesterType);
            } else if (element.equals(Element.ELEMENT_SINK)) {
                String[] list = new String[1];
                String leakTesterType = getSpinner(ids[0]);
                list[0] = leakTesterType;
                LeakTester_Type leakTester_type = new LeakTester_Type(leakTesterType, 1,1,1);
                ManualCleaningStation manualCleaningStation = new ManualCleaningStation(leakTester_type);
                leaveActivity(RESULT_OK, manualCleaningStation, list);

            } else if (element.equals(Element.ELEMENT_REPROCESSORTYPE)) {
                String name = getTextString(ids[0]);
                int numScopes = getTextInt(ids[1]);
                int cycleTime = getTextInt(ids[2]);
                int waitTime = getTextInt(ids[3]);
                int price = getTextInt(ids[4]);
                Reprocessor_Type reprocessor_type = new Reprocessor_Type(name, numScopes, cycleTime, waitTime, price);
                if (!reprocessor_type.validate()) {
                    makeToast("INVALID VALUES ENTERED");
                    return;
                }
                leaveActivity(RESULT_OK, reprocessor_type);
            } else if (element.equals(Element.ELEMENT_REPROCESSOR)) {
                String[] list = new String[1];
                String reprocessorType = getSpinner(ids[0]);
                list[0] = reprocessorType;
                Reprocessor_Type reprocessor_type = new Reprocessor_Type(reprocessorType, 1,1,1,1);
                Reprocessor reprocessor = new Reprocessor(reprocessor_type);
                leaveActivity(RESULT_OK, reprocessor, list);
            } else if (mode.equals("actor")) {
                int num = getTextInt(ids[0]);
                int cooldown;
                if (element.equals(Element.ELEMENT_NURSE)) {
                    cooldown = getTextInt(ids[1]);
                } else {
                    cooldown = 1;
                }
                if (num < 1 || cooldown < 1) {
                    makeToast("INVALID VALUES ENTERED");
                    return;
                }
                leaveActivity(RESULT_OK, element, num, cooldown);
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
        labelView.setWidth(labelLength);
        newLayout.addView(labelView);

        if (type.equals("text")) {
            EditText fieldView = new EditText(getApplicationContext());
            fieldView.setInputType(InputType.TYPE_CLASS_TEXT);
            fieldView.setText("");
            fieldView.setWidth(textLength);
            int id = View.generateViewId();
            fieldView.setId(id);
            newLayout.addView(fieldView);
            return id;
        }
        else if (type.equals("number")) {
            EditText fieldView = new EditText(getApplicationContext());
            fieldView.setInputType(InputType.TYPE_CLASS_NUMBER);
            fieldView.setText("0");
            fieldView.setWidth(textLength);
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
        labelView.setWidth(labelLength);
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

        } else if (type.equals("spinner")) {
            Spinner spinner = new Spinner(getApplicationContext());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, values);
            spinner.setAdapter(adapter);
            int id = View.generateViewId();
            spinner.setId(id);
            newLayout.addView(spinner);
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

    public void setSpinner(int id, String selected) {
        Spinner spinner = findViewById(id);
        SpinnerAdapter adapter = spinner.getAdapter();
        int length = adapter.getCount();
        for (int i=0; i < length; i++) {
            if (adapter.getItem(i).equals(selected)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    public String getSpinner(int id) {
        Spinner spinner = findViewById(id);
        return spinner.getSelectedItem().toString();
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
        if (valueList.size() < 1) {
            return null;
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
    public void leaveActivity(int code, Element newElement, int number, int cooldown) {
            if (code == RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                finish();
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra("element", newElement);
            returnIntent.putExtra("number", number);
            returnIntent.putExtra("cooldown", cooldown);
            setResult(code, returnIntent);
            finish();
    }
}
