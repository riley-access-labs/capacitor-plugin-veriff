package com.csantosm.capacitor.veriff;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.veriff.Branding;
import com.veriff.Configuration;
import com.veriff.Sdk;

import org.json.JSONException;

public class VeriffPlugin {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }


}
