package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Doctor extends Element implements Serializable {

    private State state;

    private int timeLeft;

    private ArrayList<Procedure> procedures;


    public Doctor(ArrayList<Procedure> procedures) {
        this.element = ELEMENT_DOCTOR;
        this.procedures = procedures;
        setState(State.STATE_WAIT);
        timeLeft = 0;
    }

    public int getState() {
        return state.state;
    }

    public ArrayList<Procedure> getProcedures() {
        return procedures;
    }

    public String[] getProceduresNames(){
        String[] procs = new String[procedures.size()];
        for(int i = 0; i < procedures.size(); i++){
            procs[i] = procedures.get(i).getName();
        }
        return procs;
    }

    public void setProcedures(ArrayList<Procedure> procedures) {
        this.procedures = procedures;
    }

    public void setState(@State.StateDef int state) {
        this.state = new State(state);
    }

    public void tick() {
        timeLeft--;
        if(timeLeft < 0){
            timeLeft = 0;
        }
        System.out.println("Doctor " + timeLeft + " " + state);
        if (timeLeft <= 0) {
            if (state.equals(State.STATE_TRAVEL)) {
                setState(State.STATE_OPERATION);
            } else if (state.equals(State.STATE_DONE)) {
                setState(State.STATE_WAIT);
            }
        }
    }

    public void startTravel(int travelTime) {
        this.timeLeft = travelTime;
        setState(State.STATE_TRAVEL);
    }

    public void startPostProcedure(int postProcedureTime) {
        this.timeLeft = postProcedureTime;
        setState(State.STATE_DONE);
    }
}
