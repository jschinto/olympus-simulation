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
    /*Runs a tick of time for every client currently operating
      Any client that is marked as done is removed from the operating list entirely
      Clients not completed are themselves ticked, which reduces their timeLeft for the current activity.
     */
    public void runTick(String currTime) {
        for (int i=0; i < operating.size(); i++) {
            if (operating.get(i).getState() == State.STATE_DONE) {
                operating.remove(i);
                i--;
            }
            else if (operating.get(i).getProcedureRoom() != null) {
                operating.get(i).tick(currTime);
            }
        }
    }

    //Sorts the queue first based on their state, than their arrival time
    public void sortQueue() {
        Collections.sort(queue);
    }

    public ArrayList<Client> getQueue() {
        return queue;
    }

    //add at the end of the queue
    public void addClient(Client client) {
        queue.add(client);
        sortQueue();
    }

    //Returns a client based on the given index.
    public Client getClientByIndex(int index) {
        return queue.get(index);
    }

    public void addToOperating(Client c) {
        operating.add(c);
    }

    /*
        Returns the next client ready to be given a room.
        Ignores any clients that have either not arrived or are not actually in the waiting room
     */
    public Client getNextClient(int currTime, int offset) {
        if (queue.size() <= offset || queue.get(offset).getArrivalTime() > currTime || queue.get(offset).getState() != State.STATE_WAIT) {
            return null;
        }
        Client nextClient = queue.get(offset);
        //operating.add(nextClient);
        return nextClient;
    }

    //Sets the client at the given index to the given Client
    public void setClient(Client client, int index) {
        queue.set(index, client);
        sortQueue();
    }

    //Removes the client at the given index
    public void deleteClient(int index) {
        queue.remove(index);
    }

    //Returns the number of clients in the queue, including those who have not arrived or are operating.
    public int getClientNum() {
        return queue.size();
    }

    //Returns the client with the latest arrival time as a default for creating new clients
    public int getLatestTime() {
        if(queue.size() == 0)
        {
            return 0;
        }
        return queue.get(queue.size() - 1).getArrivalTime();
    }

    public void setIDs() {
        for(int i = 0; i < queue.size(); i++) {
            queue.get(i).setId(i + 1);
        }
    }

    public int removeClientsOutsideRange(int startBound, int endBound) {
        int count = 0;
        for(int i = 0; i < queue.size(); i++) {
            if(queue.get(i).getArrivalTime() > endBound || queue.get(i).getArrivalTime() < startBound) {
                queue.remove(i);
                i--;
                count++;
            }
        }
        setIDs();
        return count;
    }

    public Boolean isEverythingDone() {
        boolean done = true;
        for(Client c:queue) {
            done &= c.getState() == State.STATE_DONE;
        }

        return done;
    }
}
