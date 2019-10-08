package com.olympus.simulation;

import java.util.ArrayList;

public class ManualCleaningStation_Manager {
    static ArrayList<ManualCleaningStation> stations;

    public ManualCleaningStation_Manager(){
        stations = new ArrayList<>();
    }

    public ArrayList<ManualCleaningStation> getManualCleaningStations(){
        return stations;
    }

    public ManualCleaningStation getManualCleaningStationByIndex(int index){
        return stations.get(index);
    }

    public int getManualCleaningStationNum(){
        return stations.size();
    }

    public void addManualCleaningStation (ManualCleaningStation station) {
        stations.add(station);
        sort();
    }

    public void removeManualCleaningStation(ManualCleaningStation station) {
        stations.remove(station);
        sort();
    }
    public void removeManualCleaningStationByLeakTester(String leakTesterName) {
        for (int i=0; i < stations.size(); i++) {
            if (stations.get(i).currentLeakTester.getName().equals(leakTesterName)) {
                stations.remove(i);
                sort();
                return;
            }
        }
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
        for(int i = 0; i < stations.size(); i++){
            for(int j = i; j < stations.size(); j++){
                if(stations.get(i).getCurrentLeakTester().getTimeToComplete() > stations.get(j).getCurrentLeakTester().getTimeToComplete()){
                    ManualCleaningStation temp = stations.get(i);
                    stations.set(i, stations.get(j));
                    stations.set(j, temp);
                }
            }
        }
    }
}
