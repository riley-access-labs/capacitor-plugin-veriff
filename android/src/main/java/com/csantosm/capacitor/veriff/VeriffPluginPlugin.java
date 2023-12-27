package com.csantosm.capacitor.veriff;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.veriff.Branding;
import com.veriff.Configuration;
import com.veriff.Result;
import com.veriff.Sdk;
import org.json.JSONException;
import org.json.JSONObject;

@CapacitorPlugin(name = "VeriffPlugin")
public class VeriffPluginPlugin extends Plugin {

    private static final int REQUEST_CODE = 800;
    private static JSONObject veriffConfig = new JSONObject();

    @PluginMethod
    public void start(PluginCall call) {
        String sessionUrl = call.getString("sessionUrl");
        JSONObject config = call.getObject("configuration");
        Log.d("VeriffPlugin", sessionUrl);

        try {
            this.launchVeriffSDK(call, sessionUrl, config);
        } catch (JSONException e) {
            e.printStackTrace();
            call.reject(e.getStackTrace().toString());
        }
    }

    private void launchVeriffSDK(PluginCall call, String sessionUrl, JSONObject config) throws JSONException {
        Branding.Builder branding = new Branding.Builder();

        if (!config.isNull("primary")) {
            // Change the default theme color if it is not null
            String primary = config.getString("primary");
            String backgroundColor = config.getString("backgroundColor");
            String logo = config.getString("logo");
            branding.primary(Color.parseColor(primary));
            branding.background(Color.parseColor(backgroundColor));
        }

        Configuration configuration = new Configuration.Builder().branding(branding.build()).build();
        Intent intent = Sdk.createLaunchIntent(getActivity(), sessionUrl, configuration);
        intent.putExtra("requestCode", REQUEST_CODE);

        // https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
        startActivityForResult(call, intent, "onActivityResult");
    }

    @ActivityCallback
    public void onActivityResult(PluginCall call, ActivityResult activityResult) {
        Intent data = activityResult.getData();

        if (data != null) {
            Result veriffResult = Result.fromResultIntent(data);
            if (veriffResult != null) {
                try {
                    JSObject result = handleResult(veriffResult);
                    call.resolve(result);
                } catch (JSONException e) {
                    call.reject(e.getStackTrace().toString());
                }
            }
        } else {
            call.reject("Result after launch Veriff is null");
        }
    }

    public JSObject handleResult(Result result) throws JSONException {
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
        return resultJson;
    }
}
