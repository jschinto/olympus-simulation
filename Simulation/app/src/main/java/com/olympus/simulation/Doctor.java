package com.olympus.simulation;

import java.io.Serializable;

public class Doctor extends Element implements Serializable {

    private State state;

    private int timeLeft;


    public Doctor() {
        this.element = ELEMENT_DOCTOR;
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