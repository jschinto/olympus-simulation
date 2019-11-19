package com.olympus.simulation;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class State_Scope implements Serializable {

    public int state;

    public static final String[] stateNames = {"Clean", "Cleaning", "In Use", "Clean", "Dirty", "Returning", "Waiting Reprocessing", "Waiting Reprocessing", "Reprocessing", "Clean", "Clean"};
    public static final String[] mappings = {"Cabinet", "Sink", "Room", "Hallway", "Sink", "Hallway", "Cabinet", "Hallway", "Reprocessor", "Reprocessor", "Room"};

    public static final int STATE_FREE = 0;
    public static final int STATE_TRAVEL = 3;
    public static final int STATE_USE = 2;
    public static final int STATE_DIRTY = 4;
    public static final int STATE_CLEANING = 1;
    public static final int STATE_DONE = 6;
    public static final int STATE_RETURNING = 5;
    public static final int STATE_TOREPROCESS = 7;
    public static final int STATE_REPROCESSING = 8;
    public static final int STATE_DONEREPROCESSING = 9;
    public static final int STATE_INROOM = 10;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_FREE,
            STATE_TRAVEL,
            STATE_USE,
            STATE_DIRTY,
            STATE_CLEANING,
            STATE_RETURNING,
            STATE_TOREPROCESS,
            STATE_REPROCESSING,
            STATE_DONEREPROCESSING,
            STATE_INROOM
    })
    public @interface StateDef {
    }

    public State_Scope(@StateDef int state) {
        this.state = state;
    }

    public boolean equals(@StateDef int state) {
        return this.state == state;
    }

}
