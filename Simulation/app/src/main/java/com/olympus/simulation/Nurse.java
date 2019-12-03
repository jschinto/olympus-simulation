package com.olympus.simulation;

import java.io.Serializable;
import java.util.ArrayList;

public class Nurse extends Element implements Serializable, ActorCSV.Actor {

    private State state;
    private int id;
    private ArrayList<Procedure> currProcedure;

    private int timeLeft;


    public Nurse() {
        this.element = ELEMENT_NURSE;
        this.currProcedure = null;
        setState(State.STATE_WAIT);
        timeLeft = 0;
        id = 0;
        setDestination(null);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public int getState() {
        return state.state;
    }

    public void setState(@State.StateDef int state) {
        this.state = new State(state);
        Nurse_Manager.handleStateSwitch(this);
    }

    public void tick() {
        timeLeft--;
        if(timeLeft < 0){
            timeLeft = 0;
        }
        //System.out.println("Nurse " + timeLeft + " " + state);
        if (timeLeft <= 0) {
            if (state.equals(State.STATE_TRAVEL)) {
                setState(State.STATE_INROOM);
            } else if (state.equals(State.STATE_DONE)) {
                this.setCurrProcedure(null);
                this.setDestination(null);
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


    @Override
    public ActorCSV getActorCSV() {
        return new ActorCSV("Nurse", "", id + "");
    }

    public ArrayList<Procedure> getCurrProcedure() {
        return currProcedure;
    }

    public void setCurrProcedure(ArrayList<Procedure> currProcedure) {
        this.currProcedure = currProcedure;
    }
}
