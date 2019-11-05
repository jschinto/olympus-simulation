package com.olympus.simulation;

import java.io.Serializable;

public class Procedure implements Serializable {

    //name given to the operation
    private String name;
    //the minimum and maximum time the operation will last, to account for variance in completion time
    private int time;
    //needs? TODO: need a way to store and check a procedure's needs

    public Procedure(String name, int time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime (int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Procedure) {
            return ((Procedure)o).getName().equals(this.getName()) && ((Procedure)o).getTime() == (this.getTime());
        }
        return super.equals(o);
    }





}