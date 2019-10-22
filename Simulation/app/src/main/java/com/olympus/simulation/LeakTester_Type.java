package com.olympus.simulation;

import java.io.Serializable;

public class LeakTester_Type extends Element implements Serializable {

    //TODO: Ensure that requiredAttentionTime <= timeToComplete

    String name;
    int timeToComplete;
    int requiredAttentionTime;
    int price;

    public LeakTester_Type (String name, int timeToComplete, int requiredAttentionTime, int price){
        this.element = Element.ELEMENT_LEAKTESTERTYPE;
        this.name = name;
        this.timeToComplete = timeToComplete;
        this.requiredAttentionTime = requiredAttentionTime;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeToComplete() {
        return timeToComplete;
    }

    public void setTimeToComplete(int timeToComplete) {
        this.timeToComplete = timeToComplete;
    }

    public int getRequiredAttentionTime() {
        return requiredAttentionTime;
    }

    public void setRequiredAttentionTime(int requiredAttentionTime) {
        this.requiredAttentionTime = requiredAttentionTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean validate(){
        if(name != null && !this.name.equals("") && this.timeToComplete > 0 && this.requiredAttentionTime > 0 && this.price > 0 && this.timeToComplete >= this.requiredAttentionTime){
            return true;
        }
        return false;
    }
}
