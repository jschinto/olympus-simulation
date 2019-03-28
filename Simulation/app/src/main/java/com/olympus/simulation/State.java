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

    public static final int STATE_WAIT = 2;
    public static final int STATE_TRAVEL = 0;
    public static final int STATE_OPERATION = 1;
    public static final int STATE_DONE = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_WAIT,
            STATE_OPERATION,
            STATE_DONE,
            STATE_TRAVEL
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