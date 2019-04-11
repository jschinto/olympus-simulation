package com.olympus.simulation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
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

import java.util.ArrayList;
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
        //TODO: TEST CODE REMOVE PLZ
        Procedure proc = new Procedure("Bark", 3, 5);
        Scope_Type type = new Scope_Type("TESTSCOPE", 5);
        type.addProcedure(proc);
        Scope scope = new Scope(type, 5);

        simulation_manager.addProcedure(proc);
        simulation_manager.addScopeType(type);
        simulation_manager.addScope(scope);
        //TEST CODE REMOVE PLZ
        currentClicked = null;
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // handle button activities
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addProcedureType) {
            Intent procedureIntent = new Intent(getApplicationContext(), ProcedureActivity.class);
            Procedure procedure = new Procedure("", 0, 0);
            procedureIntent.putExtra("procedure", procedure);
            startActivityForResult(procedureIntent, procedure_Request);
        }
        else if (id == R.id.addClient) {
            int latest = simulation_manager.getLatestClientTime();

            Intent clientIntent = new Intent(getApplicationContext(), ClientActivity.class);
            Client client = new Client(null, latest);
            clientIntent.putExtra("client", client);
            clientIntent.putExtra("procedures", simulation_manager.getProcedureNames());
            startActivityForResult(clientIntent, client_Request);
        }
        else if (id == R.id.addProcedureRoom) {

            Intent procedureRoomIntent = new Intent(getApplicationContext(), ProcedureRoomActivity.class);
            ProcedureRoom procedureRoom = new ProcedureRoom(0,0);
            procedureRoomIntent.putExtra("procedureRoom", procedureRoom);
            startActivityForResult(procedureRoomIntent, procedureRoom_Request);
        }
        else if(id == R.id.startSimulation) {
            ActionMenuItemView playImage = findViewById(R.id.playButton);
            if(item.getTitle().equals("Confirm Start")) {
                startSimulation(item);
                item.setTitle("Pause");
                playImage.setIcon(getResources().getDrawable(R.drawable.pause_button));
            }
            else if(item.getTitle().equals("Pause")){
                isPaused = true;
                item.setTitle("Resume");
                playImage.setIcon(getResources().getDrawable(R.drawable.play_button));
            }
            else {
                isPaused = false;
                item.setTitle("Pause");
                playImage.setIcon(getResources().getDrawable(R.drawable.pause_button));
            }
        }
        else if (id == R.id.viewProcedureTypes) {

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

                //deleting a procedureRoom
                if (procedureRoom.getTravelTime() <= 0 || procedureRoom.getCooldownTime() <= 0) {
                    simulation_manager.deleteProcedureRoom(index);

                    LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                    View room = linearLayoutRooms.getChildAt(simulation_manager.getProcedureRoomNum());
                    linearLayoutRooms.removeView(room);

                    //editing a procedureRoom
                } else {
                    simulation_manager.editProcedureRoom(procedureRoom, index);
                }
                //nothing to be done, represents just viewing or canceling an add to procedureRoom
            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }

        //returned from ClientActivity
        if (requestCode == client_Request) {
            //added a client
            if (resultCode == RESULT_FIRST_USER) {
                int arrivalTime = data.getIntExtra("arrivalTime",simulation_manager.getLatestClientTime());
                Client client = new Client(new ArrayList<Procedure>(), arrivalTime);
                String[] procedures = data.getStringArrayExtra("procedures");
                for (int i=0; i < procedures.length; i++) {
                    client.addProcedure(simulation_manager.getProcedureByName(procedures[i]));
                }

                int amount = data.getIntExtra("amount",1);
                for (int i=0; i < amount; i++) {
                    simulation_manager.addClient(new Client(client));

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

                //edited or deleted a client
            } else if (resultCode == RESULT_OK) {
                Client client = (Client)data.getSerializableExtra("client");
                int index = currentClicked.index;

                //deleting a client
                if (client.getProcedureList() == null || client.getArrivalTime() < 0) {
                    simulation_manager.deleteClient(index);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
                    View clientView = linearLayoutClients.getChildAt(simulation_manager.getClientNum());
                    linearLayoutClients.removeView(clientView);

                    //editing a client
                } else {
                    String[] procedures = data.getStringArrayExtra("procedures");
                    client.setProcedureList(new ArrayList<Procedure>());
                    for (int i=0; i < procedures.length; i++) {
                        client.addProcedure(simulation_manager.getProcedureByName(procedures[i]));
                    }
                    simulation_manager.editClient(client, index);
                }
                //nothing to be done, represents just viewing or canceling an add to a client
            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }


        //returned from ProcedureActivity
        if (requestCode == procedure_Request) {
            //added a procedure
            if (resultCode == RESULT_FIRST_USER) {
                Procedure procedure = (Procedure) data.getSerializableExtra("procedure");
                simulation_manager.addProcedure(procedure);

                //edited or deleted a procedure
            } else if (resultCode == RESULT_OK) {
                Procedure procedure = (Procedure) data.getSerializableExtra("procedure");

                //deleting a procedure
                if (procedure.getMinTime() <= 0 || procedure.getMaxTime() <= 0 || procedure.getMinTime() > procedure.getMaxTime()) {
                    simulation_manager.deleteProcedure(procedure.getName());

                    //editing a procedure
                } else {
                    String oldName = data.getStringExtra("oldName");
                    simulation_manager.deleteProcedure(oldName);
                    simulation_manager.addProcedure(procedure);
                }
                //nothing to be done, represents just viewing or canceling an add to a procedure
            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }


    }

    public void onClick (View view) {
        Tag tag = (Tag)view.getTag();
        currentClicked = tag;
        int index = tag.index;
        String type = tag.type;

        if (type.equals("Procedure Room")) {
            ProcedureRoom procedureRoom = simulation_manager.getProcedureRoom(index);

            Intent procedureRoomIntent = new Intent(getApplicationContext(), ProcedureRoomActivity.class);
            procedureRoomIntent.putExtra("procedureRoom", procedureRoom);
            startActivityForResult(procedureRoomIntent, procedureRoom_Request);
        }

        else if(type.equals("Client")){
            Client client = simulation_manager.getClient(index);

            Intent clientIntent = new Intent(getApplicationContext(), ClientActivity.class);
            clientIntent.putExtra("client", client);
            clientIntent.putExtra("procedures", simulation_manager.getProcedureNames());
            startActivityForResult(clientIntent, client_Request);
        }
    }

    static boolean isRunning = false;
    static boolean isPaused = false;


    public void startSimulation(final MenuItem item) {
        simulation_manager.setCurrTime(0);
        Timer time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("RestrictedApi")
            @Override
            public void run() {
                if(!isRunning && !isPaused) {
                    isRunning = true;
                    simulation_manager.runTick();
                    simulation_manager.incrementCurrTime();

                    if (simulation_manager.getCurrTime() >= simulation_manager.getEndTime()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                item.setTitle("Confirm Start");
                                ActionMenuItemView playImage = findViewById(R.id.playButton);
                                playImage.setIcon(getResources().getDrawable(R.drawable.play_button));
                            }
                        });
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
