package com.olympus.simulation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addGetProcedureRoom() {
        ProcedureRoom_Manager man = new ProcedureRoom_Manager();
        ProcedureRoom room = new ProcedureRoom(60, 10);

        man.addProcedureRoom(room);

        ProcedureRoom roomCheck = man.getProcedureRoom();

        assertEquals(room, roomCheck);
        assertEquals(60, roomCheck.getTravelTime());
    }

    @Test
    public void coolDownProcedureRoom(){
        ProcedureRoom_Manager man = new ProcedureRoom_Manager();
        ProcedureRoom room = new ProcedureRoom(60, 10);

        man.addProcedureRoom(room);

        man.runTick();

        assertEquals(0, room.getCooldownTimeLeft());

        room.startCooldown();

        for(int i = 10; i > 5; i--){
            assertEquals(i, room.getCooldownTimeLeft());
            man.runTick();
        }

        assertEquals(false, room.isAvailable());
        assertEquals(5, room.getCooldownTimeLeft());

        for(int i = 5; i >= 0; i--){
            assertEquals(i, room.getCooldownTimeLeft());
            man.runTick();
        }

        assertEquals(0, room.getCooldownTimeLeft());
        assertEquals(true, room.isAvailable());
    }

    @Test
    public void addGetClient() {
        Client_Manager man = new Client_Manager();
        Client client = new Client(new Procedure("Operation", 60, 180), 0);

        man.addClient(client);

        assertEquals(State.STATE_WAIT, client.getState());

        Client clientCheck = man.getNextClient(0);

        assertEquals(client, clientCheck);
        assertEquals("Operation", clientCheck.getProcedure().getName());
    }

    @Test
    public void setProcedureRoom(){
        Client_Manager man = new Client_Manager();
        Client client = new Client(new Procedure("Operation", 60, 180), 0);

        man.addClient(client);

        man.getNextClient(0);

        ProcedureRoom room = new ProcedureRoom(10, 20);

        assertEquals(0, client.getTimeLeft());
        client.setProcedureRoom(room);

        assertEquals(State.STATE_TRAVEL, client.getState());
        assertEquals(10, client.getTimeLeft());

        man.runTick();

        for(int i = 9; i >= 1; i--)
        {
            assertEquals(i, client.getTimeLeft());
            man.runTick();
        }

        assertTrue(client.getTimeLeft() >= 60 && client.getTimeLeft() <= 180);
        assertEquals(State.STATE_OPERATION, client.getState());
    }

    @Test
    public void simulationMatch(){
        Simulation_Manager simMan = new Simulation_Manager(0, 60, 1);

        Client client = new Client(new Procedure("Operation", 60, 180), 0);

        ProcedureRoom room1 = new ProcedureRoom(60, 10);
        ProcedureRoom room2 = new ProcedureRoom(30, 10);

        simMan.addClient(client);
        simMan.addProcedureRoom(room1);
        simMan.addProcedureRoom(room2);

        simMan.runTick();

        assertEquals(room2, client.getProcedureRoom());
        assertEquals(room2.getTravelTime(), client.getTimeLeft() + 1);
        assertTrue(room1.isAvailable());
        assertFalse(room2.isAvailable());

        Client client2 = new Client(new Procedure("Operator", 60, 180), 0);

        simMan.addClient(client2);

        simMan.runTick();

        assertEquals(room1, client2.getProcedureRoom());
    }
}