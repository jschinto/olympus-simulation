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

    public Client(Procedure procedure, State state) {
        this.procedure = procedure;
        this.state = state;
        procedureRoom = null;
        timeLeft = 0;

    }

    public void tick() {
        timeLeft--;
    }

    //start the operation, pick a random time between min and max procedure times
    //set appropriate state and time left to client
    public void beginProcedure() {
        int minTime = procedure.getMinTime();
        int maxTime = procedure.getMaxTime();
        timeLeft = (int) (maxTime + Math.random()*(maxTime-minTime));
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
    }

    public State getState() {
        return state;
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