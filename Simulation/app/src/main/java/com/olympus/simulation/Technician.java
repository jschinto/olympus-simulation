package com.olympus.simulation;

import java.io.Serializable;

public class Technician extends Element implements Serializable, ActorCSV.Actor {
    private State state;
    private int id;
    private int travel;

    public Technician(){
        this.element = ELEMENT_TECHNICIAN;
        this.state = new State(State.STATE_WAIT);
        this.id = 0;
        setDestination(null);
    }

    public void tick(){
        this.travel--;
        if(this.travel <= 0) {
            if(this.state.state == State.STATE_TRAVEL){
                setState(new State(State.STATE_OPERATION));
            }
        }
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

    public void setState(State state) {
        this.state = state;
        Technician_Manager.handleStateSwitch(this);
    }


    public void startTravel(int travelTime){
        if(travelTime == -1){
            this.travel = 0;
            setState(new State(State.STATE_WAIT));
        } else if (travelTime == 0) {
            this.travel = 0;
            setState(new State(State.STATE_OPERATION));
        } else {
            this.travel = travelTime;
            setState(new State(State.STATE_TRAVEL));
        }

    }

    public void setState(int state) {
        setState(new State(state));
    }

    public int getTravel() {
        return travel;
    }

    public void setTravel(int travel) {
        this.travel = travel;
    }

    @Override
    public ActorCSV getActorCSV() {
        return new ActorCSV("Technician", "", id + "");
    }
}
