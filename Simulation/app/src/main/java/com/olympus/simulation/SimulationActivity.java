package com.olympus.simulation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationActivity
        extends AppCompatActivity
        implements View.OnClickListener,
        SavePromptDialog.SavePromptDialogListener,
        LoadPromptDialog.LoadPromptDialogListener,
        SetTimePromptDialog.SetTimePromptDialogListener {

    Simulation_Manager simulation_manager;
    Hall_Monitor hall_monitor;
    Tag currentClicked;
    Menu menu;

    public static final int procedureRoom_Request = 1;
    public static final int client_Request = 2;
    public static final int procedure_Request = 3;
    public static final int scopeType_Request = 4;
    public static final int scope_Request = 5;
    public static final int towerType_Request = 6;

    public static final int element_Request = 8;

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
            list.add(o);
        }

        public void renderList() {
            hallway.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                ObjectView obj = new ObjectView(o, getApplicationContext());
                obj.changeOrientation(LinearLayout.VERTICAL);
                hallway.addView(obj);
            }
        }

        public void setListFromBackend() {
            list = simulation_manager.getHallway();
        }

        public void removeObject(Object o) {
            for (int i = 0; i < hallway.getChildCount(); i++) {
                ObjectView obj = (ObjectView) hallway.getChildAt(i);
                if (obj.contains(o)) {
                    hallway.removeViewAt(i);
                    list.remove(o);
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

        simulation_manager = new Simulation_Manager(0, 720, 1);
        hall_monitor = new Hall_Monitor();
        currentClicked = null;

        updateActorUI(); //TODO::::

    }

    //handle view bar button clicks
    public void buttonClick(View view) {
        if (view.getId() == R.id.buttonViewProcedures) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            String[] procedureNames = simulation_manager.getProcedureNames();
            for (int i = 0; i < procedureNames.length; i++) {
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
            for (int i = 0; i < scopeTypeNames.length; i++) {
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
        } else if (view.getId() == R.id.textViewTime) {
            if (!simulationStarted) {
                SetTimePromptDialog setTimePromptDialog = new SetTimePromptDialog(simulation_manager.getStartTime(), simulation_manager.getEndTime());
                setTimePromptDialog.show(getSupportFragmentManager(), "set time dialog");
            }
        } else if (view.getId() == R.id.buttonViewLeakTesterTypes) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            String[] leakTesterNames = simulation_manager.getLeakTesterTypeNames();
            for (int i = 0; i < leakTesterNames.length; i++) {
                popupMenu.getMenu().add(Menu.NONE, i, i, leakTesterNames[i]);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String name = item.getTitle().toString();

                    LeakTester_Type leakTesterType = simulation_manager.getLeakTesterByName(name);
                    currentClicked = new Tag(-1, leakTesterType.getName());
                    Intent scopeTypeIntent = new Intent(getApplicationContext(), ElementActivity.class);
                    scopeTypeIntent.putExtra("element", leakTesterType);
                    scopeTypeIntent.putExtra("mode", "view");
                    startActivityForResult(scopeTypeIntent, element_Request);
                    return true;
                }
            });
            popupMenu.show();

        } else if (view.getId() == R.id.buttonViewTowerTypes) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            String[] towerTypeNames = simulation_manager.getTowerTypeNames();
            for (int i = 0; i < towerTypeNames.length; i++) {
                popupMenu.getMenu().add(Menu.NONE, i, i, towerTypeNames[i]);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String name = item.getTitle().toString();

                    Tower_Type tower_type = simulation_manager.getTowerTypeByName(name);
                   // currentClicked = new Tag(-1, tower_type.getName());
                    Intent towerTypeIntent = new Intent(getApplicationContext(), TowerTypeActivity.class);
                    towerTypeIntent.putExtra("towerType", tower_type);
                    towerTypeIntent.putExtra("scopeTypes", simulation_manager.getScopeTypeNames());
                    startActivityForResult(towerTypeIntent, towerType_Request);
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
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.plus_button) {
            MenuItem addClient = menu.findItem(R.id.addClient);
            MenuItem addScope = menu.findItem(R.id.addScope);
            MenuItem addScopeType = menu.findItem(R.id.addScopeType);
            MenuItem addTowerType = menu.findItem(R.id.addTowerType);
            MenuItem addProcedureRoom = menu.findItem(R.id.addProcedureRoom);
            MenuItem addManualCleaningStation = menu.findItem(R.id.addManualCleaningStation);

            if (simulation_manager.getProcedureNum() > 0) {
                addClient.setVisible(true);
                addScopeType.setVisible(true);
            } else {
                addClient.setVisible(false);
                addScopeType.setVisible(false);
            }
            String[] scopeTypes = simulation_manager.getScopeTypeNames();
            if (scopeTypes == null || scopeTypes.length <= 0) {
                addScope.setVisible(false);
                addTowerType.setVisible(false);
            } else {
                addScope.setVisible(true);
                addTowerType.setVisible(true);
            }
            if (simulation_manager.getTowerTypeNum() > 0) {
                addProcedureRoom.setVisible(true);
            } else {
                addProcedureRoom.setVisible(false);
            }
            if (simulation_manager.getLeakTesterTypeNum() <= 0) {
                addManualCleaningStation.setVisible(false);
            } else  {
                addManualCleaningStation.setVisible(true);
            }
        }
        else if (id == R.id.addProcedureType) {
            Intent procedureIntent = new Intent(getApplicationContext(), ProcedureActivity.class);
            Procedure procedure = new Procedure("", 0);
            procedureIntent.putExtra("procedure", procedure);
            startActivityForResult(procedureIntent, procedure_Request);
        } else if (id == R.id.addClient) {
            int latest = simulation_manager.getLatestClientTime();

            Intent clientIntent = new Intent(getApplicationContext(), ClientActivity.class);
            Client client = new Client(null, latest);
            clientIntent.putExtra("client", client);
            clientIntent.putExtra("procedures", simulation_manager.getProcedureNames());
            clientIntent.putExtra("startTime", simulation_manager.getStartTime());
            clientIntent.putExtra("endTime", simulation_manager.getEndTime());
            startActivityForResult(clientIntent, client_Request);
        } else if (id == R.id.addProcedureRoom) {
            Intent procedureRoomIntent = new Intent(getApplicationContext(), ElementActivity.class);
            ProcedureRoom procedureRoom = new ProcedureRoom(-1, -1);
            procedureRoomIntent.putExtra("element", procedureRoom);
            procedureRoomIntent.putExtra("mode", "add");
            procedureRoomIntent.putExtra("towerTypes", simulation_manager.getTowerTypeNames());
            startActivityForResult(procedureRoomIntent, element_Request);
        } else if (id == R.id.addScope) {
            Intent scopeIntent = new Intent(getApplicationContext(), ScopeActivity.class);
            Scope scope = new Scope(null);
            scopeIntent.putExtra("scope", scope);
            scopeIntent.putExtra("scopeTypeNames", simulation_manager.getScopeTypeNames());
            startActivityForResult(scopeIntent, scope_Request);
        } else if (id == R.id.addScopeType) {
            Intent scopeTypeIntent = new Intent(getApplicationContext(), ScopeTypeActivity.class);
            Scope_Type scopeType = new Scope_Type("", -1, -1);
            scopeTypeIntent.putExtra("scopeType", scopeType);
            scopeTypeIntent.putExtra("procedureNames", simulation_manager.getProcedureNames());
            startActivityForResult(scopeTypeIntent, scopeType_Request);
        } else if (id == R.id.addTowerType) {
            Intent towerTypeIntent = new Intent(getApplicationContext(), TowerTypeActivity.class);
            Tower_Type type = new Tower_Type(null, null, 0);
            towerTypeIntent.putExtra("towerType", type);
            towerTypeIntent.putExtra("scopeTypes", simulation_manager.getScopeTypeNames());
            startActivityForResult(towerTypeIntent, towerType_Request);
        } else if (id == R.id.addLeakTesterType) {
            Intent leakTesterTypeIntent = new Intent(getApplicationContext(), ElementActivity.class);
            LeakTester_Type leakTester = new LeakTester_Type("", -1, -1, -1);
            leakTesterTypeIntent.putExtra("element", leakTester);
            leakTesterTypeIntent.putExtra("mode", "add");
            startActivityForResult(leakTesterTypeIntent, element_Request);
        } else if (id == R.id.addManualCleaningStation) {
            Intent sinkIntent = new Intent(getApplicationContext(), ElementActivity.class);
            ManualCleaningStation sink = new ManualCleaningStation(null);
            sinkIntent.putExtra("element", sink);
            sinkIntent.putExtra("mode", "add");
            sinkIntent.putExtra("leakTesterTypes", simulation_manager.getLeakTesterTypeNames());
            startActivityForResult(sinkIntent, element_Request);
        } else if (id == R.id.startSimulation) {
            ActionMenuItemView playImage = findViewById(R.id.playButton);
            if (item.getTitle().equals("Confirm Start")) {
                startSimulation(item);
                item.setTitle("Pause");
                playImage.setIcon(getResources().getDrawable(R.drawable.pause_button));
            } else if (item.getTitle().equals("Pause")) {
                isPaused = true;
                item.setTitle("Resume");
                playImage.setIcon(getResources().getDrawable(R.drawable.play_button));
            } else {
                isPaused = false;
                item.setTitle("Pause");
                playImage.setIcon(getResources().getDrawable(R.drawable.pause_button));
                //loadLoadout(true, "LastRun");
            }
        } else if (id == R.id.saveLoadout) {
            isPaused = true;
            SavePromptDialog savePromptDialog = new SavePromptDialog();
            savePromptDialog.show(getSupportFragmentManager(), "save dialog");
        } else if (id == R.id.loadLoadout) {
            isPaused = true;
            LoadPromptDialog loadPromptDialog = new LoadPromptDialog();
            loadPromptDialog.show(getSupportFragmentManager(), "load dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            renderUIFromManager();//TODO:::
            return;
        }

        if (requestCode == element_Request) {
            Element element = (Element) data.getSerializableExtra("element");
            String[] list = (String[]) data.getSerializableExtra("list");

            if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {
                ProcedureRoom procedureRoom = (ProcedureRoom) element;

                if (list != null) {
                    for (int i = 0; i < list.length; i++) {
                        Tower_Type type = simulation_manager.getTowerTypeByName(list[i]);
                        procedureRoom.addTowerType(type);
                    }
                }

                //adding a new procedure room
                if (resultCode == RESULT_FIRST_USER) {
                    simulation_manager.addProcedureRoom(procedureRoom);

                    /*LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                    ObjectView roomImage = new ObjectView(procedureRoom, getApplicationContext());
                    roomImage.changeOrientation(LinearLayout.HORIZONTAL);
                    roomImage.setOnClickListener(this);

                    int index = simulation_manager.getProcedureRoomNum() - 1;
                    Tag tag = new Tag(index, "Procedure Room");

                    roomImage.setTag(tag);

                    linearLayoutRooms.addView(roomImage);*/


                } else if (resultCode == RESULT_OK) {
                    int index = currentClicked.index;

                    //editing a procedure room
                    if (procedureRoom.validate()) {
                        simulation_manager.editProcedureRoom(procedureRoom, index);

                        //deleting a procedure room
                    } else {
                        simulation_manager.deleteProcedureRoom(index);

                        /*LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                        View room = linearLayoutRooms.getChildAt(simulation_manager.getProcedureRoomNum());
                        linearLayoutRooms.removeView(room);*/
                    }

                }
                renderUIFromManager();

            } else if (element.equals(Element.ELEMENT_LEAKTESTERTYPE)) {
                LeakTester_Type leakTester_type = (LeakTester_Type) element;
                if (resultCode == RESULT_FIRST_USER) {
                    simulation_manager.addLeakTester(leakTester_type);
                } else if (resultCode == RESULT_OK) {
                    //remove for deleting or editing
                    simulation_manager.removeLeakTesterTypeByName(currentClicked.type);

                    //if editing add new one back in
                    if (leakTester_type.validate()) {
                        simulation_manager.addLeakTester(leakTester_type);
                    }
                }
                renderUIFromManager();
            }else if (element.equals(Element.ELEMENT_NURSE)) {
                if (resultCode == RESULT_OK) {
                    int number = data.getIntExtra("number", -1);
                    int cooldown = data.getIntExtra("cooldown", -1);
                    simulation_manager.setNurseNum(number);
                    simulation_manager.setNursePostProcedureTime(cooldown);
                    updateActorUI();
                }
            } else if (element.equals(Element.ELEMENT_DOCTOR)) {//TODO::: fix for doctor changes
                if (resultCode == RESULT_OK) {
                    int number = data.getIntExtra("number", -1);
                    int cooldown = data.getIntExtra("cooldown", -1);
                    simulation_manager.setDoctorNum(number);
                    simulation_manager.setDoctorPostProcedureTime(cooldown);
                    updateActorUI();
                }
            } else if (element.equals(Element.ELEMENT_SINK)) {
                if (resultCode == RESULT_FIRST_USER) {
                    LeakTester_Type leakTester_type = simulation_manager.getLeakTesterByName(list[0]);
                    ManualCleaningStation manualCleaningStation = new ManualCleaningStation(leakTester_type);
                    simulation_manager.addManualCleaningStation(manualCleaningStation);
                } else if (resultCode == RESULT_OK) {
                    ManualCleaningStation manualCleaningStation = (ManualCleaningStation)element;
                    //remove old if edited or deleted
                    simulation_manager.removeManualCleaningStationByLeakTester(currentClicked.type);

                    if (manualCleaningStation.validate()) {
                        LeakTester_Type leakTester_type = simulation_manager.getLeakTesterByName(list[0]);
                        manualCleaningStation.setCurrentLeakTester(leakTester_type);
                        simulation_manager.addManualCleaningStation(manualCleaningStation);
                    }
                }

                renderUIFromManager();
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
                    ObjectView clientImage = new ObjectView(newClient, getApplicationContext());
                    clientImage.changeOrientation(LinearLayout.HORIZONTAL);
                    clientImage.setOnClickListener(this);

                    int index = simulation_manager.getClientNum() - 1;
                    Tag tag = new Tag(index, "Client");

                    clientImage.setTag(tag);

                    linearLayoutClients.addView(clientImage);
                    if (simulation_manager.removeClientsOutsideRange() > 0) {
                        Toast.makeText(getApplicationContext(), "Patients outside of range have been deleted!", Toast.LENGTH_LONG).show();
                    }
                    renderUIFromManager();
                }

                //edited or deleted a client
            } else if (resultCode == RESULT_OK) {
                Client client = (Client) data.getSerializableExtra("client");
                int index = currentClicked.index;

                //deleting a client
                if (client.getProcedureList() == null || client.getArrivalTime() < 0) {
                    simulation_manager.deleteClient(index);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
                    View clientView = linearLayoutClients.getChildAt(simulation_manager.getClientNum());
                    linearLayoutClients.removeView(clientView);
                    renderUIFromManager();
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
                    if (simulation_manager.removeClientsOutsideRange() > 0) {
                        Toast.makeText(getApplicationContext(), "Patients outside of range have been deleted!", Toast.LENGTH_LONG).show();
                    }
                    renderUIFromManager();
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
                if (procedure.getTime() <= 0) {
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
                Scope_Type scopeType = simulation_manager.getScopeTypeByName(type);

                int amount = (int) data.getSerializableExtra("amount");

                for (int i = 0; i < amount; i++) {
                    Scope scope = new Scope(scopeType);

                    simulation_manager.addScope(scope);

                    LinearLayout linearLayoutScope = findViewById(R.id.LinearLayoutScopes);
                    ObjectView scopeImage = new ObjectView(scope, getApplicationContext());
                    scopeImage.changeOrientation(LinearLayout.HORIZONTAL);
                    scopeImage.setOnClickListener(this);

                    int index = simulation_manager.getScopeNum() - 1;
                    Tag tag = new Tag(index, "Scope");

                    scopeImage.setTag(tag);

                    linearLayoutScope.addView(scopeImage);
                }

                renderUIFromManager();

            } else if (resultCode == RESULT_OK) {
                String type = (String) data.getSerializableExtra("scopeType");

                int index = currentClicked.index;

                //Deleting
                if (type == null) {
                    simulation_manager.removeScope(index);
                    LinearLayout linearLayoutScopes = findViewById(R.id.LinearLayoutScopes);
                    View scopeImg = linearLayoutScopes.getChildAt(simulation_manager.getScopeNum());
                    linearLayoutScopes.removeView(scopeImg);
                    renderUIFromManager();
                }
                //Editing
                else {
                    Scope_Type scopeType = simulation_manager.getScopeTypeByName(type);
                    Scope scope = new Scope(scopeType);
                    simulation_manager.removeScope(index);
                    simulation_manager.addScope(scope);
                    renderUIFromManager();
                }
            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }


        if (requestCode == scopeType_Request) {
            if (resultCode == RESULT_FIRST_USER) {
                String name = data.getStringExtra("name");
                int price = data.getIntExtra("price", 1);
                int cleaningTime = data.getIntExtra("cleaningTime", 1);
                Scope_Type scope_type = new Scope_Type(name, cleaningTime, price);
                String[] procedureList = data.getStringArrayExtra("procedures");
                for (int i = 0; i < procedureList.length; i++) {
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
                    for (int i = 0; i < procedureList.length; i++) {
                        scope_type.addProcedure(simulation_manager.getProcedureByName(procedureList[i]));
                    }
                    simulation_manager.addScopeType(scope_type);
                }

            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }

        if (requestCode == towerType_Request) {
            if (resultCode == RESULT_FIRST_USER) {
                String name = data.getStringExtra("name");
                int price = data.getIntExtra("price", 0);
                String[] scopeTypeList = data.getStringArrayExtra("scopeTypes");
                ArrayList<Scope_Type> scopeTypes = new ArrayList<>();
                for (int i = 0; i < scopeTypeList.length; i++) {
                    scopeTypes.add(simulation_manager.getScopeTypeByName(scopeTypeList[i]));
                }
                Tower_Type type = new Tower_Type(name, scopeTypes, price);
                simulation_manager.addTowerType(type);
            } else if (resultCode == RESULT_OK) {
                String oldName = data.getStringExtra("oldName");
                simulation_manager.removeTowerTypeByName(oldName);

                String name = data.getStringExtra("name");
                int price = data.getIntExtra("price", 0);
                String[] scopeTypeList = data.getStringArrayExtra("scopeTypes");
                ArrayList<Scope_Type> scopeTypes = new ArrayList<>();
                for (int i = 0; i < scopeTypeList.length; i++) {
                    scopeTypes.add(simulation_manager.getScopeTypeByName(scopeTypeList[i]));
                }
                Tower_Type type = new Tower_Type(name, scopeTypes, price);
                simulation_manager.addTowerType(type);
            } else if (resultCode == RESULT_CANCELED) {
                return;
            }
        }
    }

    public void onClick(View view) {
        Tag tag = (Tag) view.getTag();
        currentClicked = tag;
        int index = tag.index;
        String type = tag.type;

        if (type.equals("Procedure Room")) {

            ProcedureRoom procedureRoom = simulation_manager.getProcedureRoom(index);
            Intent procedureRoomIntent = new Intent(getApplicationContext(), ElementActivity.class);
            procedureRoomIntent.putExtra("element", procedureRoom);
            procedureRoomIntent.putExtra("mode", "view");
            procedureRoomIntent.putExtra("towerTypes", simulation_manager.getTowerTypeNames());
            startActivityForResult(procedureRoomIntent, element_Request);

        } else if (type.equals("Client")) {
            Client client = simulation_manager.getClient(index);

            Intent clientIntent = new Intent(getApplicationContext(), ClientActivity.class);
            clientIntent.putExtra("client", client);
            clientIntent.putExtra("procedures", simulation_manager.getProcedureNames());
            clientIntent.putExtra("startTime", simulation_manager.getStartTime());
            clientIntent.putExtra("endTime", simulation_manager.getEndTime());
            startActivityForResult(clientIntent, client_Request);
        } else if (type.equals("Scope")) {
            Scope scope = simulation_manager.getScopeByIndex(index);

            Intent scopeIntent = new Intent(getApplicationContext(), ScopeActivity.class);
            scopeIntent.putExtra("scope", scope);
            scopeIntent.putExtra("scopeTypeNames", simulation_manager.getScopeTypeNames());
            startActivityForResult(scopeIntent, scope_Request);
        } else if (type.equals("Nurse")) {
            Intent nurseIntent = new Intent(getApplicationContext(), ElementActivity.class);
            Nurse nurse = new Nurse();
            nurseIntent.putExtra("element", nurse);
            nurseIntent.putExtra("mode", "actor");
            nurseIntent.putExtra("number", simulation_manager.getNurseNum());
            nurseIntent.putExtra("cooldown", simulation_manager.getNursePostProcedureTime());
            startActivityForResult(nurseIntent, element_Request);
        } else if (type.equals("Technician")) {
            Intent techIntent = new Intent(getApplicationContext(), ElementActivity.class);
            Technician technician = new Technician();
            techIntent.putExtra("element", technician);
            techIntent.putExtra("mode", "actor");
            techIntent.putExtra("number", simulation_manager.getTechnicianNum());
            startActivityForResult(techIntent, element_Request);
        }else if (type.equals("Doctor")) {//TODO:: fix for doctor
            Intent doctorIntent = new Intent(getApplicationContext(), ElementActivity.class);
            Doctor doctor = new Doctor(null);
            doctorIntent.putExtra("element", doctor);
            doctorIntent.putExtra("mode", "actor");
            doctorIntent.putExtra("number", simulation_manager.getDoctorNum());
            doctorIntent.putExtra("cooldown", simulation_manager.getDoctorPostProcedureTime());
            startActivityForResult(doctorIntent, element_Request);

        } else if (type.equals("Sink")) {
            Intent sinkIntent = new Intent(getApplicationContext(), ElementActivity.class);
            ManualCleaningStation manualCleaningStation = simulation_manager.getManualCleaningStationByIndex(index);
            sinkIntent.putExtra("element", manualCleaningStation);
            sinkIntent.putExtra("mode", "view");
            sinkIntent.putExtra("leakTesterTypes", simulation_manager.getLeakTesterTypeNames());
            currentClicked.type = manualCleaningStation.getCurrentLeakTester().getName();
            startActivityForResult(sinkIntent, element_Request);
        }
    }

    public void saveLoadout(String fileName) {
        if (fileName != null && fileName.equals("LastRun")) {
            Toast.makeText(getApplicationContext(), "You Cannot Save to LastRun!", Toast.LENGTH_LONG).show();
            return;
        } else if (fileName == null) {
            fileName = "LastRun";
        } else if (fileName.equals("")) {
            Toast.makeText(getApplicationContext(), "Invalid Entry!", Toast.LENGTH_LONG).show();
            return;
        }
        fileName += ".json";
        FileHelper theFileHelper = new FileHelper(fileName);
        GsonBuilder gsonBuilder  = new GsonBuilder();
        // Allowing the serialization of static fields

        gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        Gson gson = gsonBuilder.create();
        //Gson gson = new Gson();
        String jsonString;
        if (!ranAlready) {
            jsonString = gson.toJson(simulation_manager);
        } else {
            FileHelper theFileHelper2 = new FileHelper();
            jsonString = theFileHelper2.ReadFile(getApplicationContext());
        }
        theFileHelper.writeFileOnInternalStorage(getApplicationContext(), jsonString);
        Toast.makeText(getApplicationContext(), "Saved Setup to " + fileName, Toast.LENGTH_LONG).show();
    }

    public void loadLoadout(String fileName) {
        if (fileName == null || fileName.equals("")) {
            Toast.makeText(getApplicationContext(), "Invalid Entry!", Toast.LENGTH_LONG).show();
            return;
        }
        fileName += ".json";
        FileHelper theFileHelper = new FileHelper(fileName);
        String jsonString = theFileHelper.ReadFile(getApplicationContext());
        if (jsonString != "" && jsonString != null) {
            GsonBuilder gsonBuilder  = new GsonBuilder();
            // Allowing the serialization of static fields

            gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
            Gson gson = gsonBuilder.create();
            //Gson gson = new Gson();
            simulation_manager = gson.fromJson(jsonString, Simulation_Manager.class);
            if (!hideToast) {
                Toast.makeText(getApplicationContext(), "Loaded Setup from " + fileName, Toast.LENGTH_LONG).show();
            }
            hideToast = false;
            renderUIFromManager();
            if (time != null) {
                time.cancel();
            }
            if (startItem != null) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void run() {
                        startItem.setTitle("Confirm Start");
                        ActionMenuItemView playImage = findViewById(R.id.playButton);
                        playImage.setIcon(getResources().getDrawable(R.drawable.play_button));
                    }
                });
            }
            ranAlready = false;
            simulationStarted = false;
            //simulation_manager.setEndTime(600);
        } else {
            Toast.makeText(getApplicationContext(), "Error Loading File " + fileName + "!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    //Method to create each log file
    public void createLogs() {
        //Equipment Log:
        simulation_manager.createLog(getApplicationContext(), "equipment", new EquipmentCSV().getCSVHeader(), simulation_manager.getEquipmentCSVList());
        //Station Log:
        simulation_manager.createLog(getApplicationContext(), "station", new StationCSV().getCSVHeader(), simulation_manager.getStationCSVList());
        //Scope Log:
        simulation_manager.createLog(getApplicationContext(), "scope", new ScopeLogCSV().getCSVHeader(), simulation_manager.getScopeLogCSVList());
    }

    public void renderUIFromManager() {
        updateActorUI();

        TextView textTime = findViewById(R.id.textViewTime);
        textTime.setText(Time.convertToString(simulation_manager.getCurrTime()));

        hall_monitor.setListFromBackend();
        hall_monitor.renderList();

        ArrayList<Client> waitingRoom = simulation_manager.getWaitingRoom();
        LinearLayout linearLayoutClient = findViewById(R.id.LinearLayoutClients);
        populateSection(linearLayoutClient, "Client", waitingRoom);

        ArrayList<ProcedureRoom> procedureRooms = simulation_manager.getProcedureRooms();
        LinearLayout linearLayoutProcedureRoom = findViewById(R.id.LinearLayoutRooms);
        populateSection(linearLayoutProcedureRoom, "Procedure Room", procedureRooms);

        ArrayList<Scope> scopes = simulation_manager.getFreeScopes();
        LinearLayout linearLayoutScope = findViewById(R.id.LinearLayoutScopes);
        populateSection(linearLayoutScope, "Scope", scopes);

        ArrayList<ManualCleaningStation> manualCleaningStations = simulation_manager.getManualCleaningStations();
        LinearLayout linearLayoutSinks = findViewById(R.id.LinearLayoutSinks);
        populateSection(linearLayoutSinks, "Sink", manualCleaningStations);
    }

    public <T> void populateSection(LinearLayout theLinearLayout, String tagType, ArrayList<T> list) {
        theLinearLayout.removeAllViews();
        for (int index = 0; index < list.size(); index++) {
            Object insert = list.get(index);

            ObjectView viewImage = new ObjectView(insert, getApplicationContext());
            viewImage.changeOrientation(LinearLayout.HORIZONTAL);
            viewImage.setOnClickListener(this);

            Tag tag = new Tag(index, tagType);

            viewImage.setTag(tag);

            theLinearLayout.addView(viewImage);
        }
    }

    static boolean isRunning = false;
    static boolean isPaused = false;
    static boolean ranAlready = false;
    static boolean hideToast = false;
    static boolean simulationStarted = false;
    Timer time = null;
    MenuItem startItem = null;

    public void startSimulation(final MenuItem item) {
        startItem = item;
        simulationStarted = true;
        simulation_manager.initLogs();
        if (ranAlready) {
            hideToast = true;
            loadLoadout("LastRun");
        }
        saveLoadout(null);
        ranAlready = true;
        simulation_manager.setCurrTime(simulation_manager.getStartTime());
        time = new Timer();
        time.scheduleAtFixedRate(new TimerTask() {
            @SuppressLint("RestrictedApi")
            @Override
            public void run() {
                if (!isRunning && !isPaused) {
                    isRunning = true;
                    simulation_manager.runTick();
                    simulation_manager.incrementCurrTime();

                    if (simulation_manager.getCurrTime() >= simulation_manager.getEndTime() && simulation_manager.isEverythingDone()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                simulationStarted = false;
                                item.setTitle("Confirm Start");
                                ActionMenuItemView playImage = findViewById(R.id.playButton);
                                playImage.setIcon(getResources().getDrawable(R.drawable.play_button));
                            }
                        });
                        createLogs();
                        this.cancel();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            renderUIFromManager();
                            isRunning = false;
                        }
                    });
                    //System.err.println("Loop " + (simulation_manager.getCurrTime() - 1));
                }
            }
        }, 0, 1000);
    }

    @Override
    public void submitFileName(String fileName) {
        saveLoadout(fileName);
        isPaused = false;
    }

    @Override
    public void loadFileName(String fileName) {
        loadLoadout(fileName);
        isPaused = false;
    }

    @Override
    public void cancelLoad() {
        isPaused = false;
    }

    @Override
    public void cancelSave() {
        isPaused = false;
    }

    @Override
    public void setStartEndTime(int start, int end) {
        if (end <= start) {
            Toast.makeText(getApplicationContext(), "Start time must be before end time!", Toast.LENGTH_LONG).show();
            return;
        }
        simulation_manager.setStartTime(start);
        simulation_manager.setCurrTime(start);
        simulation_manager.setEndTime(end);
        if (simulation_manager.removeClientsOutsideRange() > 0) {
            Toast.makeText(getApplicationContext(), "Patients outside of range have been deleted!", Toast.LENGTH_LONG).show();
        }
        renderUIFromManager();
    }

    @Override
    public void cancelTime() {

    }


    public void updateActorUI() {
        LinearLayout nurseLayout = findViewById(R.id.LinearLayoutNurses);
        nurseLayout.removeAllViews();

        ObjectView nurse = new ObjectView(new Nurse(), getApplicationContext());
        nurse.changeOrientation(ObjectView.HORIZONTAL);
        nurse.addLine("Nurses Available: " + simulation_manager.getFreeNurses() + "/" + simulation_manager.getNurseNum());
        Tag tagN =  new Tag(0, "Nurse");
        nurse.setTag(tagN);
        nurse.setOnClickListener(this);
        nurseLayout.addView(nurse);


        ObjectView technician = new ObjectView(new Technician(), getApplicationContext());
        technician.changeOrientation(ObjectView.HORIZONTAL);
        technician.addLine("Technicians Available: " + simulation_manager.getTechnicianFreeNum() + "/" + simulation_manager.getTechnicianNum());
        Tag tagT =  new Tag(0, "Technician");
        technician.setTag(tagT);
        technician.setOnClickListener(this);
        nurseLayout.addView(technician);
    }
}
