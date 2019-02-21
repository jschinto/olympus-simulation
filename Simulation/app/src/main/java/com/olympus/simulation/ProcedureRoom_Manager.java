package com.olympus.simulation;

import java.util.ArrayList;

public class ProcedureRoom_Manager {
    //the rooms included in the simulation
    private ArrayList<ProcedureRoom> roomList;

    public ProcedureRoom_Manager() {
        roomList = new ArrayList<ProcedureRoom>();
    }

    //run the operations for a tick of time
    public void runTick() {
        for (int i=0; i < roomList.size(); i++) {
            if (roomList.get(i).getCooldownTime() > 0) {
                roomList.get(i).tick();
            }
        }
    }

    //when adding a room, insert to sort by least travel time
    public void addProcedureRoom(ProcedureRoom room) {
        int travelTime = room.getTravelTime();
        for (int i=0; i < roomList.size(); i++) {
            if (travelTime <= roomList.get(i).getTravelTime()) {
                roomList.add(i, room);
                return;
            }
        }
        roomList.add(room);
    }

    //when getting a room, find the first room (least travel time) that is not occupied
    public ProcedureRoom getProcedureRoom() {
        ProcedureRoom room;
        for (int i=0; i < roomList.size(); i++) {
            if (roomList.get(i).isAvailable()) {
                return roomList.get(i);
            }
        }
        return null;
    }

}
