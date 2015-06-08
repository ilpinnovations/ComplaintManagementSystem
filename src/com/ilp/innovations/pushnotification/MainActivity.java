package com.ilp.innovations.pushnotification;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.content.Intent;
 
public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(this);
        if(db.isFirstTimeOpening())
        {
        	Log.d("message", "First time opening");
        	Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        	startActivity(intent);
        	this.finish();
        } 
        else
        {
        	Log.d("message", "Already registered");
        	Intent intent = new Intent(MainActivity.this, ContentView.class);
        	startActivity(intent);
        	this.finish();
        }  
    }       
 
 
 
}