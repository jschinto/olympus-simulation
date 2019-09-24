package com.olympus.simulation;

import java.util.ArrayList;

public class Nurse_Manager {
    private ArrayList<Nurse> nurses;

    public Nurse_Manager() {
        nurses = new ArrayList<Nurse>();
    }

    public int getNurseNum() {
        return nurses.size();
    }

    public boolean nurseAvailable() {
        for (int i=0; i < nurses.size(); i++) {
            if (nurses.get(i).getState() == State.STATE_WAIT) {
                return true;
            }
        }
        return false;
    }
}
