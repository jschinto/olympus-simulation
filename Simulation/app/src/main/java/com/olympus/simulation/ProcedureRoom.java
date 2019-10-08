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
    //how much time it takes to travel to this room from the client waiting room
    private int travelTime;
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

    public ProcedureRoom(int travelTime, int cooldownTime) {
        this.element = Element.ELEMENT_PROCEDUREROOM;
        this.occupied = false;
        this.ready = false;
        this.travelTime = travelTime;
        this.cooldownTime = cooldownTime;
        this.cooldownTimeLeft = 0;
        this.client = null;
        this.scope = new ArrayList<>();
        this.towerTypes = new ArrayList<>();
        this.currentNurse = null;
        this.currentDoctor = null;
    }

    public void claimElements(ArrayList<Scope> list, Doctor doctor, Nurse nurse) {
        this.scope = list;
        for(Scope s : list) {
            s.claim(this, this.travelTime);
        }
        this.currentDoctor = doctor;
        this.currentDoctor.startTravel(this.travelTime);
        this.currentNurse = nurse;
        this.currentNurse.startTravel(this.travelTime);
    }

    //cooldown time decreases with each tick of time
    public void tick() {
        cooldownTimeLeft--;
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
        for(Scope s : this.scope){
            s.freeScope(currTime);
        }
        this.scope.clear();
        this.currentNurse.startPostProcedure(Nurse_Manager.getPostProcedureTime());
        this.currentNurse = null;
        this.currentDoctor.startPostProcedure(Doctor_Manager.getPostProcedureTime());
        this.currentDoctor = null;
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
    public int getTravelTime() {
        return travelTime;
    }

    //Sets the travel time to the waiting room
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean validate() {
        return cooldownTime > 0 && travelTime > 0;
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
        return new StationCSV("Room", "Procedure Room " + this.id);
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
}
