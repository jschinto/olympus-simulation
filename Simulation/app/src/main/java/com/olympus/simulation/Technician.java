package com.olympus.simulation;

import java.io.Serializable;

public class Technician extends Element implements Serializable {
    private State state;
    private int timeLeft;

    public Technician(){
        this.element = ELEMENT_TECHNICIAN;
        this.state = new State(State.STATE_WAIT);
        this.timeLeft = 0;
    }

    public void tick(){
        this.timeLeft--;
        if(this.timeLeft < 0){
            this.timeLeft = 0;
        }
        if(this.timeLeft == 0){
            if(this.state.equals(State.STATE_TRAVEL)){
                this.state = new State(State.STATE_OPERATION);
            }
        }
    }
}
