package com.olympus.simulation;

import java.util.ArrayList;

public class Technician_Manager {
    private static ArrayList<Technician> technicians;

    public Technician_Manager(){
        technicians = new ArrayList<>();
        technicians.add(new Technician());
    }

    public int getTechnicianNum(){
        return technicians.size();
    }

    public static Technician getTechnician() {
        for(int i = 0; i < technicians.size(); i++){
            if(technicians.get(i).getState() == State.STATE_WAIT){
                Technician tech = technicians.get(i);
                return tech;
            }
        }
        return null;
    }

    public void setTechnicianNum(int num) {
        this.technicians = new ArrayList<Technician>();
        for (int i=0; i < num; i++) {
            this.technicians.add(new Technician());
        }
    }

    public int getFreeTechnicianNum(){
        int count = 0;
        for(int i = 0; i < technicians.size(); i++){
            if(technicians.get(i).getState() == State.STATE_WAIT){
                count++;
            }
        }
        return count;
    };

    public Boolean isEverythingDone() {
        return getTechnicianNum() == getFreeTechnicianNum();
    }
}
