package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Doctor extends Element implements Serializable, ActorCSV.Actor {

    private State state;
    private int id;
    private int timeLeft;
    private ArrayList<Procedure> procedures;


    public Doctor(ArrayList<Procedure> procedures) {
        this.element = ELEMENT_DOCTOR;
        this.procedures = procedures;
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

    public void addProcedure(Procedure procedure) {
        if (procedures == null) {
            procedures = new ArrayList<Procedure>();
        }
        procedures.add(procedure);
    }

    public void setProcedures(ArrayList<Procedure> procedures) {
        if (procedures != null) {
            this.procedures = new ArrayList<>(procedures);
        } else {
            this.procedures = null;
        }
    }

    public void setState(@State.StateDef int state) {
        this.state = new State(state);
        Doctor_Manager.handleStateSwitch(this);
    }

    public void tick() {
        timeLeft--;
        if(timeLeft < 0){
            timeLeft = 0;
        }
        if (timeLeft <= 0) {
            if (state.equals(State.STATE_TRAVEL)) {
                setState(State.STATE_INROOM);
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

    public boolean validate() {
        return procedures!=null && procedures.size() > 0;
    }

    @Override
    public ActorCSV getActorCSV() {
        return new ActorCSV("Doctor", procedures.toString().replace("[","\"").replace("]","\""), id + "");
    }
}
