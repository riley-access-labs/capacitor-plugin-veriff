package com.csantosm.capacitor.veriff;

import android.util.Log;

public class VeriffPlugin {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
