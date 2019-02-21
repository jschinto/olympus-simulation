package com.olympus.simulation;


/*
class to represent a Client in the simulation
 */
public class Client {

    //the procedure that a client is going to have done
    private Procedure procedure;
    //the procedure room that the client is in
    private ProcedureRoom procedureRoom;
    //the current status of the client in the simulation
    private State state;
    //how much time is left in the client's current task/state
    private int timeLeft;

    public Client(Procedure procedure) {
        this.procedure = procedure;
        setState(State.STATE_WAIT);
        procedureRoom = null;
        timeLeft = 0;
    }

    public void tick() {
        timeLeft--;
        //client is done with operation
        if (timeLeft <= 0 && state.equals(State.STATE_OPERATION)) {
            setState(State.STATE_DONE);
            procedureRoom.setOccupied(false);
            procedureRoom.startCooldown();

        }
        //client is done traveling to operation room
        if (timeLeft <= 0 && state.equals(State.STATE_TRAVEL)) {
            setState(State.STATE_OPERATION);
            beginProcedure();
        }
    }

    //start the operation, pick a random time between min and max procedure times
    //set appropriate state and time left to client
    public void beginProcedure() {
        //check procedure constraints
        int minTime = procedure.getMinTime();
        int maxTime = procedure.getMaxTime();
        timeLeft = (int) (minTime + Math.random()*(maxTime-minTime));
        setState(State.STATE_OPERATION);
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public ProcedureRoom getProcedureRoom() {
        return procedureRoom;
    }

    public void setProcedureRoom(ProcedureRoom procedureRoom) {
        this.procedureRoom = procedureRoom;
        procedureRoom.setOccupied(true);
        setState(State.STATE_TRAVEL);
        timeLeft = procedureRoom.getTravelTime();
    }

    public int getState() {
        return state.state;
    }

    public void setState(@State.StateDef int state) {
        this.state = new State(state);
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

}