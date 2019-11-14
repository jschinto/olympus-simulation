package com.olympus.simulation;

import android.content.Context;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Simulation_Manager {
    //the ProcedureRoom and Client Managers needed to run the simulation
    private ProcedureRoom_Manager procedureRoomManager;
    private Client_Manager clientManager;
    private Procedure_Manager procedureManager;
    private  Scope_Manager scopeManager;
    private ScopeType_Manager scopeTypeManager;
    private TowerType_Manager towerTypeManager;
    private Nurse_Manager nursemanager;
    private Doctor_Manager doctormanager;
    private LeakTesterType_Manager leakTesterTypeManager;
    private ManualCleaningStation_Manager manualCleaningStationManager;
    private Technician_Manager technicianManager;
    private ReprocessorType_Manager reprocessorTypeManager;
    private Reprocessor_Manager reprocessorManager;

    //the time the simulated hospital should open and close
    //Represented as a integer from 0 - some value
    private int startTime;

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    private int endTime;
    //Time the system should wait between ticks.
    private int waitTime;

    public int getCurrTime() {
        return currTime;
    }

    public void setCurrTime(int currTime) {
        this.currTime = currTime;
    }

    public void incrementCurrTime() {
        this.currTime++;
    }

    //Current time of the simulation
    private int currTime;

    //Initializes the simulation using the startTime, endTime, and waitTime
    public Simulation_Manager(int startTime, int endTime, int waitTime) {
        procedureRoomManager = new ProcedureRoom_Manager();
        clientManager = new Client_Manager();
        procedureManager = new Procedure_Manager();
        scopeManager = new Scope_Manager();
        scopeTypeManager = new ScopeType_Manager();
        towerTypeManager = new TowerType_Manager();
        leakTesterTypeManager = new LeakTesterType_Manager();
        nursemanager = new Nurse_Manager();
        doctormanager = new Doctor_Manager();
        manualCleaningStationManager = new ManualCleaningStation_Manager();
        technicianManager = new Technician_Manager();
        reprocessorTypeManager = new ReprocessorType_Manager();
        reprocessorManager = new Reprocessor_Manager();
        this.startTime = startTime;
        this.endTime = endTime;
        this.waitTime = waitTime;
        this.currTime = this.startTime;
    }

    /*
       Runs a tick of time
       Assigns a client to a procedure room if one is found to be ready
       Runs a tick for client and procedure rooms so they can process each individual client or room.
     */
    public boolean runTick() {
        //Grabs an open procedure room and attempts to assign a client to it. Continues to match
        //Clients to procedure rooms until a pair cannot be made.

        clientManager.runTick(Time.convertToString(currTime));
        scopeManager.runTick(Time.convertToString(currTime));
        nursemanager.runTick();
        doctormanager.runTick();
        technicianManager.runTick();
        procedureRoomManager.runTick(Time.convertToString(currTime));
        reprocessorManager.runTick();

        int patientOffset = 0;
        int roomOffset = 0;
        ProcedureRoom openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
        while (openRoom != null) {
            Client nextClient = clientManager.getNextClient(currTime, patientOffset);
            if (nextClient == null) {
                break;
            }

            Nurse freeNurse = nursemanager.getNurse();
            if(freeNurse == null){
                break;
            }

            Doctor freeDoctor = doctormanager.getDoctor(nextClient.getProcedureList());
            if(freeDoctor == null){
                break;
            }

            if(!openRoom.checkCanProcess(nextClient)){
                roomOffset++;
                openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
                if(openRoom == null && clientManager.getNextClient(currTime, patientOffset + 1) == null){
                    break;
                }
                else if(clientManager.getNextClient(currTime, patientOffset + 1) == null){
                    roomOffset = 0;
                    openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
                    patientOffset++;
                }

                continue;
            }

            ArrayList<Scope> scopeList = new ArrayList<Scope>();
            for(int i = 0; i < nextClient.getProcedureList().size(); i++) {
                Scope freeScope = scopeManager.getAvaliableScope(nextClient.getProcedureList().get(i));
                Technician tech = Technician_Manager.getTechnician();
                if (freeScope == null || tech == null) {
                    for(int j = 0; j < scopeList.size(); j++){
                        scopeList.get(j).setTempGrab(false);
                        scopeList.get(j).setHolding(null, -1);
                    }
                    break;
                }
                if(!scopeList.contains(freeScope)) {
                    tech.setDestination(openRoom);
                    freeScope.setHolding(tech, openRoom.getTravelTime());
                    scopeList.add(freeScope);
                    freeScope.setTempGrab(true);
                }
            }

            if(scopeList.size() == nextClient.getProcedureList().size()) {
                roomOffset = 0;
                clientManager.addToOperating(nextClient);
                nextClient.setProcedureRoom(openRoom);
                openRoom.claimElements(scopeList, freeDoctor, freeNurse);
                for(Scope s : scopeList) {
                    String proc = "";
                    for(Procedure p : nextClient.getProcedureList()) {
                        if(s.checkProcedure(p)) {
                            proc = p.getName();
                        }
                    }
                    String station = "Hallway";
                    ScopeLogCSV logItem = new ScopeLogCSV(s.getType().getName(), s.getId() + "", "Scope " + s.getId(), Time.convertToString(currTime), Time.convertToString(currTime), proc, station, State_Scope.stateNames[s.getState()]);
                    scopeManager.addScopeLogCSV(logItem);//scope is traveling
                }
                openRoom = procedureRoomManager.getProcedureRoom(roomOffset);
                clientManager.sortQueue();
            }
            else
            {
                roomOffset = 0;
                patientOffset++;
            }
        }
        scopeManager.sortList();
        clientManager.sortQueue();

        for(int i = 0; i < this.scopeManager.getScopeNum(); i++){
            Technician tech = Technician_Manager.getTechnician();
            if(tech != null && this.scopeManager.getScopeByIndex(i).getState() == State_Scope.STATE_CLEANING && this.scopeManager.getScopeByIndex(i).getTimeLeft() == 0){
                ManualCleaningStation station = manualCleaningStationManager.getFreeStation();
                if(station == null){
                    break;
                }
                tech.setDestination(station);
                this.scopeManager.getScopeByIndex(i).setHolding(tech, 5);
                this.scopeManager.getScopeByIndex(i).startClean(station);
            }
        }

        for(int i = 0; i < this.scopeManager.getScopeNum(); i++){
            if(this.scopeManager.getScopeByIndex(i).getState() == State_Scope.STATE_REPROCESSING && !this.scopeManager.getScopeByIndex(i).isInReprocessor()){
                Reprocessor reprocessor = reprocessorManager.getFreeReprocessor();
                if(reprocessor == null){
                    break;
                }
                if(this.scopeManager.getScopeByIndex(i).getReprocessorLoadDelay() == 0) {
                    reprocessor.addScope(this.scopeManager.getScopeByIndex(i));
                    this.scopeManager.getScopeByIndex(i).setInReprocessor(true);
                    this.scopeManager.getScopeByIndex(i).setReprocessorLoadDelay(-1);
                } else if (this.scopeManager.getScopeByIndex(i).getReprocessorLoadDelay() == -1) {
                    this.scopeManager.getScopeByIndex(i).startReprocessorLoadDelay();
                } else {
                    this.scopeManager.getScopeByIndex(i).decrementReprocessorLoadDelay();
                }
            }
        }
        return false;
    }

    //Adds a client to the client manager
    public void addClient(Client client) {
        clientManager.addClient(client);
        clientManager.setIDs();
    }

    //Adds a procedure room to the procedure room manager
    public void addProcedureRoom(ProcedureRoom room) {
        procedureRoomManager.addProcedureRoom(room);
        procedureRoomManager.setIDs();
    }

    //Adds a procedure to the list of procedures
    public void addProcedure(Procedure procedure) {
        procedureManager.addProcedure(procedure);
    }

    //Returns the number of clients in the waiting room
    public int getClientNum() {
        return clientManager.getClientNum();
    }

    //Returns the number of procedure rooms in the simulation
    public int getProcedureRoomNum() {
        return procedureRoomManager.getProcedureRoomNum();
    }

    public int getProcedureNum() {
        return procedureManager.getProcedureNum();
    }

    //Returns a client based on the given index. Associated with their position in the queue.
    public Client getClient(int index) {
        return clientManager.getClientByIndex(index);
    }

    public ArrayList<Client> getWaitingRoom() {
        ArrayList<Client> waitingRoom = new ArrayList<>();
        for (Client c:clientManager.getQueue()) {
            if(c.getState() == State.STATE_WAIT || c.getState() == State.STATE_DONE) {
                waitingRoom.add(c);
            }
        }
        return waitingRoom;
    }

    public ArrayList<Object> getHallway() {
        ArrayList<Object> hallway = new ArrayList<>();

        for (Client c:clientManager.getQueue()) {
            if(c.getState() == State.STATE_TRAVEL) {
                hallway.add(c);
            }
        }
        for (Nurse n:nursemanager.getNurseList()) {
            if (n.getState() == State.STATE_TRAVEL) {
                hallway.add(n);
            }
        }
        for (Doctor d:doctormanager.getAllDoctors()) {
            if (d.getState() == State.STATE_TRAVEL) {
                hallway.add(d);
            }
        }
        for (Technician t:technicianManager.getTechnicianList()) {
            if (t.getState() == State.STATE_TRAVEL) {
                hallway.add(t);
            }
        }
        for(Scope s:scopeManager.getScopes()) {
            if(s.getState() == State_Scope.STATE_TRAVEL || s.getState() == State_Scope.STATE_DIRTY || s.getState() == State_Scope.STATE_RETURNING || s.getState() == State_Scope.STATE_TOREPROCESS) {
                hallway.add(s);
            }
        }
        return hallway;
    }

    //Returns the procedure room based on the given index. Associated with their position in the list.
    public ProcedureRoom getProcedureRoom(int index){
        return procedureRoomManager.getProcedureRoomByIndex(index);
    }

    public ArrayList<ProcedureRoom> getProcedureRooms() {
        return procedureRoomManager.getProcedureRooms();
    }

    //Returns a list of all the available procedures.
    public String[] getProcedureNames() {
        ArrayList<String> procedureNames = procedureManager.getProcedureNames();
        String[] procedureNamesArray = new String[procedureNames.size()];
        return procedureNames.toArray(procedureNamesArray);
    }

    public Procedure getProcedureByName(String name){
        return procedureManager.getProcedureByName(name);
    }

    public Procedure getProcedure(int index) {
        return procedureManager.getProcedure(index);
    }

    //Updates the client at the position of the given index with the given Client
    public void editClient(Client client, int index) {
        clientManager.setClient(client, index);
        clientManager.setIDs();
    }

    //Updates the procedure room at the position of the given index with the given Procedure Room
    public void editProcedureRoom(ProcedureRoom room, int index){
        procedureRoomManager.setProcedureRoom(room, index);
        procedureRoomManager.setIDs();
    }

    //Removes the client at the given index.
    public void deleteClient(int index) {
        clientManager.deleteClient(index);
        clientManager.setIDs();
    }

    //Removes the procedure room at the given index.
    public void deleteProcedureRoom(int index) {
        procedureRoomManager.deleteProcedureRoom(index);
        procedureRoomManager.setIDs();
    }

    public void deleteProcedure(String name) {
        procedureManager.deleteProcedure(name);
    }

    public int getLatestClientTime() {
        return clientManager.getLatestTime();
    }
    public void addScopeType(Scope_Type t) {
        scopeTypeManager.addScopeType(t);
    }

    public void removeScopeType(String t) {
        scopeTypeManager.deleteScopeType(t);
    }

    public String[] getScopeTypeNames() {
        ArrayList<String> scopeTypeNames = scopeTypeManager.getScopeTypeNames();
        String[] scopeTypeNamesArray = new String[scopeTypeNames.size()];
        return scopeTypeNames.toArray(scopeTypeNamesArray);
    }

    public Scope_Type getScopeTypeByName(String name) {
        return scopeTypeManager.getScopeTypeByName(name);
    }

    public void addScope(Scope s) {
        scopeManager.addScope(s);
        scopeManager.setIDs();
    }

    public void removeScope(Scope s) {
        scopeManager.removeScope(s);
        scopeManager.setIDs();
    }

    public void removeScope(int i) {
        scopeManager.removeScopeByIndex(i);
        scopeManager.setIDs();
    }

    public int getScopeNum() {return scopeManager.getScopeNum();}

    public Scope getScopeByIndex(int index) {return scopeManager.getScopeByIndex(index);}

    public ArrayList<Scope> getFreeScopes() {
        ArrayList<Scope> scopes = new ArrayList<>();
        for(Scope s:scopeManager.getScopes()) {
            if(s.getState() == State_Scope.STATE_FREE) {
                scopes.add(s);
            }
        }
        return scopes;
    }

    public void setIDs() {
        clientManager.setIDs();
        procedureRoomManager.setIDs();
        scopeManager.setIDs();
    }

    public void addTowerType(Tower_Type type){
        towerTypeManager.addTowerType(type);
    }

    public String[] getTowerTypeNames(){
        ArrayList<String> list = towerTypeManager.getTowerTypeNames();
        String[] towerTypeNamesArray = new String[list.size()];
        return list.toArray(towerTypeNamesArray);
    }

    public Tower_Type getTowerTypeByName(String name){
        return towerTypeManager.getTowerTypeByName(name);
    }

    public int getTowerTypeNum() {
        return towerTypeManager.getTowerTypeNum();
    }

    public void removeTowerTypeByName(String name) {
        towerTypeManager.removeTowerType(name);
    }

    public String[] getLeakTesterTypeNames(){
        return leakTesterTypeManager.getLeakTesterTypeNames();
    }

    public void addLeakTester(LeakTester_Type type){
        leakTesterTypeManager.addLeakTesterType(type);
    }

    public int getLeakTesterTypeNum(){
        return leakTesterTypeManager.getLeakTesterTypeNames().length;
    }

    public LeakTester_Type getLeakTesterByName(String name){
        return leakTesterTypeManager.getLeakTesterTypeByName(name);
    }

    public void removeLeakTesterType(LeakTester_Type type){
        leakTesterTypeManager.removeLeakTesterType(type);
    }

    public void removeLeakTesterTypeByName(String name) {
        leakTesterTypeManager.removeLeakTesterTypeByName(name);
    }

    public ArrayList<ManualCleaningStation> getManualCleaningStations(){
        return manualCleaningStationManager.getManualCleaningStations();
    }

    public ManualCleaningStation getManualCleaningStationByIndex(int index){
        return manualCleaningStationManager.getManualCleaningStationByIndex(index);
    }

    public int getManualCleaningStationNum(){
        return manualCleaningStationManager.getManualCleaningStationNum();
    }

    public void addManualCleaningStation(ManualCleaningStation station) {
        manualCleaningStationManager.addManualCleaningStation(station);
    }

    public void removeManualCleaningStationByLeakTester(String leakTesterName) {
        manualCleaningStationManager.removeManualCleaningStationByLeakTester(leakTesterName);
    }

    //TODO: maybe show toast for patients deleted
    public int removeClientsOutsideRange() {
        return clientManager.removeClientsOutsideRange(startTime, endTime);
    }

    public ArrayList<CSVable> getEquipmentCSVList() {
        //Add all equipment (must implement equipment interface) here:
        ArrayList<EquipmentCSV.Equipment> equip = new ArrayList<>();
        equip.addAll(scopeManager.getScopes());

        //Convert to CSVable Equipment Type:
        ArrayList<CSVable> toReturn = new ArrayList<>();
        for(EquipmentCSV.Equipment e : equip) {
            toReturn.add(e.getEquipmentCSV());
        }
        return toReturn;
    }

    public ArrayList<CSVable> getStationCSVList() {
        StationCSV waitingRoom = new StationCSV("Room", "Waiting Room");
        StationCSV hallway = new StationCSV("Room", "Hallway");
        StationCSV sink = new StationCSV("Sink", "Sink");
        StationCSV reprocessor = new StationCSV("Reprocessor", "Reprocessor");
        ArrayList<StationCSV.Station> stations = new ArrayList<>();
        stations.addAll(procedureRoomManager.getProcedureRooms());

        ArrayList<CSVable> toReturn = new ArrayList<>();
        toReturn.add(waitingRoom);
        toReturn.add(hallway);
        toReturn.add(sink);
        toReturn.add(reprocessor);
        for(StationCSV.Station s : stations) {
            toReturn.add(s.getStationCSV());
        }
        return toReturn;
    }

    public ArrayList<CSVable> getScopeLogCSVList() {
        scopeManager.finalizeCSVList(Time.convertToString(currTime));
        ArrayList<CSVable> toReturn = new ArrayList<>();
        for(ScopeLogCSV sl : scopeManager.getCsvList()) {
            toReturn.add(sl);
        }
        return toReturn;
    }

    public ArrayList<CSVable> getActorLogCSVList() {
        nursemanager.finalizeCSVList(Time.convertToString(currTime));
        doctormanager.finalizeCSVList(Time.convertToString(currTime));
        technicianManager.finalizeCSVList(Time.convertToString(currTime));
        ArrayList<CSVable> toReturn = new ArrayList<>();
        for(ActorLogCSV al : nursemanager.getCsvList()) {
            toReturn.add(al);
        }
        for(ActorLogCSV al : doctormanager.getCsvList()) {
            toReturn.add(al);
        }
        for(ActorLogCSV al : technicianManager.getCsvList()) {
            toReturn.add(al);
        }
        return toReturn;
    }

    public void initLogs() {
        scopeManager.initCSVList(Time.convertToString(startTime), Time.convertToString(endTime));
        nursemanager.initCSVList(Time.convertToString(startTime), Time.convertToString(endTime));
        doctormanager.initCSVList(Time.convertToString(startTime), Time.convertToString(endTime));
        technicianManager.initCSVList(Time.convertToString(startTime), Time.convertToString(endTime));
    }

    public void createLog(Context mcoContext, String filename, String CSVHeader, ArrayList<CSVable> logItems) {
        String body = CSVHeader;
        for(int i = 0; i < logItems.size(); i++) {
            body += "\n";
            body += logItems.get(i).getCSV();
        }
        FileHelper fileHelper = new FileHelper(filename + "-log.csv");
        fileHelper.writeFileOnInternalStorage(mcoContext, body);
    }

    public interface CSVable {
        String getCSV();
        String getCSVHeader();
    }

    public int getNurseNum() {
        return nursemanager.getNurseNum();
    }
    public int getNursePostProcedureTime() {
        return nursemanager.getPostProcedureTime();
    }
    public void setNurseNum(int num) {
        nursemanager.setNurseNum(num);
    }
    public void setNursePostProcedureTime(int time) {
        nursemanager.setPostProcedureTime(time);
    }
    public int getFreeNurses() {
        return nursemanager.getFreeNurses();
    }

    public int getDoctorNum() {
        return doctormanager.getDoctorNum();
    }
    public int getDoctorPostProcedureTime() {
        return doctormanager.getPostProcedureTime();
    }
    public void setDoctorPostProcedureTime(int time) {
        doctormanager.setPostProcedureTime(time);
    }
    public void addDoctor(Doctor doctor) {
        doctormanager.addDoctor(doctor);
    }
    public Doctor getDoctorByIndex(int i){
        return doctormanager.getDoctorByIndex(i);
    }
    public void removeDoctorByIndex(int i) {
        doctormanager.removeDoctorByIndex(i);
    }
    public ArrayList<Doctor> getFreeDoctors() {
        return doctormanager.getFreeDoctors();
    }

    public int getTechnicianNum(){return technicianManager.getTechnicianNum();}
    public int getTechnicianFreeNum() {return technicianManager.getFreeTechnicianNum();}
    public void setTechnicianNum(int num) {technicianManager.setTechnicianNum(num);}

    public ArrayList<Reprocessor> getReprocessors() {
        return reprocessorManager.getReprocessors();
    }
    public Reprocessor getReprocessorByIndex(int i) {
        return reprocessorManager.getReprocessorByIndex(i);
    }
    public Reprocessor_Type getReprocessorTypeByName(String name) {
        return reprocessorTypeManager.getTypeByName(name);
    }
    public void removeReprocessorTypeByName(String name) {
        reprocessorTypeManager.removeTypeByName(name);
    }
    public void removeReprocessorByType(String type) {
        reprocessorManager.removeReprocessorByType(type);
    }
    public void addReprocessor(Reprocessor reprocessor) {
        reprocessorManager.addReprocessor(reprocessor);
    }

    public void addReprocessorType(Reprocessor_Type reprocessor_type) {
        reprocessorTypeManager.addType(reprocessor_type);
    }

    public String[] getReprocessorTypeNames() {
        return reprocessorTypeManager.getTypeNames();
    }

    public Boolean isEverythingDone() {
        boolean done = true;
        done &= clientManager.isEverythingDone();
        done &= scopeManager.isEverythingDone();
        done &= procedureRoomManager.isEverythingDone();
        done &= nursemanager.isEverythingDone();
        done &= technicianManager.isEverythingDone();
        done &= doctormanager.isEverythingDone();

        return done;
    }
}
