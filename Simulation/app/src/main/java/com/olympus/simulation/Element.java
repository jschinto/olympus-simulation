package com.olympus.simulation;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Element implements Serializable {
    public int element;

    public Element destination;
    public int order;

    public static final int ELEMENT_CLIENT = 0;
    public static final int ELEMENT_PROCEDURE = 1;
    public static final int ELEMENT_PROCEDUREROOM = 2;
    public static final int ELEMENT_SCOPE = 3;
    public static final int ELEMENT_SCOPETYPE = 4;
    public static final int ELEMENT_LEAKTESTERTYPE = 5;
    public static final int ELEMENT_TOWERTYPE = 6;
    public static final int ELEMENT_NURSE = 7;
    public static final int ELEMENT_DOCTOR = 8;
    public static final int ELEMENT_SINK = 9;
    public static final int ELEMENT_TECHNICIAN = 10;
    public static final int ELEMENT_REPROCESSORTYPE = 11;
    public static final int ELEMENT_REPROCESSOR = 12;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            ELEMENT_CLIENT,
            ELEMENT_PROCEDURE,
            ELEMENT_PROCEDUREROOM,
            ELEMENT_SCOPE,
            ELEMENT_SCOPETYPE,
            ELEMENT_LEAKTESTERTYPE,
            ELEMENT_TOWERTYPE,
            ELEMENT_NURSE,
            ELEMENT_DOCTOR,
            ELEMENT_SINK,
            ELEMENT_TECHNICIAN,
            ELEMENT_REPROCESSORTYPE,
            ELEMENT_REPROCESSOR
    })
    public @interface ElementDef {
    }

    public Element() {

    }
    public Element(@Element.ElementDef int element) {
        this.element = element;
    }

    public boolean equals(@Element.ElementDef int element) {
        return this.element == element;
    }

    public void setDestination(Element destination) {
        this.destination = destination;
    }

    public String getDestinationName() {
        if(destination == null) {
            return "Waiting Room";
        }
        if(destination instanceof ProcedureRoom) {
            return "Room " + ((ProcedureRoom) destination).getId();
        }
        if(destination instanceof ManualCleaningStation) {
            return "Sink " + ((ManualCleaningStation) destination).getId();
        }
        if(destination instanceof Reprocessor) {
            return  "Reprocessor " + ((Reprocessor) destination).getId();
        }
        return "";
    }

    public Element getDestination() {
        return destination;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public int getOrder() {
        return order;
    }
}
