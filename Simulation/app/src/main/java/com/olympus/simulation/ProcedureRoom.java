package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProcedureRoom extends Element implements Serializable, StationCSV.Station {
    //TODO: IMPLEMENT DIFFERENT TRAVEL TIMES FOR SCOPES AND PATIENTS

    //whether the room is currently being used or not
    private boolean occupied;
    //whether the room is ready to serve the customer
    private boolean ready;
    //whether the room has all of its elements cleared out of it.
    private boolean clear;
    //how much time it takes to travel to this room from the client waiting room
    private int travelTimeClient;
    private int travelTimeDoctor;
    private int travelTimeNurse;
    private int travelTimeTechnician;
    //the time needed after an operation for the room to be available again
    private int cooldownTime;
    //time left in cooldown
    private int cooldownTimeLeft;

    private int id;

    private Client client;
    private ArrayList<Scope> scope;
    private ArrayList<Tower_Type> towerTypes;
    private  Nurse currentNurse;
    private Doctor currentDoctor;
    private Technician tech;

    public ProcedureRoom(int travelTimeClient, int travelTimeDoctor, int travelTimeNurse, int travelTimeTechnician, int cooldownTime) {
        this.element = Element.ELEMENT_PROCEDUREROOM;
        this.occupied = false;
        this.ready = false;
        this.travelTimeClient = travelTimeClient;
        this.travelTimeDoctor = travelTimeDoctor;
        this.travelTimeNurse = travelTimeNurse;
        this.travelTimeTechnician = travelTimeTechnician;
        this.cooldownTime = cooldownTime;
        this.cooldownTimeLeft = 0;
        this.client = null;
        this.scope = new ArrayList<>();
        this.towerTypes = new ArrayList<>();
        this.currentNurse = null;
        this.currentDoctor = null;
        this.clear = true;
    }
    public ProcedureRoom(ProcedureRoom procedureRoom) {
        this.element = Element.ELEMENT_PROCEDUREROOM;
        this.occupied = false;
        this.ready = false;
        this.travelTimeClient = procedureRoom.getTravelTimeClient();
        this.travelTimeDoctor = procedureRoom.getTravelTimeDoctor();
        this.travelTimeNurse = procedureRoom.getTravelTimeNurse();
        this.travelTimeTechnician = procedureRoom.getTravelTimeTechnician();
        this.cooldownTime = procedureRoom.getCooldownTime();
        this.cooldownTimeLeft = 0;
        this.client = null;
        this.scope = new ArrayList<>();
        this.towerTypes = new ArrayList<Tower_Type>(procedureRoom.getTowerTypes());
        this.currentNurse = null;
        this.currentDoctor = null;
        this.clear = true;
    }

    public void claimElements(ArrayList<Scope> list, Doctor doctor, Nurse nurse) {
        this.scope = list;
        for(Scope s : list) {
            s.claim(this, this.travelTimeTechnician);
        }
        this.currentDoctor = doctor;
        this.currentDoctor.setDestination(this);
        this.currentDoctor.startTravel(this.travelTimeDoctor);
        this.currentNurse = nurse;
        this.currentNurse.setDestination(this);
        this.currentNurse.startTravel(this.travelTimeNurse);
    }

    //cooldown time decreases with each tick of time
    public void tick() {
        if(this.tech == null) {
            Technician getTech = Technician_Manager.getTechnician();
            if (getTech == null) {
                return;
            } else {
                getTech.setDestination(this);
                getTech.startTravel(this.getTravelTimeTechnician());
                this.tech = getTech;
            }
        }
        if(this.tech.getState() == State.STATE_OPERATION) {
            cooldownTimeLeft--;
            if(cooldownTimeLeft <= 0){
                this.tech.startTravel(-1);
                this.tech = null;
            }
        }
        return;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        setOccupied(true);
        this.client = client;
    }

    public void removeElements(String currTime) {
        setReady(false);
        setClear(false);
        this.currentNurse.startPostProcedure(Nurse_Manager.getPostProcedureTime());
        this.currentNurse = null;
        int cooldown = 0;
        for(int i = 0; i < this.client.getProcedureList().size(); i++){
            cooldown += this.client.getProcedureList().get(i).getDoctorPostProcedureTime();
        }
        this.currentDoctor.startPostProcedure(cooldown);
        this.currentDoctor = null;

        tryClear(currTime);
    }

    public void tryClear(String currTime){
        for(int i = 0; i < this.scope.size(); i++){
            int temp = scope.get(i).getState();
            scope.get(i).setState(State_Scope.STATE_DIRTY);
            scope.get(i).state = temp;
            if(this.scope.get(i).getHolding() == null) {
                Technician tech = Technician_Manager.getTechnician();
                if (tech != null) {
                    tech.setDestination(this);
                    this.scope.get(i).setHolding(tech, this.travelTimeTechnician);
                }
            }
            if(this.scope.get(i).canTravel()) {
                this.scope.get(i).freeScope(currTime);
                this.scope.remove(i);
                i--;
            }
        }
        if(this.scope.size() == 0) {
            setClear(true);
        }
    }

    public void removeClient() {
        setOccupied(false);
        this.client = null;
        startCooldown();
    }

    //Starts the process of the cooldown by setting the timeLeft to the cooldown time
    public void startCooldown() {
        cooldownTimeLeft = cooldownTime;
    }

    //Returns true of the room is available for a client
    public boolean isAvailable() {
        return !isOccupied() && cooldownTimeLeft == 0;
    }

    //Returns true of the room is occupied by another client
    public boolean isOccupied() {
        return occupied;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    //Returns the amount of cooldown time left.
    public int getCooldownTimeLeft() {
        return cooldownTimeLeft;
    }

    //Sets the room to be either occupied or unoccupied based on the send in value
    //True = Occupied, False = Unoccupied
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    //Returns the travel time from the waiting room to the procedure room
    public int getTravelTimeClient() {
        return travelTimeClient;
    }

    //Sets the travel time to the waiting room
    public void setTravelTimeClient(int travelTime) {
        this.travelTimeClient = travelTime;
    }

    //Returns the travel time from the waiting room to the procedure room
    public int getTravelTimeDoctor() {
        return travelTimeDoctor;
    }

    //Sets the travel time to the waiting room
    public void setTravelTimeDoctor(int travelTime) {
        this.travelTimeDoctor = travelTime;
    }

    //Returns the travel time from the waiting room to the procedure room
    public int getTravelTimeNurse() {
        return travelTimeNurse;
    }

    //Sets the travel time to the waiting room
    public void setTravelTimeNurse(int travelTime) {
        this.travelTimeNurse = travelTime;
    }

    //Returns the travel time from the waiting room to the procedure room
    public int getTravelTimeTechnician() {
        return travelTimeTechnician;
    }

    //Sets the travel time to the waiting room
    public void setTravelTimeTechnician(int travelTime) {
        this.travelTimeTechnician = travelTime;
    }

    //Returns the cooldown time that the room must go through every time it completes.
    public int getCooldownTime() {
        return cooldownTime;
    }

    //Sets the cooldown time that the room must go through every time it completes.
    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public ArrayList<Scope> getScopeList() {
        return scope;
    }

    public void startOperating(){
        this.currentDoctor.setState(State.STATE_OPERATION);
        this.currentNurse.setState(State.STATE_OPERATION);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean validate() {
        return cooldownTime > 0 && travelTimeClient > 0 && travelTimeDoctor > 0 && travelTimeNurse > 0 && travelTimeTechnician > 0;
    }
    public void addTowerType(Tower_Type type){
        this.towerTypes.add(type);
    }

    public void removeTowerType(String name){
        for(int i = 0; i < this.towerTypes.size(); i++){
            if(this.towerTypes.get(i).getName() == name){
                this.towerTypes.remove(i);
                break;
            }
        }
    }

    public String[] getTowerTypeNames(){
        String[] types = new String[this.towerTypes.size()];
        for(int i = 0; i < this.towerTypes.size(); i++){
            types[i] = this.towerTypes.get(i).getName();
        }
        return types;
    }

    public ArrayList<Tower_Type> getTowerTypes() {
        return this.towerTypes;
    }

    //Returns true if the procedure room has the required tower types to process that patient, false
    //otherwise.
    public boolean checkCanProcess(Client c){
        ArrayList<Procedure> procList = c.getProcedureList();

        int counter = 0;
        for(int i = 0; i < procList.size(); i++){
            for(int j = 0; j < this.towerTypes.size(); j++){
                if(this.towerTypes.get(j).checkProcessProcedure(procList.get(i))){
                    counter++;
                    break;
                }
            }
        }

        if(counter == procList.size()){
            return true;
        }

        return false;
    }

    @Override
    public StationCSV getStationCSV() {
        return new StationCSV("Room", this.id + "", "Room " + this.id);
    }

    public Nurse getCurrentNurse() {
        return currentNurse;
    }

    public void setCurrentNurse(Nurse currentNurse) {
        this.currentNurse = currentNurse;
    }

    public Doctor getCurrentDoctor() {
        return currentDoctor;
    }

    public void setCurrentDoctor(Doctor currentDoctor) {
        this.currentDoctor = currentDoctor;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }
}
