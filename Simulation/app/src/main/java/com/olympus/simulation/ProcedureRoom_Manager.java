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
            if (roomList.get(i).getCooldownTimeLeft() > 0) {
                roomList.get(i).tick();//call tick on each room
            }
            if(roomList.get(i).isReady() == false) {
                if(roomList.get(i).getScopeList().size() == 0) {
                    continue;
                }
                int checker = 0;
                for(Scope s : roomList.get(i).getScopeList()) {
                    if(s.getState() == State_Scope.STATE_USE) {
                        checker++;
                    }
                }

                if(checker == roomList.get(i).getScopeList().size()) {
                    roomList.get(i).setReady(true);
                }
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

    public ProcedureRoom getProcedureRoomByIndex(int index)
    {
        return roomList.get(index);
    }

    public void setProcedureRoom(ProcedureRoom room, int index) {
        roomList.set(index, room);
    }

    public void deleteProcedureRoom(int index){
        roomList.remove(index);
    }

    public int getProcedureRoomNum(){
        return roomList.size();
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

    public void setIDs() {
        for(int i = 0; i < roomList.size(); i++) {
            roomList.get(i).setId(i + 1);
        }
    }
}
