package com.olympus.simulation;

import java.util.ArrayList;

public class Simulation_Manager {
    //the ProcedureRoom and Client Managers needed to run the simulation
    private ProcedureRoom_Manager procedureRoomManager;
    private Client_Manager clientManager;
    private Procedure_Manager procedureManager;

    //the time the simulated hospital should open and close
    //Represented as a integer from 0 - some value
    private int startTime;
    private int endTime;
    //Time the system should wait between ticks.
    private int waitTime;
    //Current time of the simulation
    private int currTime;

    //Initializes the simulation using the startTime, endTime, and waitTime
    public Simulation_Manager(int startTime, int endTime, int waitTime) {
        procedureRoomManager = new ProcedureRoom_Manager();
        clientManager = new Client_Manager();
        procedureManager = new Procedure_Manager();
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
            nextClient.setProcedureRoom(openRoom);
            openRoom = procedureRoomManager.getProcedureRoom();
        }
        clientManager.runTick();
        procedureRoomManager.runTick();
        currTime++;

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
    public void editProcedure(ProcedureRoom room, int index){
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

    public int getLatestClientTime() {
        return clientManager.getLatestTime();
    }
}
