package com.olympus.simulation;

import java.util.ArrayList;

public class Reprocessor_Manager {

    private ArrayList<Reprocessor> reprocessors;

    public Reprocessor_Manager(){
        this.reprocessors = new ArrayList<>();
    }

    public void runTick(){
        for(int i = 0; i < this.reprocessors.size(); i++){
            if(this.reprocessors.get(i).getState() == State_Scope.STATE_CLEANING) {
                this.reprocessors.get(i).tick();
            }
            else {
                if(this.reprocessors.get(i).checkStart()){
                    this.reprocessors.get(i).start();
                }
            }
        }
    }

    public ArrayList<Reprocessor> getReprocessors() {
        return reprocessors;
    }

    public void setReprocessors(ArrayList<Reprocessor> reprocessors) {
        this.reprocessors = reprocessors;
    }

    public Reprocessor getReprocessorByIndex(int i) {
        return reprocessors.get(i);
    }

    public void addReprocessor(Reprocessor reprocessor){
        this.reprocessors.add(reprocessor);
    }

    public void removeReprocessor(Reprocessor reprocessor){
        this.reprocessors.remove(reprocessor);
    }

    public void removeReprocessorByType(String type) {
        for (int i=0; i < reprocessors.size(); i++) {
            if (reprocessors.get(i).getType().getName().equals(type)) {
                reprocessors.remove(i);
                return;
            }
        }
    }

    public int getNumReprocessors(){
        return this.reprocessors.size();
    }

    public Reprocessor getFreeReprocessor(){
        this.sortReprocessors();
        for(int i = 0; i < this.reprocessors.size(); i++){
            if(this.reprocessors.get(i).getState() == State_Scope.STATE_FREE && this.reprocessors.get(i).getNumScopes() < this.reprocessors.get(i).getType().getNumScopes()){
                return this.reprocessors.get(i);
            }
        }
        return null;
    }

    public void sortReprocessors(){
        for(int i = 0; i < this.reprocessors.size(); i++){
            for(int j = i; j < this.reprocessors.size(); j++){
                if(this.reprocessors.get(i).getNumScopes() < this.reprocessors.get(j).getNumScopes()){
                    Reprocessor temp = this.reprocessors.get(i);
                    this.reprocessors.set(i, this.reprocessors.get(j));
                    this.reprocessors.set(j, temp);
                }
            }
        }
    }

    public void setIds() {
        for (int i=0; i < reprocessors.size(); i++) {
            reprocessors.get(i).setId(i+1);
        }
    }
}
