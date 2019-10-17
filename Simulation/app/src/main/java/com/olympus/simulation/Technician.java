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

    public int getTravel() {
        return travel;
    }

    public void setTravel(int travel) {
        this.travel = travel;
    }
}
