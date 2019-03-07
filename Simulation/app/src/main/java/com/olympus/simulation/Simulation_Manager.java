package com.olympus.simulation;

public class Simulation_Manager {
    //the ProcedureRoom and Client Managers needed to run the simulation
    private ProcedureRoom_Manager procedureRoomManager;
    private Client_Manager clientManager;

    //the time the simulated hospital should open
    private int startTime;
    private int endTime;
    //Time the system should wait between ticks.
    private int waitTime;
    //Current time of the simulation
    private int currTime;

    public Simulation_Manager(int startTime, int endTime, int waitTime) {
        procedureRoomManager = new ProcedureRoom_Manager();
        clientManager = new Client_Manager();
        this.startTime = startTime;
        this.endTime = endTime;
        this.waitTime = waitTime;
        this.currTime = this.startTime;
    }

    //run a tick of time
    public boolean runTick() {
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

    public void addClient(Client client) {
        clientManager.addClient(client);
    }

    public void addProcedureRoom(ProcedureRoom room) {
        procedureRoomManager.addProcedureRoom(room);
    }

    public int getClientNum() {
        return clientManager.getClientNum();
    }

    public int getProcedureRoomNum() {
        return procedureRoomManager.getProcedureRoomNum();
    }

    public Client getClient(int index) {
        return clientManager.getClientByIndex(index);
    }

    public ProcedureRoom getProcedureRoom(int index){
        return procedureRoomManager.getProcedureRoomByIndex(index);
    }

    public void editClient(Client client, int index) {
        clientManager.setClient(client, index);
    }

    public void editProcedure(ProcedureRoom room, int index){
        procedureRoomManager.setProcedureRoom(room, index);
    }

    public void deleteClient(int index) {
        clientManager.deleteClient(index);
    }

    public void deleteProcedureRoom(int index) {
        procedureRoomManager.deleteProcedureRoom(index);
    }

}
