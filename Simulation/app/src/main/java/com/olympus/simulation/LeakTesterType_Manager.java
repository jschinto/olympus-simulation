package com.olympus.simulation;

import java.util.ArrayList;

public class LeakTesterType_Manager {
    ArrayList<LeakTester_Type> types;

    public LeakTesterType_Manager(){
        types = new ArrayList<>();
    }

    public void addLeakTesterType(LeakTester_Type type){
        this.types.add(type);
    }

    public void removeLeakTesterType(LeakTester_Type type){
        this.types.remove(type);
    }

    public ArrayList<LeakTester_Type> getLeakTesterTypes(){
        return this.types;
    }

    public String[] getLeakTesterTypeNames(){
        String[] names = new String[this.types.size()];
        for(int i = 0; i < this.types.size(); i++){
            names[i] = this.types.get(i).getName();
        }

        return names;
    }

    public LeakTester_Type getLeakTesterTypeByName(String name){
        for(int i = 0; i < this.types.size(); i++){
            if(this.types.get(i).getName().equals(name)){
                return this.types.get(i);
            }
        }

        return null;
    }
}
