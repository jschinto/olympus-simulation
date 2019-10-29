package com.olympus.simulation;

import java.util.ArrayList;

public class Reprocessor_Manager {

    private ArrayList<Reprocessor> reprocessors;

    public Reprocessor_Manager(){
        this.reprocessors = new ArrayList<>();
    }

    public void runTick(){
        for(int i = 0; i < this.reprocessors.size(); i++){
            this.reprocessors.get(i).tick();
        }
    }

    public ArrayList<Reprocessor> getReprocessors() {
        return reprocessors;
    }

    public void setReprocessors(ArrayList<Reprocessor> reprocessors) {
        this.reprocessors = reprocessors;
    }

    public void addReprocessor(Reprocessor reprocessor){
        this.reprocessors.add(reprocessor);
    }

    public void removeReprocessor(Reprocessor reprocessor){
        this.reprocessors.remove(reprocessor);
    }

    public int getNumReprocessors(){
        return this.reprocessors.size();
    }
}
