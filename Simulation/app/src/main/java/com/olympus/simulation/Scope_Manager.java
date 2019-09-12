package com.olympus.simulation;

import java.util.ArrayList;
import java.util.Collections;

public class Scope_Manager {
    private ArrayList<Scope> list;

    public Scope_Manager(){
        list = new ArrayList<Scope>();
        //types = new ArrayList<Scope_Type>();
    }

    public Scope getScopeByIndex(int index) {
        return list.get(index);
    }

    public int getScopeNum() {
        return list.size();
    }

    public void addScope(Scope s) {
        list.add(s);
        sortList();
    }

    public boolean removeScope(Scope s) {
       boolean success = list.remove(s);
       sortList();
       return success;
    }

    public void removeScopeByIndex(int i) {

        list.remove(i);
        sortList();
    }

    public void runTick() {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getState() == State_Scope.STATE_TRAVEL || list.get(i).getState() == State_Scope.STATE_DIRTY) {
                list.get(i).tick();
            }
        }
        sortList();
    }

    public Scope getAvaliableScope(Procedure p) {

        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).checkProcedure(p) && list.get(i).getState() == State_Scope.STATE_FREE && !list.get(i).getTempGrab()) {
                return list.get(i);
            }
        }

        return null;
    }

    public void sortList(){
        Collections.sort(list);
    }

    public void setIDs() {
        for(int i = 0; i < list.size(); i++) {
            list.get(i).setId(i + 1);
        }
    }

    public ArrayList<Scope> getScopes() {
        return list;
    }
}
