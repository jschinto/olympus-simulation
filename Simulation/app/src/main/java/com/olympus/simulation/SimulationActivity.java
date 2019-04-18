package com.olympus.simulation;

import   android.annotation.SuppressLint;
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
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationActivity extends AppCompatActivity implements View.OnClickListener {

    Simulation_Manager simulation_manager;
    Hall_Monitor hall_monitor;
    Tag currentClicked;

    public static final int procedureRoom_Request = 1;
    public static final int client_Request = 2;
    public static final int procedure_Request = 3;
    public static final int scopeType_Request = 4;
    public static final int scope_Request = 5;

    public class Hall_Monitor {
        ArrayList<Object> list;
        LinearLayout hallway;

        public Hall_Monitor() {
            list = new ArrayList<Object>();
            hallway = findViewById(R.id.LinearLayoutHallway);
        }

        public void addObject(Object o) {
            ObjectView obj = new ObjectView(o, getApplicationContext());
            obj.changeOrientation(LinearLayout.VERTICAL);
            hallway.addView(obj);
        }

        public void getObjectByIndex(int i) {
            //TODO: Implement
        }

        public void getObjectList() {
            //TODO: Implement
        }

        public void removeObject(Object o) {
            for(int i = 0; i < hallway.getChildCount(); i++) {
                ObjectView obj = (ObjectView)hallway.getChildAt(i);
                if(obj.contains(o)){
                    hallway.removeViewAt(i);
                    return;
                }
            }
        }

        public void removeObjectByIndex(int i) {
            //TODO: Implement
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//set orientation to lock on portrait
        simulation_manager = new Simulation_Manager(0,100,1);
        hall_monitor = new Hall_Monitor();
        FileHelper theFileHelper = new FileHelper();
        String jsonString = theFileHelper.ReadFile(getApplicationContext());
        if(jsonString != "") {
            Gson gson = new Gson();
            simulation_manager = gson.fromJson(jsonString, Simulation_Manager.class);
            renderUIFromManager();
        }
        //TODO: TEST CODE REMOVE PLZ
      /*  Procedure proc = new Procedure("Bark", 3, 5);
        Scope_Type type = new Scope_Type("TESTSCOPE", 5);
        type.addProcedure(proc);
        Scope scope = new Scope(type, 5);

        simulation_manager.addProcedure(proc);
        simulation_manager.addScopeType(type);
        simulation_manager.addScope(scope);*/
        //TEST CODE REMOVE PLZ
        currentClicked = null;
    }

    //handle view bar button clicks
    public void buttonClick(View view){
        if (view.getId() == R.id.buttonViewProcedures) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            String[] procedureNames = simulation_manager.getProcedureNames();
            for (int i=0; i < procedureNames.length; i++) {
                popupMenu.getMenu().add(Menu.NONE, i, i, procedureNames[i]);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String name = item.getTitle().toString();

                    Procedure procedure = simulation_manager.getProcedureByName(name);
                    Intent procedureIntent = new Intent(getApplicationContext(), ProcedureActivity.class);
                    procedureIntent.putExtra("procedure", procedure);
                    startActivityForResult(procedureIntent, procedure_Request);
                    return true;
                }
            });
            popupMenu.show();
        } else if (view.getId() == R.id.buttonViewScopeTypes) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            String[] scopeTypeNames = simulation_manager.getScopeTypeNames();
            for (int i=0; i < scopeTypeNames.length; i++) {
                popupMenu.getMenu().add(Menu.NONE, i, i, scopeTypeNames[i]);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String name = item.getTitle().toString();

                    Scope_Type scope_type = simulation_manager.getScopeTypeByName(name);
                    Intent scopeTypeIntent = new Intent(getApplicationContext(), ScopeTypeActivity.class);
                    scopeTypeIntent.putExtra("scopeType", scope_type);
                    scopeTypeIntent.putExtra("procedureNames", simulation_manager.getProcedureNames());
                    startActivityForResult(scopeTypeIntent, scopeType_Request);
                    return true;
                }
            });
            popupMenu.show();
        }
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
        else if (id == R.id.addScope) {
            Intent scopeIntent = new Intent(getApplicationContext(), ScopeActivity.class);
            Scope scope = new Scope(null, -1);
            scopeIntent.putExtra("scope", scope);
            scopeIntent.putExtra("scopeTypeNames", simulation_manager.getScopeTypeNames());
            startActivityForResult(scopeIntent, scope_Request);
        }
        else if (id == R.id.addScopeType) {
            Intent scopeTypeIntent = new Intent(getApplicationContext(), ScopeTypeActivity.class);
            Scope_Type scopeType = new Scope_Type("", -1);
            scopeTypeIntent.putExtra("scopeType", scopeType);
            scopeTypeIntent.putExtra("procedureNames", simulation_manager.getProcedureNames());
            startActivityForResult(scopeTypeIntent, scopeType_Request);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //returned from ProcedureRoomActivity
        if (requestCode == procedureRoom_Request) {
            //added a procedureRoom
            if (resultCode == RESULT_FIRST_USER) {

                ProcedureRoom procedureRoom = (ProcedureRoom) data.getSerializableExtra("procedureRoom");
                simulation_manager.addProcedureRoom(procedureRoom);

                LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                ObjectView roomImage = new ObjectView(procedureRoom, getApplicationContext());
                roomImage.changeOrientation(LinearLayout.HORIZONTAL);
                roomImage.setOnClickListener(this);

                int index = simulation_manager.getProcedureRoomNum() - 1;
                Tag tag = new Tag(index, "Procedure Room");

                roomImage.setTag(tag);

                linearLayoutRooms.addView(roomImage);

                //edited or deleted a procedureRoom
            } else if (resultCode == RESULT_OK) {
                ProcedureRoom procedureRoom = (ProcedureRoom) data.getSerializableExtra("procedureRoom");
                int index = currentClicked.index;

                //deleting a procedureRoom
                if (procedureRoom.getTravelTime() <= 0 || procedureRoom.getCooldownTime() <= 0) {
                    simulation_manager.deleteProcedureRoom(index);

                    LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                    View room = linearLayoutRooms.getChildAt(simulation_manager.getProcedureRoomNum()); //TODO:: not -1?????/
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
                int arrivalTime = data.getIntExtra("arrivalTime", simulation_manager.getLatestClientTime());
                Client client = new Client(new ArrayList<Procedure>(), arrivalTime);
                String[] procedures = data.getStringArrayExtra("procedures");
                for (int i = 0; i < procedures.length; i++) {
                    client.addProcedure(simulation_manager.getProcedureByName(procedures[i]));
                }

                int amount = data.getIntExtra("amount", 1);
                for (int i = 0; i < amount; i++) {
                    Client newClient = new Client(client);
                    simulation_manager.addClient(newClient);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
                    /*
                    ImageView clientImage = new ImageView(getApplicationContext());
                    clientImage.setImageResource(R.drawable.client);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    clientImage.setLayoutParams(layoutParams);
                    clientImage.setPadding(10, 0, 0, 0);
                    clientImage.setOnClickListener(this);
                    */
                    ObjectView clientImage = new ObjectView(newClient, getApplicationContext());
                    clientImage.changeOrientation(LinearLayout.HORIZONTAL);
                    clientImage.setOnClickListener(this);

                    int index = simulation_manager.getClientNum() - 1;
                    Tag tag = new Tag(index, "Client");

                    clientImage.setTag(tag);

                    linearLayoutClients.addView(clientImage);
                }

                //edited or deleted a client
            } else if (resultCode == RESULT_OK) {
                Client client = (Client) data.getSerializableExtra("client");
                int index = currentClicked.index;

                //deleting a client
                if (client.getProcedureList() == null || client.getArrivalTime() < 0) {
                    simulation_manager.deleteClient(index);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
                    View clientView = linearLayoutClients.getChildAt(simulation_manager.getClientNum()); //TODO:: not -1???
                    linearLayoutClients.removeView(clientView);

                    //editing a client
                } else {
                    String[] procedures = data.getStringArrayExtra("procedures");
                    client.setProcedureList(new ArrayList<Procedure>());
                    for (int i = 0; i < procedures.length; i++) {
                        client.addProcedure(simulation_manager.getProcedureByName(procedures[i]));
                    }
                    simulation_manager.editClient(client, index);
                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
                    ObjectView clientView = (ObjectView) linearLayoutClients.getChildAt(index);
                    clientView.update();
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

        if (requestCode == scope_Request) {

            if (resultCode == RESULT_FIRST_USER) {//adding
                String type = (String) data.getSerializableExtra("scopeType");
                int clean = (int) data.getSerializableExtra("cleaningTime");

                Scope_Type scopeType = simulation_manager.getScopeTypeByName(type);

                Scope scope = new Scope(scopeType, clean);

                simulation_manager.addScope(scope);

                LinearLayout linearLayoutScope = findViewById(R.id.LinearLayoutScopes);
                ObjectView scopeImage = new ObjectView(scope, getApplicationContext());
                scopeImage.changeOrientation(LinearLayout.HORIZONTAL);
                scopeImage.setOnClickListener(this);

                int index = simulation_manager.getScopeNum() - 1;
                Tag tag = new Tag(index, "Scope");

                scopeImage.setTag(tag);

                linearLayoutScope.addView(scopeImage);


            } else if (resultCode == RESULT_OK) {
                String type = (String) data.getSerializableExtra("scopeType");
                int clean = (int) data.getSerializableExtra("cleaningTime");

                int index = currentClicked.index;

                //Deleting
                if (clean < 0) {
                    simulation_manager.removeScope(index);
                    LinearLayout linearLayoutScopes = findViewById(R.id.LinearLayoutScopes);
                    View scopeImg = linearLayoutScopes.getChildAt(simulation_manager.getScopeNum()); //TODO:: not -1?????/
                    linearLayoutScopes.removeView(scopeImg);
                }
                //Editing
                else {
                    Scope_Type scopeType = simulation_manager.getScopeTypeByName(type);
                    Scope scope = new Scope(scopeType, clean);
                    simulation_manager.removeScope(index);
                    simulation_manager.addScope(scope);
                }
            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }



        if (requestCode == scopeType_Request) {
            if (resultCode == RESULT_FIRST_USER) {
                String name = data.getStringExtra("name");
                int price = data.getIntExtra("price", 1);
                Scope_Type scope_type = new Scope_Type(name, price);
                String[] procedureList = data.getStringArrayExtra("procedures");
                for (int i=0; i < procedureList.length; i++) {
                    scope_type.addProcedure(simulation_manager.getProcedureByName(procedureList[i]));
                }
                simulation_manager.addScopeType(scope_type);

            } else if (resultCode == RESULT_OK) {
                Scope_Type scope_type = (Scope_Type) data.getSerializableExtra("scopeType");

                //Deleting
                if (scope_type.getPrice() <= 0 || scope_type.getProcedureList() == null) {
                    simulation_manager.removeScopeType(scope_type.getName());
                }
                //Editing
                else {
                    String oldName = data.getStringExtra("oldName");
                    simulation_manager.removeScopeType(oldName);

                    scope_type.setProcedureList(new ArrayList<Procedure>());
                    String[] procedureList = data.getStringArrayExtra("procedures");
                    for (int i=0; i < procedureList.length; i++) {
                        scope_type.addProcedure(simulation_manager.getProcedureByName(procedureList[i]));
                    }
                    simulation_manager.addScopeType(scope_type);
                }

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

        else if (type.equals("Client")) {
            Client client = simulation_manager.getClient(index);

            Intent clientIntent = new Intent(getApplicationContext(), ClientActivity.class);
            clientIntent.putExtra("client", client);
            clientIntent.putExtra("procedures", simulation_manager.getProcedureNames());
            startActivityForResult(clientIntent, client_Request);
        }

        else if(type.equals("Scope")) {
            Scope scope = simulation_manager.getScopeByIndex(index);

            Intent scopeIntent = new Intent(getApplicationContext(), ScopeActivity.class);
            scopeIntent.putExtra("scope", scope);
            scopeIntent.putExtra("scopeTypeNames", simulation_manager.getScopeTypeNames());
            startActivityForResult(scopeIntent, scope_Request);
        }
    }

    public void renderUIFromManager() {
        Integer clientNum = simulation_manager.getClientNum();
        LinearLayout linearLayoutClient = findViewById(R.id.LinearLayoutClients);
        populateSection(linearLayoutClient, R.drawable.client, "Client", clientNum);

        Integer procedureRoomNum = simulation_manager.getProcedureRoomNum();
        LinearLayout linearLayoutProcedureRoom = findViewById(R.id.LinearLayoutRooms);
        populateSection(linearLayoutProcedureRoom, R.drawable.procedure_room, "Procedure Room", procedureRoomNum);

        Integer scopeNum = simulation_manager.getScopeNum();
        LinearLayout linearLayoutScope = findViewById(R.id.LinearLayoutScopes);
        populateSection(linearLayoutScope, R.drawable.scope, "Scope", scopeNum);
    }

    public void populateSection(LinearLayout theLinearLayout, int resID, String tagType, Integer elemNum) {
        theLinearLayout.removeAllViews();
        for(int index = 0; index < elemNum; index++) {
            ImageView theImage = new ImageView(getApplicationContext());
            theImage.setImageResource(resID);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
            theImage.setLayoutParams(layoutParams);
            theImage.setPadding(10, 0, 0, 0);
            theImage.setOnClickListener(this);

            Tag tag = new Tag(index, tagType);

            theImage.setTag(tag);

            theLinearLayout.addView(theImage);
        }
    }

    static boolean isRunning = false;
    static boolean isPaused = false;

    public void startSimulation(final MenuItem item) {
        FileHelper theFileHelper = new FileHelper();
        Gson gson = new Gson();
        String jsonString = gson.toJson(simulation_manager);
        theFileHelper.writeFileOnInternalStorage(getApplicationContext(), jsonString);
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
        TextView textTime = findViewById(R.id.textViewTime);
        textTime.setText(Time.convertToString(simulation_manager.getCurrTime()));

        int numClients = simulation_manager.getClientNum();
        int numProcedureRooms = simulation_manager.getProcedureRoomNum();
        int numScopes = simulation_manager.getScopeNum();

        LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
        for(int i = 0; i < numClients; i++)
        {
            Client client = simulation_manager.getClient(i);

            if(client.getState() == State.STATE_OPERATION && client.getuiUpdated() == false) {
                ObjectView clientImage = new ObjectView(client, getApplicationContext());
                clientImage.changeOrientation(LinearLayout.HORIZONTAL);
                clientImage.setOnClickListener(this);

                linearLayoutClients.addView(clientImage);
                client.setuiUpdated(true);
                hall_monitor.removeObject(client);
            }

            ObjectView clientImg = (ObjectView)linearLayoutClients.getChildAt(i);

            if(clientImg != null) {
                clientImg.changeObject(client);

                if (client.getState() == State.STATE_WAIT)
                {
                    clientImg.setBackgroundColor(Color.BLUE);
                }
                else if (client.getState() == State.STATE_TRAVEL && client.getuiUpdated() == false)
                {
                    linearLayoutClients.removeView(clientImg);
                    hall_monitor.addObject(client);
                    client.setuiUpdated(true);
                }
                else if (client.getState() == State.STATE_OPERATION)
                {
                    clientImg.setBackgroundColor(Color.YELLOW);
                }
                else if (client.getState() == State.STATE_DONE)
                {
                    clientImg.setBackgroundColor(Color.GREEN);
                }
            }
        }
        LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
        for(int i = 0; i < numProcedureRooms; i++) {
            ProcedureRoom room = simulation_manager.getProcedureRoom(i);
            View roomImg = linearLayoutRooms.getChildAt(i);

            if (room.isAvailable()) {
                roomImg.setBackgroundColor(Color.BLUE);
            } else if (room.isOccupied()) {
                roomImg.setBackgroundColor(Color.YELLOW);
            } else {
                roomImg.setBackgroundColor(Color.GREEN);
            }
        }

        LinearLayout linearLayoutScopes = findViewById(R.id.LinearLayoutScopes);
        for (int i=0; i < numScopes; i++) {
            Scope scope = simulation_manager.getScopeByIndex(i);
            
            if(scope.getState() == State_Scope.STATE_USE && scope.getuiUpdated() == false){
                ObjectView scopeImage = new ObjectView(scope, getApplicationContext());
                scopeImage.changeOrientation(LinearLayout.HORIZONTAL);
                scopeImage.setOnClickListener(this);

                linearLayoutScopes.addView(scopeImage);
                scope.setuiUpdated(true);
                hall_monitor.removeObject(scope);
            }

            ObjectView scopeImg = (ObjectView)linearLayoutScopes.getChildAt(i);

            if(scopeImg != null) {
                scopeImg.changeObject(scope);

                if (scope.getState() == State_Scope.STATE_FREE) {
                    scopeImg.setBackgroundColor(Color.BLUE);

                } else if (scope.getState() == State_Scope.STATE_TRAVEL && scope.getuiUpdated() == false) {
                    linearLayoutScopes.removeView(scopeImg);
                    hall_monitor.addObject(scope);
                    scope.setuiUpdated(true);

                } else if (scope.getState() == State_Scope.STATE_USE) {
                    scopeImg.setBackgroundColor(Color.YELLOW);

                } else if (scope.getState() == State_Scope.STATE_DIRTY) {
                    scopeImg.setBackgroundColor(Color.GREEN);

                }
            }
        }
    }
}
