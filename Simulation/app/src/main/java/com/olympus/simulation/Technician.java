package com.olympus.simulation;

import java.io.Serializable;

public class Technician extends Element implements Serializable {
    private State state;
    private int travel;


    public Technician(){
        this.element = ELEMENT_TECHNICIAN;
        this.state = new State(State.STATE_WAIT);
    }

    public void tick(){
        this.travel--;
        if(this.travel <= 0) {
            if(this.state.state == State.STATE_TRAVEL){
                setState(new State(State.STATE_OPERATION));
            }
        }
    }

    public int getState() {
        return state.state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public void startTravel(int travelTime){
        if(travelTime == -1){
            this.travel = 0;
            this.state = new State(State.STATE_WAIT);
        } else if (travelTime == 0) {
            this.travel = 0;
            this.state = new State(State.STATE_OPERATION);
        } else {
            this.travel = travelTime;
            this.state = new State(State.STATE_TRAVEL);
        }
    }

    public void setState(int state) {
        this.state = new State(state);
    }

    public int getTravel() {
        return travel;
    }

    public void setTravel(int travel) {
        this.travel = travel;
    }
}
