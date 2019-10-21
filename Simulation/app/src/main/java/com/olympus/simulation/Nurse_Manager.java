package com.olympus.simulation;

import java.util.ArrayList;

public class Nurse_Manager {
    private ArrayList<Nurse> nurses;

    private static int postProcedureTime;

    public Nurse_Manager() {
        nurses = new ArrayList<Nurse>();
        postProcedureTime = 1;

        nurses.add(new Nurse());
    }

    public void runTick(){
        for(int i = 0; i < this.nurses.size(); i++){
            this.nurses.get(i).tick();
        }
    }

    public int getNurseNum() {
        return nurses.size();
    }

    public Nurse getNurse() {
        for (int i=0; i < nurses.size(); i++) {
            if (nurses.get(i).getState() == State.STATE_WAIT) {
                Nurse nurse = nurses.get(i);
                return nurse;
            }
        }
        return null;
    }

    public void operationComplete(Nurse nurse) {
        nurse.startPostProcedure(postProcedureTime);
    }

    public static int getPostProcedureTime() {
        return postProcedureTime;
    }

    public void setPostProcedureTime(int postProcedureTime) {
        this.postProcedureTime = postProcedureTime;
    }
    public void setNurseNum(int num) {
        nurses = new ArrayList<Nurse>();
        for (int i=0; i < num; i++) {
            nurses.add(new Nurse());
        }
    }


    public Boolean isEverythingDone() {
        boolean done = true;
        for (Nurse n : nurses) {
            done &= n.getState() == State.STATE_WAIT;
        }
        return done;
    }
    public int getFreeNurses() {
        int count = 0;
        for (int i=0; i < nurses.size(); i++) {
            if (nurses.get(i).getState() == State.STATE_WAIT) {
                count++;
            }
        }
        return count;
    }
}
