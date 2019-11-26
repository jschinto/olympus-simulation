package com.olympus.simulation;

import java.io.Serializable;
import java.util.ArrayList;

public class Tower_Type implements Serializable {
    String name;
    ArrayList<Scope_Type> holds;
    int price;

    //Creates a new Tower_Type that can hold the given scopes
    public Tower_Type(String name, ArrayList<Scope_Type> types, int price) {
        this.name = name;
        this.holds = types;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    //Returns the list of scopes that the tower can hold
    public ArrayList<Scope_Type> getScopeTypes(){
        return this.holds;
    }

    //Sets the list of scopes that the tower can hold to the given list
    public void setScopeType(ArrayList<Scope_Type> types){
        this.holds = types;
    }

    //Adds the given scope type to the list of types that the tower can hold
    public void addScopeType(Scope_Type type){
        this.holds.add(type);
    }

    //Removes the given scope name from the list of types that the tower can hold
    public void removeScopeType(String typeName){
        for(int i = 0; i < this.holds.size(); i++){
            if(this.holds.get(i).getName() == typeName){
                this.holds.remove(i);
                break;
            }
        }
    }

    public int getPrice(){
        return this.price;
    }

    public void setPrice(int price){
        this.price = price;
    }

    //Returns true if this tower type can process the given procedure, false otherwise
    public boolean checkProcessProcedure(Procedure p){
        for(int i = 0; i < this.holds.size(); i++){
            if (this.holds.get(i).checkProcedure(p)){
                return true;
            }
        }
        return false;
    }
}