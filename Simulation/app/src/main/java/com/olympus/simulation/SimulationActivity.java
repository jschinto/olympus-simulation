package com.olympus.simulation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SimulationActivity extends AppCompatActivity implements View.OnClickListener {

    Simulation_Manager simulation_manager;
    Tag currentClicked;

    public static final int procedureRoom_Request = 1;
    public static final int client_Request = 2;
    public static final int procedure_Request = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set orientation to lock on portrait
        simulation_manager = new Simulation_Manager(0,100,1);
        currentClicked = null;
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Button buttonAddDelete = findViewById(R.id.buttonAddDelete);
        buttonAddDelete.setText("Add");
        if (id == R.id.addProcedureType) {

            procedureInfoBox("","","");
            findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
        }
        if (id == R.id.addClient) {
            int latest = simulation_manager.getLatestClientTime();
            String latestString = Time.convertToString(latest);

            clientInfoBox(latestString,"1",-1);
            findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
        }
        if (id == R.id.addProcedureRoom) {

            Intent procedureRoomIntent = new Intent(getApplicationContext(), ProcedureRoomActivity.class);
            ProcedureRoom procedureRoom = new ProcedureRoom(0,0);
            procedureRoomIntent.putExtra("procedureRoom", procedureRoom);
            startActivityForResult(procedureRoomIntent, procedureRoom_Request);
        }
        if(id == R.id.startSimulation) {
            startSimulation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //returned from ProcedureRoomActivity
        if (requestCode == procedureRoom_Request) {
            //added a procedureRoom
            if (resultCode == RESULT_FIRST_USER) {

                ProcedureRoom procedureRoom = (ProcedureRoom)data.getSerializableExtra("procedureRoom");
                simulation_manager.addProcedureRoom(procedureRoom);

                LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                ImageView roomImage = new ImageView(getApplicationContext());
                roomImage.setImageResource(R.drawable.procedure_room);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                roomImage.setLayoutParams(layoutParams);
                roomImage.setPadding(10, 0, 0, 0);
                roomImage.setOnClickListener(this);

                int index = simulation_manager.getProcedureRoomNum() - 1;
                Tag tag = new Tag(index, "Procedure Room");

                roomImage.setTag(tag);

                linearLayoutRooms.addView(roomImage);

                //edited or deleted a procedureRoom
            } else if (resultCode == RESULT_OK) {
                ProcedureRoom procedureRoom = (ProcedureRoom)data.getSerializableExtra("procedureRoom");
                int index = currentClicked.index;

                //deleting a procedureRoom TODO: error on delete, add, delete in same position
                if (procedureRoom.getTravelTime() <= 0 || procedureRoom.getCooldownTime() <= 0) {
                    simulation_manager.deleteProcedureRoom(index);

                    LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                    View room = linearLayoutRooms.getChildAt(simulation_manager.getProcedureRoomNum());
                    room.setVisibility(View.GONE);

                    //editing a procedureRoom
                } else {
                    simulation_manager.editProcedure(procedureRoom, index);
                }
                //nothing to be done, represents just viewing or canceling an add to procedureRoom
            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }
    }














    public void onAddDeleteClick(View view){
        Button buttonAddDelete = findViewById(R.id.buttonAddDelete);
        String buttonName = buttonAddDelete.getText().toString();

        if(buttonName.equals("Add"))
        {
            TextView headerView =  findViewById(R.id.textInfo_Name);
            String header = headerView.getText().toString();
            //press add button for procedure room
            if (header.equals("Procedure Room")) {
                //////////////////////////
                int travelTime = 0;
                int cooldownTime = 0;
                try {
                    EditText editData1 = findViewById(R.id.editData1);
                    travelTime = Integer.parseInt(editData1.getText().toString());
                    EditText editData2 = findViewById(R.id.editData2);
                    cooldownTime = Integer.parseInt(editData2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (cooldownTime <= 0 || travelTime <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);
                simulation_manager.addProcedureRoom(procedureRoom);

                LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                ImageView roomImage = new ImageView(getApplicationContext());
                roomImage.setImageResource(R.drawable.procedure_room);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                roomImage.setLayoutParams(layoutParams);
                roomImage.setPadding(10, 0, 0, 0);
                roomImage.setOnClickListener(this);

                int index = simulation_manager.getProcedureRoomNum() - 1;
                Tag tag = new Tag(index, "Procedure Room");

                roomImage.setTag(tag);

                linearLayoutRooms.addView(roomImage);
            }
            else if (header.equals("Client")){

                String arrivalString = "";
                int amount = 0;
                try {
                    /*
                    EditText editData1 = findViewById(R.id.editData1);
                    Get Procedure from EditText
                    */
                    EditText editData1 = findViewById(R.id.editData1);
                    arrivalString = editData1.getText().toString();
                    EditText editData2 = findViewById(R.id.editData2);
                    amount = Integer.parseInt(editData2.getText().toString());
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

                Spinner spinner4 = findViewById(R.id.spinner4);
                Procedure procedure = simulation_manager.getProcedureByName((String)spinner4.getSelectedItem());

                if (procedure == null) {
                    Toast.makeText(getApplicationContext(), "A customer needs a valid procedure", Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0; i < amount; i++) {
                    Client client = new Client(procedure, arrivalTime);
                    simulation_manager.addClient(client);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
                    ImageView clientImage = new ImageView(getApplicationContext());
                    clientImage.setImageResource(R.drawable.client);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(78, 100);
                    clientImage.setLayoutParams(layoutParams);
                    clientImage.setOnClickListener(this);

                    int index = simulation_manager.getClientNum() - 1;
                    Tag tag = new Tag(index, "Client");

                    clientImage.setTag(tag);

                    linearLayoutClients.addView(clientImage);
                }
            }
            else if (header.equals("Procedure Type")) {
                String name = null;
                int minTime = 0;
                int maxTime = 0;
                try {
                    EditText editData1 = findViewById(R.id.editData1);
                    name = editData1.getText().toString();
                    EditText editData2 = findViewById(R.id.editData2);
                    minTime = Integer.parseInt(editData2.getText().toString());
                    EditText editData3 = findViewById(R.id.editData3);
                    maxTime = Integer.parseInt(editData3.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (name == null) {
                    Toast.makeText(getApplicationContext(), "Invalid Name!", Toast.LENGTH_LONG).show();
                }
                if (minTime <= 0 || maxTime <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (minTime > maxTime) {
                    Toast.makeText(getApplicationContext(), "min time can't be greater than max time!", Toast.LENGTH_LONG).show();
                }

                Procedure procedure = new Procedure(name, minTime, maxTime);
                simulation_manager.addProcedure(procedure);

            }
        }
        else if(buttonName.equals("Delete")){
            if(currentClicked == null){
                return;
            }
            else
            {
                int index = currentClicked.index;
                String type = currentClicked.type;

                if(type.equals("Procedure Room")){
                    simulation_manager.deleteProcedureRoom(index);

                    LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);

                    View room = linearLayoutRooms.getChildAt(simulation_manager.getProcedureRoomNum());

                    room.setVisibility(View.GONE);
                }
                else if(type.equals("Client")){
                    simulation_manager.deleteClient(index);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);

                    View client = linearLayoutClients.getChildAt(simulation_manager.getClientNum());

                    client.setVisibility(View.GONE);
                }

                currentClicked = null;
            }
        }
    }

    public void onUpdate (View view) {
        int index = currentClicked.index;
        String type = currentClicked.type;

        if(type.equals("Client")) {
            String arrivalString = "";
            try {
                EditText editData1 = findViewById(R.id.editData1);
                arrivalString = editData1.getText().toString();
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

            Spinner spinner4 = findViewById(R.id.spinner4);
            Procedure procedure = simulation_manager.getProcedureByName((String)spinner4.getSelectedItem());

            if (procedure == null) {
                Toast.makeText(getApplicationContext(), "A customer needs a valid procedure", Toast.LENGTH_LONG).show();
                return;
            }

            Client update = new Client(procedure, arrivalTime);

            simulation_manager.editClient(update, index);
        }
        else if(type.equals("Procedure Room"))
        {
            int travelTime = 0;
            int cooldownTime = 0;
            try {
                EditText editData1 = findViewById(R.id.editData1);
                travelTime = Integer.parseInt(editData1.getText().toString());
                EditText editData2 = findViewById(R.id.editData2);
                cooldownTime = Integer.parseInt(editData2.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }
            if (cooldownTime <= 0 || travelTime <= 0) {
                Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                return;
            }

            ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);

            simulation_manager.editProcedure(procedureRoom, index);
        }
    }

    public void onClick (View view) {

        findViewById(R.id.buttonEdit).setVisibility(View.VISIBLE);

        Button buttonAddDelete = findViewById(R.id.buttonAddDelete);
        buttonAddDelete.setText("Delete");

        Tag tag = (Tag)view.getTag();
        currentClicked = tag;
        int index = tag.index;
        String type = tag.type;

        if (type.equals("Procedure Room")) {
            ProcedureRoom procedureRoom = simulation_manager.getProcedureRoom(index);

            Intent procedureRoomIntent = new Intent(getApplicationContext(), ProcedureRoomActivity.class);
            procedureRoomIntent.putExtra("procedureRoom", procedureRoom);
            startActivityForResult(procedureRoomIntent, procedureRoom_Request);
           // procedureRoomInfoBox(""+room.getTravelTime(), ""+room.getCooldownTime());
        }

        else if(type.equals("Client")){
            Client client = simulation_manager.getClient(index);
            //TODO: indexing procedure types in array???????/

            int arrivalTime = client.getArrivalTime();
            String arrivalString = Time.convertToString(arrivalTime);


            clientInfoBox(arrivalString, "", -1);
        }
    }

    public void procedureRoomInfoBox(String travelTime, String cooldownTime) {
        setInfoBox("Travel Time:", travelTime, "Cooldown Time:", cooldownTime, "", "", "", null, -1, "", "", "", "");
        TextView headerView =  findViewById(R.id.textInfo_Name);
        headerView.setText("Procedure Room");
    }
    public void clientInfoBox(String arrivalTime, String amountToAdd, int procedureType) {
        String[] procedureNames = simulation_manager.getProcedureNames();
        if (amountToAdd.isEmpty()) {
            setInfoBox("Arrival Time:", arrivalTime, "", "", "", "", "Procedure Type:", procedureNames, procedureType, "", "", "", "");
        }
        else {
            setInfoBox("Arrival Time:", arrivalTime, "Amount To Add:", ""+amountToAdd, "", "", "Procedure Type:", procedureNames, procedureType, "", "", "", "");
        }
        TextView headerView =  findViewById(R.id.textInfo_Name);
        headerView.setText("Client");
    }
    public void procedureInfoBox(String procedureName, String minCompletionTime, String maxCompletionTime) {
        setInfoBox("Procedure Name", procedureName, "Min Completion Time", minCompletionTime, "Max Completion Time", maxCompletionTime, "", null, -1, "", "","","");
        TextView headerView =  findViewById(R.id.textInfo_Name);
        headerView.setText("Procedure Type");
    }

    public void clearInfoBox() {
        TextView headerView =  findViewById(R.id.textInfo_Name);
        headerView.setText("");

        TextView textData1 = findViewById(R.id.textData1);
        EditText editData1 = findViewById(R.id.editData1);
        TextView textData2 = findViewById(R.id.textData2);
        EditText editData2 = findViewById(R.id.editData2);
        TextView textData3 = findViewById(R.id.textData3);
        EditText editData3 = findViewById(R.id.editData3);
        TextView textData4 = findViewById(R.id.textData4);
        Spinner spinner4 = findViewById(R.id.spinner4);
        TextView textData5 = findViewById(R.id.textData5);
        EditText editData5 = findViewById(R.id.editData5);
        TextView textData6 = findViewById(R.id.textData6);
        EditText editData6 = findViewById(R.id.editData6);
        textData1.setVisibility(View.INVISIBLE);
        editData1.setVisibility(View.INVISIBLE);
        textData2.setVisibility(View.INVISIBLE);
        editData2.setVisibility(View.INVISIBLE);
        textData3.setVisibility(View.INVISIBLE);
        editData3.setVisibility(View.INVISIBLE);
        textData4.setVisibility(View.INVISIBLE);
        spinner4.setVisibility(View.INVISIBLE);
        textData5.setVisibility(View.INVISIBLE);
        editData5.setVisibility(View.INVISIBLE);
        textData6.setVisibility(View.INVISIBLE);
        editData6.setVisibility(View.INVISIBLE);
    }
    public void setInfoBox(String attribute1, String value1, String attribute2, String value2, String attribute3, String value3,
                           String attribute4, String[] value4, int index4, String attribute5, String value5, String attribute6, String value6) {
        clearInfoBox();

        if (!attribute1.isEmpty()) {
            TextView textData1 = findViewById(R.id.textData1);
            textData1.setVisibility(View.VISIBLE);
            textData1.setText(attribute1);

            EditText editData1 = findViewById(R.id.editData1);
            editData1.setVisibility(View.VISIBLE);
            editData1.setText(value1);
        }

        if (!attribute2.isEmpty()) {
            TextView textData2 = findViewById(R.id.textData2);
            textData2.setVisibility(View.VISIBLE);
            textData2.setText(attribute2);

            EditText editData2 = findViewById(R.id.editData2);
            editData2.setVisibility(View.VISIBLE);
            editData2.setText(value2);
        }

        if (!attribute3.isEmpty()) {
            TextView textData3 = findViewById(R.id.textData3);
            textData3.setVisibility(View.VISIBLE);
            textData3.setText(attribute3);

            EditText editData3 = findViewById(R.id.editData3);
            editData3.setVisibility(View.VISIBLE);
            editData3.setText(value3);
        }

        if (!attribute4.isEmpty()) {
            TextView textData4 = findViewById(R.id.textData4);
            textData4.setVisibility(View.VISIBLE);
            textData4.setText(attribute4);

            Spinner spinner4 = findViewById(R.id.spinner4);
            spinner4.setVisibility(View.VISIBLE);
            if (value4 != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, value4);
                spinner4.setAdapter(adapter);
                if (index4!=-1) {
                    spinner4.setSelection(index4);
                }
            }
            else {
                spinner4.setVisibility(View.INVISIBLE);
            }
        }

        if (!attribute5.isEmpty()) {
            TextView textData5 = findViewById(R.id.textData5);
            textData5.setVisibility(View.VISIBLE);
            textData5.setText(attribute5);

            EditText editData5 = findViewById(R.id.editData5);
            editData5.setVisibility(View.VISIBLE);
            editData5.setText(value5);
        }

        if (!attribute6.isEmpty()) {
            TextView textData6 = findViewById(R.id.textData6);
            textData6.setVisibility(View.VISIBLE);
            textData6.setText(attribute6);

            EditText editData6 = findViewById(R.id.editData6);
            editData6.setVisibility(View.VISIBLE);
            editData6.setText(value6);
        }

    }

    static boolean isRunning = false;
    static boolean isPaused = false;


    public void startSimulation() {
        simulation_manager.setCurrTime(0);
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!isRunning && !isPaused) {
                    isRunning = true;
                    simulation_manager.runTick();
                    simulation_manager.incrementCurrTime();

                    if (simulation_manager.getCurrTime() >= simulation_manager.getEndTime()) {
                        this.cancel();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                            isRunning = false;
                        }
                    });
                    System.err.println("Loop " + (simulation_manager.getCurrTime() - 1));
                }
            }
        }, 0, 1000);
    }

    public void updateUI() {
        int numClients = simulation_manager.getClientNum();
        int numProcedureRoom = simulation_manager.getProcedureRoomNum();

        LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
        for(int i = 0; i < numClients; i++)
        {
            Client client = simulation_manager.getClient(i);
            View clientImg = linearLayoutClients.getChildAt(i);

            if(client.getState() == State.STATE_WAIT) {
                clientImg.setBackgroundColor(Color.BLUE);
            }
            else if(client.getState() == State.STATE_TRAVEL) {
                clientImg.setBackgroundColor(Color.CYAN);
            }
            else if(client.getState() == State.STATE_OPERATION) {
                clientImg.setBackgroundColor(Color.YELLOW);
            }
            else if(client.getState() == State.STATE_DONE)
            {
                clientImg.setBackgroundColor(Color.GREEN);
            }
        }
        LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
        for(int i = 0; i < numProcedureRoom; i++)
        {
            ProcedureRoom room = simulation_manager.getProcedureRoom(i);
            View roomImg = linearLayoutRooms.getChildAt(i);

            if(room.isAvailable()) {
                roomImg.setBackgroundColor(Color.BLUE);
            }
            else if(room.isOccupied()) {
                roomImg.setBackgroundColor(Color.CYAN);
            }
            else {
                roomImg.setBackgroundColor(Color.YELLOW);
            }
        }
    }
}
