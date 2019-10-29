package com.olympus.simulation;

import java.util.ArrayList;

public class ReprocessorType_Manager {
    ArrayList<Reprocessor_Type> types;

    public ReprocessorType_Manager(){
        this.types = new ArrayList<>();
    }

    public ArrayList<Reprocessor_Type> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<Reprocessor_Type> types) {
        this.types = types;
    }

    public void addType(Reprocessor_Type type){
        this.types.add(type);
    }

    public void removeType(Reprocessor_Type type){
        this.types.remove(type);
    }

    public int getNumTypes(){
        return this.types.size();
    }

    public String[] getTypeNames(){
        String[] names = new String[this.types.size()];
        for(int i = 0; i < this.types.size(); i++){
            names[i] = this.types.get(i).getName();
        }
        return names;
    }

    public Reprocessor_Type getTypeByName(String name){
        for(int i = 0; i < this.types.size(); i++){
            if(this.types.get(i).getName() == name){
                return this.types.get(i);
            }
        }
        return null;
    }

    public void removeTypeByName(String name){
        for(int i = 0; i < this.types.size(); i++){
            if(this.types.get(i).getName() == name){
                this.types.remove(i);
            }
        }
    }
}
