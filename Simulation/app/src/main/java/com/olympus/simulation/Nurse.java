package com.olympus.simulation;

import java.io.Serializable;

public class Nurse extends Element implements Serializable {

    private State state;

    private int timeLeft;


    public Nurse() {
        this.element = ELEMENT_NURSE;
        setState(State.STATE_WAIT);
        timeLeft = 0;
    }

    public int getState() {
        return state.state;
    }

    public void setState(@State.StateDef int state) {
        this.state = new State(state);
    }

    public void tick() {
        timeLeft--;
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

    public void startOperation() {
        this.timeLeft = 0;
        setState(State.STATE_OPERATION);
    }

    public void startPostProcedure(int postProcedureTime) {
        this.timeLeft = postProcedureTime;
        setState(State.STATE_DONE);
    }




}
