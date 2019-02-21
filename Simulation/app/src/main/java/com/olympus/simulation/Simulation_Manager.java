package com.olympus.simulation;

public class Simulation_Manager {
    //the ProcedureRoom and Client Managers needed to run the simulation
    private ProcedureRoom_Manager procedureRoomManager;
    private Client_Manager clientManager;

    //the time the simulated hospital should open
    private int startTime;
    private int endTime;
    private int waitTime;

    public Simulation_Manager(int startTime, int endTime, int waitTime) {
        procedureRoomManager = new ProcedureRoom_Manager();
        clientManager = new Client_Manager();
        this.startTime = startTime;
        this.endTime = endTime;
        this.waitTime = waitTime;
    }

    //run a tick of time
    //TODO: making a client leave the procedure room after operation is done, setting up times for client and procedure room
    public boolean runTick() {
        ProcedureRoom openRoom = procedureRoomManager.getProcedureRoom();
        while (!openRoom.equals(null)) {
            Client nextClient = clientManager.getNextClient();
            if (nextClient == null) {
                break;
            }
            nextClient.setProcedureRoom(openRoom);
            nextClient.beginProcedure();
            openRoom = procedureRoomManager.getProcedureRoom();
        }
        clientManager.runTick();
        procedureRoomManager.runTick();
        return false;
    }


}
