package com.olympus.simulation;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class State_Scope implements Serializable {

    public int state;

    public static final String[] stateNames = {"Clean", "Dirty", "In Use", "Traveling"};

    public static final int STATE_FREE = 0;
    public static final int STATE_TRAVEL = 3;
    public static final int STATE_USE = 2;
    public static final int STATE_DIRTY = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            STATE_FREE,
            STATE_TRAVEL,
            STATE_USE,
            STATE_DIRTY
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
