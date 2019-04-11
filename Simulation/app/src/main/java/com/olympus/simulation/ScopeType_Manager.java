package com.olympus.simulation;

import java.util.ArrayList;

public class ScopeType_Manager {
    private ArrayList<Scope_Type> scopeTypeList;

    public ScopeType_Manager() {
        scopeTypeList = new ArrayList<Scope_Type>();
    }

    public void addScopeType(Scope_Type type) {
        if (getScopeTypeByName(type.getName()) != null) {
            deleteScopeType(type.getName());
        }
        scopeTypeList.add(type);
    }

    //deletes first instance of the given ScopeType name
    public void deleteScopeType(String name) {
        for (int i=0; i < scopeTypeList.size(); i++) {
            if (scopeTypeList.get(i).getName().equals(name)) {
                deleteScopeType(i);
                return;
            }
        }
    }

    public void deleteScopeType(int index) {
        scopeTypeList.remove(index);
    }

    //returns a list of the ScopeType names from the list of created ScopeTypes
    public ArrayList<String> getScopeTypeNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i=0; i < scopeTypeList.size(); i++) {
            names.add(scopeTypeList.get(i).getName());
        }
        return names;
    }

    public Scope_Type getScopeTypeByName(String name) {
        for(Scope_Type p : scopeTypeList){
            if(p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    public Scope_Type getScopeType(int index) {
        return scopeTypeList.get(index);
    }

    public int getScopeTypeNum() {
        return scopeTypeList.size();
    }
}
