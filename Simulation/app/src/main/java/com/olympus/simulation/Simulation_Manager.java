package com.olympus.simulation;

import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Simulation_Manager {
    //the ProcedureRoom and Client Managers needed to run the simulation
    private ProcedureRoom_Manager procedureRoomManager;
    private Client_Manager clientManager;
    private Procedure_Manager procedureManager;
    private  Scope_Manager scopeManager;
    private ScopeType_Manager scopeTypeManager;
    private TowerType_Manager towerTypeManager;
    private Nurse_Manager nursemanager;
    private Doctor_Manager doctormanager;
    private LeakTesterType_Manager leakTesterTypeManager;
    private ManualCleaningStation_Manager manualCleaningStationManager;

    //the time the simulated hospital should open and close
    //Represented as a integer from 0 - some value
    private int startTime;

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    private int endTime;
    //Time the system should wait between ticks.
    private int waitTime;

    public int getCurrTime() {
        return currTime;
    }

    public void setCurrTime(int currTime) {
        this.currTime = currTime;
    }

    public void incrementCurrTime() {
        this.currTime++;
    }

    //Current time of the simulation
    private int currTime;

    //Initializes the simulation using the startTime, endTime, and waitTime
    public Simulation_Manager(int startTime, int endTime, int waitTime) {
        procedureRoomManager = new ProcedureRoom_Manager();
        clientManager = new Client_Manager();
        procedureManager = new Procedure_Manager();
        scopeManager = new Scope_Manager();
        scopeTypeManager = new ScopeType_Manager();
        towerTypeManager = new TowerType_Manager();
        leakTesterTypeManager = new LeakTesterType_Manager();
        nursemanager = new Nurse_Manager();
        doctormanager = new Doctor_Manager();
        manualCleaningStationManager = new ManualCleaningStation_Manager();
        this.startTime = startTime;
        this.endTime = endTime;
        this.waitTime = waitTime;
        this.currTime = this.startTime;
    }

    /*
       Runs a tick of time
       Assigns a client to a procedure room if one is found to be ready
       Runs a tick for client and procedure rooms so they can process each individual client or room.
     */
    public boolean runTick() {
        //Grabs an open procedure room and attempts to assign a client to it. Continues to match
        //Clients to procedure rooms until a pair cannot be made.

        clientManager.runTick();
        scopeManager.runTick();
        procedureRoomManager.runTick();
        nursemanager.runTick();
        doctormanager.runTick();

        int patientOffset = 0;
        int roomOffset = 0;
        ProcedureRoom openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
        System.out.println("RUNNING");
        while (openRoom != null) {
            Client nextClient = clientManager.getNextClient(currTime, patientOffset);
            if (nextClient == null) {
                break;
            }

            Nurse freeNurse = nursemanager.getNurse();
            if(freeNurse == null){
                break;
            }

            Doctor freeDoctor = doctormanager.getDoctor();
            if(freeDoctor == null){
                break;
            }

            if(!openRoom.checkCanProcess(nextClient)){
                System.out.println("CANT CHECK");
                roomOffset++;
                openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
                if(openRoom == null && clientManager.getNextClient(currTime, patientOffset + 1) == null){
                    System.out.println("ERROR: Breaking");
                    break;
                }
                else if(clientManager.getNextClient(currTime, patientOffset + 1) == null){
                    roomOffset = 0;
                    openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
                    patientOffset++;
                }

                continue;
            }

            ArrayList<Scope> scopeList = new ArrayList<Scope>();
            for(int i = 0; i < nextClient.getProcedureList().size(); i++) {
                Scope freeScope = scopeManager.getAvaliableScope(nextClient.getProcedureList().get(i));
                if (freeScope == null) {
                    for(int j = 0; j < scopeList.size(); j++){
                        scopeList.get(i).setTempGrab(false);
                    }
                    break;
                }
                if(!scopeList.contains(freeScope)) {
                    scopeList.add(freeScope);
                    freeScope.setTempGrab(true);
                }
            }

            if(scopeList.size() == nextClient.getProcedureList().size()) {
                roomOffset = 0;
                clientManager.addToOperating(nextClient);
                nextClient.setProcedureRoom(openRoom);
                openRoom.claimElements(scopeList, freeDoctor, freeNurse);
                openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
                clientManager.sortQueue();
            }
            else
            {
                roomOffset = 0;
                patientOffset++;
            }
        }
        scopeManager.sortList();
        clientManager.sortQueue();
        return false;
    }

    //Adds a client to the client manager
    public void addClient(Client client) {
        clientManager.addClient(client);
        clientManager.setIDs();
    }

    //Adds a procedure room to the procedure room manager
    public void addProcedureRoom(ProcedureRoom room) {
        procedureRoomManager.addProcedureRoom(room);
        procedureRoomManager.setIDs();
    }

    //Adds a procedure to the list of procedures
    public void addProcedure(Procedure procedure) {
        procedureManager.addProcedure(procedure);
    }

    //Returns the number of clients in the waiting room
    public int getClientNum() {
        return clientManager.getClientNum();
    }

    //Returns the number of procedure rooms in the simulation
    public int getProcedureRoomNum() {
        return procedureRoomManager.getProcedureRoomNum();
    }

    public int getProcedureNum() {
        return procedureManager.getProcedureNum();
    }

    //Returns a client based on the given index. Associated with their position in the queue.
    public Client getClient(int index) {
        return clientManager.getClientByIndex(index);
    }

    public ArrayList<Client> getWaitingRoom() {
        ArrayList<Client> waitingRoom = new ArrayList<>();
        for (Client c:clientManager.getQueue()) {
            if(c.getState() == State.STATE_WAIT || c.getState() == State.STATE_DONE) {
                waitingRoom.add(c);
            }
        }
        return waitingRoom;
    }

    public ArrayList<Object> getHallway() {
        ArrayList<Object> hallway = new ArrayList<>();
        for (Client c:clientManager.getQueue()) {
            if(c.getState() == State.STATE_TRAVEL) {
                hallway.add(c);
            }
        }
        for(Scope s:scopeManager.getScopes()) {
            if(s.getState() == State_Scope.STATE_TRAVEL) {
                hallway.add(s);
            }
        }
        return hallway;
    }

    //Returns the procedure room based on the given index. Associated with their position in the list.
    public ProcedureRoom getProcedureRoom(int index){
        return procedureRoomManager.getProcedureRoomByIndex(index);
    }

    public ArrayList<ProcedureRoom> getProcedureRooms() {
        return procedureRoomManager.getProcedureRooms();
    }

    //Returns a list of all the available procedures.
    public String[] getProcedureNames() {
        ArrayList<String> procedureNames = procedureManager.getProcedureNames();
        String[] procedureNamesArray = new String[procedureNames.size()];
        return procedureNames.toArray(procedureNamesArray);
    }

    public Procedure getProcedureByName(String name){
        return procedureManager.getProcedureByName(name);
    }

    public Procedure getProcedure(int index) {
        return procedureManager.getProcedure(index);
    }

    //Updates the client at the position of the given index with the given Client
    public void editClient(Client client, int index) {
        clientManager.setClient(client, index);
        clientManager.setIDs();
    }

    //Updates the procedure room at the position of the given index with the given Procedure Room
    public void editProcedureRoom(ProcedureRoom room, int index){
        procedureRoomManager.setProcedureRoom(room, index);
        procedureRoomManager.setIDs();
    }

    //Removes the client at the given index.
    public void deleteClient(int index) {
        clientManager.deleteClient(index);
        clientManager.setIDs();
    }

    //Removes the procedure room at the given index.
    public void deleteProcedureRoom(int index) {
        procedureRoomManager.deleteProcedureRoom(index);
        procedureRoomManager.setIDs();
    }

    public void deleteProcedure(String name) {
        procedureManager.deleteProcedure(name);
    }

    public int getLatestClientTime() {
        return clientManager.getLatestTime();
    }
    public void addScopeType(Scope_Type t) {
        scopeTypeManager.addScopeType(t);
    }

    public void removeScopeType(String t) {
        scopeTypeManager.deleteScopeType(t);
    }

    public String[] getScopeTypeNames() {
        ArrayList<String> scopeTypeNames = scopeTypeManager.getScopeTypeNames();
        String[] scopeTypeNamesArray = new String[scopeTypeNames.size()];
        return scopeTypeNames.toArray(scopeTypeNamesArray);
    }

    public Scope_Type getScopeTypeByName(String name) {
        return scopeTypeManager.getScopeTypeByName(name);
    }

    public void addScope(Scope s) {
        scopeManager.addScope(s);
        scopeManager.setIDs();
    }

    public void removeScope(Scope s) {
        scopeManager.removeScope(s);
        scopeManager.setIDs();
    }

    public void removeScope(int i) {
        scopeManager.removeScopeByIndex(i);
        scopeManager.setIDs();
    }

    public int getScopeNum() {return scopeManager.getScopeNum();}

    public Scope getScopeByIndex(int index) {return scopeManager.getScopeByIndex(index);}

    public ArrayList<Scope> getFreeScopes() {
        ArrayList<Scope> scopes = new ArrayList<>();
        for(Scope s:scopeManager.getScopes()) {
            if(s.getState() == State_Scope.STATE_FREE || s.getState() == State_Scope.STATE_DIRTY) {
                scopes.add(s);
            }
        }
        return scopes;
    }

    public void setIDs() {
        clientManager.setIDs();
        procedureRoomManager.setIDs();
        scopeManager.setIDs();
    }

    public void addTowerType(Tower_Type type){
        towerTypeManager.addTowerType(type);
    }

    public String[] getTowerTypeNames(){
        ArrayList<String> list = towerTypeManager.getTowerTypeNames();
        String[] towerTypeNamesArray = new String[list.size()];
        return list.toArray(towerTypeNamesArray);
    }

    public Tower_Type getTowerTypeByName(String name){
        return towerTypeManager.getTowerTypeByName(name);
    }

    public int getTowerTypeNum() {
        return towerTypeManager.getTowerTypeNum();
    }

    public void removeTowerTypeByName(String name) {
        towerTypeManager.removeTowerType(name);
    }

    public String[] getLeakTesterTypeNames(){
        return leakTesterTypeManager.getLeakTesterTypeNames();
    }

    public void addLeakTester(LeakTester_Type type){
        leakTesterTypeManager.addLeakTesterType(type);
    }

    public int getLeakTesterTypeNum(){
        return leakTesterTypeManager.getLeakTesterTypeNames().length;
    }

    public LeakTester_Type getLeakTesterByName(String name){
        return leakTesterTypeManager.getLeakTesterTypeByName(name);
    }

    public void removeLeakTesterType(LeakTester_Type type){
        leakTesterTypeManager.removeLeakTesterType(type);
    }

    public ArrayList<ManualCleaningStation> getManualCleaningStations(){
        return manualCleaningStationManager.getManualCleaningStations();
    }

    public ManualCleaningStation getManualCleaningStationByIndex(int index){
        return manualCleaningStationManager.getManualCleaningStationByIndex(index);
    }

    public int getManualCleaningStationNum(){
        return manualCleaningStationManager.getManualCleaningStationNum();
    }

    //TODO: maybe show toast for patients deleted
    public int removeClientsOutsideRange() {
        return clientManager.removeClientsOutsideRange(startTime, endTime);
    }

    public int getNurseNum() {
        return nursemanager.getNurseNum();
    }
    public int getNursePostProcedureTime() {
        return nursemanager.getPostProcedureTime();
    }
    public void setNurseNum(int num) {
        nursemanager.setNurseNum(num);
    }
    public void setNursePostProcedureTime(int time) {
        nursemanager.setPostProcedureTime(time);
    }

    public int getDoctorNum() {
        return doctormanager.getDoctorNum();
    }
    public int getDoctorPostProcedureTime() {
        return doctormanager.getPostProcedureTime();
    }
    public void setDoctorNum(int num) {
        doctormanager.setDoctorNum(num);
    }
    public void setDoctorPostProcedureTime(int time) {
        doctormanager.setPostProcedureTime(time);
    }
}
