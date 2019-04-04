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
        //TODO: Currently does not check if a room is ready to service the client.
        ProcedureRoom openRoom = procedureRoomManager.getProcedureRoom();
        while (openRoom != null) {
            Client nextClient = clientManager.getNextClient(currTime);
            if (nextClient == null) {
                break;
            }

            Scope freeScope = scopeManager.getAvaliableScope(nextClient.getProcedure());
            if(freeScope == null) {
                break;
            }

            clientManager.addToOperating(nextClient);
            nextClient.setProcedureRoom(openRoom);
            openRoom.claimScope(freeScope);
            openRoom = procedureRoomManager.getProcedureRoom();
            clientManager.sortQueue();
        }
        clientManager.runTick();
        scopeManager.runTick();
        procedureRoomManager.runTick();

        clientManager.sortQueue();
        return false;
    }

    //Adds a client to the client manager
    public void addClient(Client client) {
        clientManager.addClient(client);
    }

    //Adds a procedure room to the procedure room manager
    public void addProcedureRoom(ProcedureRoom room) {
        procedureRoomManager.addProcedureRoom(room);
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

    //Returns a client based on the given index. Associated with their position in the queue.
    public Client getClient(int index) {
        return clientManager.getClientByIndex(index);
    }

    //Returns the procedure room based on the given index. Associated with their position in the list.
    public ProcedureRoom getProcedureRoom(int index){
        return procedureRoomManager.getProcedureRoomByIndex(index);
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

    //Updates the client at the position of the given index with the given Client
    public void editClient(Client client, int index) {
        clientManager.setClient(client, index);
    }

    //Updates the procedure room at the position of the given index with the given Procedure Room
    public void editProcedureRoom(ProcedureRoom room, int index){
        procedureRoomManager.setProcedureRoom(room, index);
    }

    //Removes the client at the given index.
    public void deleteClient(int index) {
        clientManager.deleteClient(index);
    }

    //Removes the procedure room at the given index.
    public void deleteProcedureRoom(int index) {
        procedureRoomManager.deleteProcedureRoom(index);
    }

    public void deleteProcedure(String name) {
        procedureManager.deleteProcedure(name);
    }

    public int getLatestClientTime() {
        return clientManager.getLatestTime();
    }
    public void addScopeType(Scope_Type t) {
        scopeManager.addScopeType(t);
    }

    public void removeScopeType(Scope_Type t) {
        scopeManager.removeScopeType(t);
    }

    public ArrayList<Scope_Type> getScopeTypes(Scope_Type t) {
        scopeManager.getScopeTypes();
    }

    public void addScope(Scope s) {
        scopeManager.addScope(s);
    }

    public void removeScope(Scope s) {
        scopeManager.removeScope(s);
    }
}
