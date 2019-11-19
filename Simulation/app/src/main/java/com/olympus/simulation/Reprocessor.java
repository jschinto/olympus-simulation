package com.olympus.simulation;

import java.util.ArrayList;

public class Reprocessor extends  Element{

    private Reprocessor_Type type;
    private ArrayList<Scope> holding;
    private int timeLeft;
    private int state;
    private Technician tech;
    private int startupDelay;
    private int id;

    public Reprocessor(Reprocessor_Type type){
        this.type = type;
        this.element = ELEMENT_REPROCESSOR;
        this.holding = new ArrayList<>();
        this.timeLeft = 0;
        this.state = State_Scope.STATE_FREE;
        this.startupDelay = -1;
    }

    public void tick(){
        this.timeLeft--;
        if(this.timeLeft < 0){
            this.timeLeft = 0;
        }

        if(this.timeLeft == 0){
            if(this.state == State_Scope.STATE_CLEANING){
                this.empty();
                this.setState(State_Scope.STATE_FREE);
            }
        }
    }

    public void start(){
        if(this.tech == null) {
            Technician getTech = Technician_Manager.getTechnician();
            if (getTech == null) {
                return;
            } else {
                getTech.setDestination(this);
                getTech.startTravel(5);
                this.tech = getTech;
            }
        }
        if(this.tech.getState() == State.STATE_OPERATION) {
            if(this.startupDelay == 0) {
                this.setState(State_Scope.STATE_CLEANING);
                this.setTimeLeft(this.type.getCycleTime());
                this.tech.startTravel(-1);
                this.tech = null;
                this.startupDelay = -1;
            } else if (this.startupDelay < 0) {
                this.startupDelay = this.type.getStartupDelay();
            } else {
                this.startupDelay--;
            }
        }
    }

    public boolean checkStart(){
        if(this.holding.size() == this.type.getNumScopes() || this.holding.size() > 0 && this.getTimeLeft() == 0){
            return true;
        }
        this.timeLeft--;

        return false;
    }

    public Reprocessor_Type getType() {
        return type;
    }

    public void setType(Reprocessor_Type type) {
        this.type = type;
    }

    public void setScopeList(ArrayList<Scope> list){
        this.holding = list;
    }

    public ArrayList<Scope> getScopeList(){
        return this.holding;
    }

    public boolean addScope(Scope scope){
        if(this.holding.size() == this.type.getNumScopes()){
            return false;
        }
        this.holding.add(scope);
        this.setTimeLeft(this.type.getWaitTime());
        scope.setRepro(this);
        return true;
    }

    public void empty(){
        for(int i = 0; i < this.holding.size(); i++){
            this.holding.get(i).finishReprocessing();
        }
        this.holding.clear();
    }

    public int getNumScopes(){
        return this.holding.size();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean validate() {
        return this.type != null;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
}
