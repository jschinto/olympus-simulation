package com.olympus.simulation;


import java.io.Serializable;
import java.util.ArrayList;

/*
class to represent a Client in the simulation
 */
public class Client extends Element implements Comparable<Client>, Serializable {

    //the procedures that a client is going to have done
    private ArrayList<Procedure> procedures;
    //the procedure room that the client is in
    private ProcedureRoom procedureRoom;
    //the current status of the client in the simulation
    private State state;
    //how much time is left in the client's current task/state
    private int timeLeft;
    //when the client enters the waiting room
    private int arrivalTime;

    private boolean uiUpdated;

    private int id;

    public Client(ArrayList<Procedure> procedures, int arrivalTime) {
        this.element = ELEMENT_CLIENT;
        this.procedures = procedures;
        setState(State.STATE_WAIT);
        procedureRoom = null;
        timeLeft = 0;
        this.arrivalTime = arrivalTime;
        this.uiUpdated = false;
    }

    public Client(Client client) {
        this.procedures = client.getProcedureList();
        setState(client.getState());
        procedureRoom = client.getProcedureRoom();
        timeLeft = client.getTimeLeft();
        arrivalTime = client.getArrivalTime();
        this.uiUpdated = false;
    }

    public void setuiUpdated(boolean bool) {
        this.uiUpdated = bool;
    }

    public boolean getuiUpdated() {
        return this.uiUpdated;
    }

    //alters appropriate variables based on passage of time(tick)
    public void tick(String currTime) {
        this.timeLeft--;
        if(this.timeLeft < 0){
            this.timeLeft = 0;
        }

        System.out.println("Client " + this.timeLeft + " " + this.state + " " + this.procedureRoom.isReady());
        //client is done with operation
        if (this.timeLeft <= 0 && this.state.equals(State.STATE_OPERATION)) {
            setState(State.STATE_DONE);
            this.procedureRoom.removeElements(currTime);
            this.procedureRoom.removeClient();
            this.procedureRoom = null;
        }
        //client is done traveling to operation room
        if (this.timeLeft <= 0 && this.state.equals(State.STATE_TRAVEL)) {
            setState(State.STATE_INROOM);
        }

        if(this.state.equals(State.STATE_INROOM) && this.procedureRoom.isReady()) {
            setState(State.STATE_OPERATION);
            beginProcedure();
        }
    }

    //start the operation, pick a random time between min and max procedure times
    //set appropriate state and time left to client
    public void beginProcedure() {
        //check procedure constraints
        this.timeLeft = 0;
        this.procedureRoom.startOperating();
        for (int i = 0; i < this.procedures.size(); i++) {
            this.timeLeft += this.procedures.get(i).getTime();
        }
    }

    public ArrayList<Procedure> getProcedureList() {
        return procedures;
    }

    public void addProcedure(Procedure procedure) {
        this.procedures.add(procedure);
    }

    public void setProcedureList(ArrayList<Procedure> procedureList) {
        this.procedures = procedureList;
    }

    public void removeProcedure(Procedure procedure)  {
        this.procedures.remove(procedure);
    }

    public ProcedureRoom getProcedureRoom() {
        return procedureRoom;
    }

    //assigns client to given procedure room
    public void setProcedureRoom(ProcedureRoom procedureRoom) {
        this.procedureRoom = procedureRoom;
        procedureRoom.setClient(this);
        setState(State.STATE_TRAVEL);
        this.setTimeLeft(procedureRoom.getTravelTimeClient());
    }

    public int getState() {
        return state.state;
    }

    public void setState(@State.StateDef int state) {
        this.state = new State(state);
        setuiUpdated(false);
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(int arrivalTime) { this.arrivalTime = arrivalTime; }

    //compareTo function to help in organizing queues
    @Override
    public int compareTo(Client o) {
        if(this.getState() != o.getState())
        {
            return this.getState() - o.getState();
        }
        else if(this.getProcedureRoom() != null && o.getProcedureRoom() == null) {
            return -1;
        }
        else if(this.getProcedureRoom() == null && o.getProcedureRoom() != null) {
            return 1;
        }
        return this.arrivalTime - o.getArrivalTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}