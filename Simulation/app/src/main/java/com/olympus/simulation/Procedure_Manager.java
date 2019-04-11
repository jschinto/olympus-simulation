package com.olympus.simulation;

import java.util.ArrayList;

public class Procedure_Manager {
    private ArrayList<Procedure> procedureList;

    public Procedure_Manager() {
        procedureList = new ArrayList<Procedure>();
    }

    public void addProcedure(Procedure procedure) {
        if (getProcedureByName(procedure.getName()) != null) {
            deleteProcedure(procedure.getName());
        }
        procedureList.add(procedure);
    }

    //deletes first instance of the given procedure name
    public void deleteProcedure(String name) {
        for (int i=0; i < procedureList.size(); i++) {
            if (procedureList.get(i).getName().equals(name)) {
                deleteProcedure(i);
                return;
            }
        }
    }

    public void deleteProcedure(int index) {
        procedureList.remove(index);
    }

    //returns a list of the procedure names from the list of created procedures
    public ArrayList<String> getProcedureNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i=0; i < procedureList.size(); i++) {
            names.add(procedureList.get(i).getName());
        }
        return names;
    }

    public Procedure getProcedureByName(String name) {
        for(Procedure p : procedureList){
            if(p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    public Procedure getProcedure(int index) {
        return procedureList.get(index);
    }

    public int getProcedureNum() {
        return procedureList.size();
    }

}
