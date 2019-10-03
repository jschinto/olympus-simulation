package com.olympus.simulation;

import java.util.ArrayList;

public class ManualCleaningStation_Manager {
    static ArrayList<ManualCleaningStation> stations;

    public ManualCleaningStation_Manager(){
        this.stations = new ArrayList<>();
    }

    public ArrayList<ManualCleaningStation> getManualCleaningStations(){
        return this.stations;
    }

    public ManualCleaningStation getManualCleaningStationByIndex(int index){
        return this.stations.get(index);
    }

    public int getManualCleaningStationNum(){
        return this.stations.size();
    }

    public void addManualCleaningStation (ManualCleaningStation station) {
        this.stations.add(station);
        sort();
    }

    public void removeManualCleaningStation(ManualCleaningStation station) {
        this.stations.remove(station);
        sort();
    }

    public static ManualCleaningStation getFreeStation() {
        for(int i = 0; i < stations.size(); i++){
            if(stations.get(i).getCurrentScope() == null){
                return stations.get(i);
            }
        }
        return null;
    }

    //TODO: Make this not garbage bubble sort
    public void sort(){
        for(int i = 0; i < this.stations.size(); i++){
            for(int j = i; j < this.stations.size(); j++){
                if(this.stations.get(i).getCurrentLeakTester().getTimeToComplete() > this.stations.get(j).getCurrentLeakTester().getTimeToComplete()){
                    ManualCleaningStation temp = this.stations.get(i);
                    this.stations.set(i, this.stations.get(j));
                    this.stations.set(j, temp);
                }
            }
        }
    }
}
