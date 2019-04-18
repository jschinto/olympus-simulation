package com.olympus.simulation;

import java.util.ArrayList;
import java.util.Collections;

public class Scope_Manager {
    private ArrayList<Scope_Type> types;
    private ArrayList<Scope> list;

    public Scope_Manager(){
        list = new ArrayList<Scope>();
        //types = new ArrayList<Scope_Type>();
    }

    public Scope getScopeByIndex(int index) {
        return list.get(index);
    }
    /*
    public void addScopeType(Scope_Type t) {
        types.add(t);
    }

    public void removeScopeType(Scope_Type t) {
        types.remove(t);
    }

    public ArrayList<Scope_Type> getScopeTypes() {
        return types;
    }
*/
    public int getScopeNum() {
        return list.size();
    }

    public void addScope(Scope s) {
        list.add(s);
        sortList();
    }

    public boolean removeScope(Scope s) {
        return list.remove(s);
    }

    public void removeScopeByIndex(int i) {
        list.remove(i);
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
        int i = 0;

        while(i < list.size() && list.get(i).getState() == State_Scope.STATE_FREE) {
            if(list.get(i).checkProcedure(p)) {
                return list.get(i);
            }
            i++;
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
}
