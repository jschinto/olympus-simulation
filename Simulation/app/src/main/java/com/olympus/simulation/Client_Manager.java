package com.olympus.simulation;

import java.util.ArrayList;

public class Client_Manager {
    //the queue of clients entering and exiting the waiting room
    private ArrayList<Client> queue;
    //the list of clients currently assigned a procedure room
    private ArrayList<Client> operating;

    public Client_Manager() {
        queue = new ArrayList<Client>();
    }
    //run the operations for a tick of time
    public void runTick() {
        for (int i=0; i < operating.size(); i++) {
            if (operating.get(i).getTimeLeft() > 0) {
                operating.get(i).tick();
            }
        }
    }

    //add at the end of the queue
    public void addClient(Client client) {
        client.setState(State.STATE_WAIT);
        queue.add(client);
    }

    //remove at the beginning of the queue, change state and add to operating list
    public Client getNextClient() {
        if (queue.isEmpty()) {
            return null;
        }
        Client nextClient = queue.remove(0);
        nextClient.setState(State.STATE_TRAVEL);
        operating.add(nextClient);
        return nextClient;
    }
}
