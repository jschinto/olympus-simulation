package com.olympus.simulation;

import java.util.ArrayList;

public class Procedure_Manager {
    private ArrayList<Procedure> procedureList;

    public Procedure_Manager() {
        procedureList = new ArrayList<Procedure>();
    }

    public void addProcedure(Procedure procedure) {
        procedureList.add(procedure);
    }

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

    public ArrayList<String> getProcedureNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i=0; i < procedureList.size(); i++) {
            names.add(procedureList.get(i).getName());
        }
        return names;
    }

}
