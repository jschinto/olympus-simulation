package com.olympus.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TowerType_Manager {
    ArrayList<Tower_Type> typeList;

    public TowerType_Manager(){
        this.typeList = new ArrayList<>();
    }

    //Returns the full list of tower types
    public ArrayList<Tower_Type> getTowerTypes(){
        return this.typeList;
    }

    //Returns a specific tower type based on the name
    public Tower_Type getTowerTypeByName(String name){
        for(int i = 0; i < this.typeList.size(); i++){
            if(this.typeList.get(i).getName().equals(name)){
                return this.typeList.get(i);
            }
        }
        return null;
    }

    //Adds the tower type to the list of types.
    public void addTowerType(Tower_Type type){
        this.typeList.add(type);
    }

    //Removes the tower type from the list of types by name.
    public void removeTowerType(String typeName){
        for(int i = 0; i < this.typeList.size(); i++){
            if(this.typeList.get(i).getName() == typeName){
                this.typeList.remove(i);
                break;
            }
        }
    }

    public int getTowerTypeNum(){
        return this.typeList.size();
    }

    public ArrayList<String> getTowerTypeNames(){
        ArrayList<String> nameList = new ArrayList<>();
        System.out.println(this.typeList);
        for(int i = 0; i < this.typeList.size(); i++){
            nameList.add(this.typeList.get(i).getName());
        }

        return nameList;
    }
}
