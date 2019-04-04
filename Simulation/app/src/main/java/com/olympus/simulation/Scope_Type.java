package com.olympus.simulation;

import java.util.ArrayList;

public class Scope_Type {

    private String name;
    private ArrayList<Procedure> procedureList;
    private int price;

    public Scope_Type (String n, int p) {
        this.name = n;
        procedureList = new ArrayList<Procedure>();
        this.price = p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Procedure> getProcedureList() {
        return procedureList;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean addProcedure(Procedure p) {
        if(procedureList.contains(p)) {
            return false;
        }

        this.procedureList.add(p);
        return true;
    }

    public boolean removeProcedure(Procedure p) {
        return procedureList.remove(p);
    }
}