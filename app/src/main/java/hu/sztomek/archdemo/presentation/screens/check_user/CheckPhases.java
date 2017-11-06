package hu.sztomek.archdemo.presentation.screens.check_user;

import android.support.annotation.IntDef;

final class CheckPhases {

    public static final int STATE_START = 1;
    public static final int STATE_EXISTS = 2; // ok, can proceed
    public static final int STATE_DOESNT_EXISTS = 3;
    public static final int STATE_REGISTERING_IN_DB = 4;
    public static final int STATE_REGISTERED = 5; // ok, can proceed

    @IntDef({STATE_START, STATE_EXISTS, STATE_DOESNT_EXISTS, STATE_REGISTERING_IN_DB, STATE_REGISTERED})
    public @interface Phase {}


    private CheckPhases() {}

}
