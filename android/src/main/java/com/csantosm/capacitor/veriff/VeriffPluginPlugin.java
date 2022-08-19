package com.csantosm.capacitor.veriff;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;


import com.veriff.Branding;
import com.veriff.Configuration;
import com.veriff.Sdk;
import com.veriff.Result;

import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "VeriffPlugin")
public class VeriffPluginPlugin extends Plugin {

    private static final int REQUEST_CODE = 800;
    private static JSONObject veriffConfig = new JSONObject();
    private VeriffPlugin implementation = new VeriffPlugin();

    private static PluginCall callback;

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void start(PluginCall call) {
        callback = call;
        String sessionUrl = call.getString("sessionUrl");
        this.launchVeriffSDK(sessionUrl);
    }

    private void launchVeriffSDK(String sessionUrl) {
        Branding.Builder branding = new Branding.Builder();

        if (veriffConfig.length() > 0) {
            try {
                branding.themeColor(Color.parseColor(veriffConfig.getString("themeColor")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Configuration configuration = new Configuration.Builder()
                .branding(branding.build())
                .build();
        Intent intent = Sdk.createLaunchIntent(getActivity(), sessionUrl, configuration);

        // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        startActivityForResult(callback, intent, REQUEST_CODE);
    }

    @ActivityCallback
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && data != null) {
            Result result = Result.fromResultIntent(data);
            if (result != null) {
                try {
                    handleResult(result);
                } catch (JSONException e) {
                    callback.reject(e.getStackTrace().toString());
                }
            }
        }
    }

    public void handleResult(Result result) throws JSONException {
        JSObject resultJson = new JSObject();
        Result.Status status = result.getStatus();
        resultJson.put("status", status.toString());
        Log.d("Handle VeriffSDK result", status.toString());
        switch (status) {
            case CANCELED:
                // User canceled the session.
                resultJson.put("message", "User canceled the verification process");
                break;
            case ERROR:
                // An error occurred during the flow, Veriff has already shown UI, no need to display
                // a separate error message here
                resultJson.put("message", result.getError());
                break;
            case DONE:
                // Session is completed from clients perspective.
                resultJson.put("message", "Session is completed from clients perspective");
                break;
            default:
                resultJson.put("message", "Unknown result state");
                break;
        }
        callback.resolve(resultJson);
    }


}
