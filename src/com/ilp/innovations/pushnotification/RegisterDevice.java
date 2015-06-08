package com.ilp.innovations.pushnotification;

import static com.ilp.innovations.pushnotification.CommonUtilities.SENDER_ID;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterDevice extends Activity{

    // label to display gcm messages
    TextView headLine;
     
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
     
    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
     
    // Connection detector
    ConnectionDetector cd;
     
    public static String name;
    public static String email;
    public static String contact;
    public static String empId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(RegisterDevice.this);
        
        cd = new ConnectionDetector(getApplicationContext());
        
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(RegisterDevice.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        
        // Getting name, email from intent
        Intent i = getIntent();
         
        name = i.getStringExtra("name");
        email = i.getStringExtra("email"); 
        contact = i.getStringExtra("contact");
        empId = i.getStringExtra("empId");
        
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        Log.d("GCM", "Manifest checking completed");
        
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        Log.d("GCM", "Dependency checking completed!");
        
        
        headLine = (TextView) findViewById(R.id.headLine);
         
        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);
 
        // Check if regid already presents
        if (regId.equals("")) {
        	
        	Log.d("message", "Registration begins..");
            // Registration is not present, register now with GCM           
            GCMRegistrar.register(this, SENDER_ID);
            Log.d("message", "Registration completed");
            db.register();
            Intent intent = new Intent(RegisterDevice.this,ContentView.class);
            startActivity(intent);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.              
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
                db.register();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
 
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, empId, name, email, contact, regId);
                        
                        return null;
                    }
 
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
 
                };
                mRegisterTask.execute(null, null, null);
                db.register();
            }
        }
    }

}
