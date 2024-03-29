package com.olympus.simulation;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/*
enumerated type for the different states that Clients can have during the simulation
 */
public class State implements Serializable {

    public int state;

    public static final String[] stateNames = {"Waiting", "Post Procedure", "Operating", "Traveling", "In Room", "Cleaning Scope", "Cleaning Room", "Reprocessing"};

    public static final int STATE_WAIT = 0;
    public static final int STATE_TRAVEL = 3;
    public static final int STATE_OPERATION = 2;
    public static final int STATE_DONE = 1;
    public static final int STATE_INROOM = 4;
    public static final int STATE_CLEANSCOPE = 5;
    public static final int STATE_CLEANROOM = 6;
    public static final int STATE_REPROC = 7;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_WAIT,
            STATE_OPERATION,
            STATE_DONE,
            STATE_TRAVEL,
            STATE_INROOM,
            STATE_CLEANSCOPE,
            STATE_CLEANROOM,
            STATE_REPROC
    })
    public @interface StateDef {
    }

    public State(@StateDef int state) {
        this.state = state;
    }

    public boolean equals(@StateDef int state) {
        return this.state == state;
    }

}