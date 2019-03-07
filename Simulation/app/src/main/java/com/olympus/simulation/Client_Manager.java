package com.olympus.simulation;

import java.util.ArrayList;
import java.util.Collections;

public class Client_Manager {
    //the queue of clients entering and exiting the waiting room
    private ArrayList<Client> queue;
    //the list of clients currently assigned a procedure room
    private ArrayList<Client> operating;

    public Client_Manager() {
        queue = new ArrayList<Client>();
        operating = new ArrayList<Client>();
    }
    //run the operations for a tick of time
    public void runTick() {
        for (int i=0; i < operating.size(); i++) {
            if (operating.get(i).getState() == State.STATE_DONE) {
                operating.remove(i);
                i--;
            }
            else if (operating.get(i).getTimeLeft() > 0) {
                operating.get(i).tick();
            }
        }
    }

    public void sortQueue() {
        Collections.sort(queue);
    }

    //add at the end of the queue
    public void addClient(Client client) {
        queue.add(client);
        sortQueue();
    }

    public Client getClientByIndex(int index) {
        return queue.get(index);
    }

    //remove at the beginning of the queue, change state and add to operating list
    public Client getNextClient(int currTime) {
        if (queue.isEmpty() || queue.get(0).getArrivalTime() > currTime || queue.get(0).getState() != State.STATE_WAIT) {
            return null;
        }
        Client nextClient = queue.get(0);
        operating.add(nextClient);
        return nextClient;
    }

    public void setClient(Client client, int index) {
        queue.set(index, client);
    }

    public void deleteClient(int index) {
        queue.remove(index);
    }

    public int getClientNum() {
        return queue.size();
    }
}
