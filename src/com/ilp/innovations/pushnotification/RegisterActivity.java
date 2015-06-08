package com.ilp.innovations.pushnotification;

import static com.ilp.innovations.pushnotification.CommonUtilities.SENDER_ID;
import static com.ilp.innovations.pushnotification.CommonUtilities.SERVER_URL;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
public class RegisterActivity extends Activity {
    // alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();
     
    // Internet detector
    ConnectionDetector cd;
     
    // UI elements
    EditText txtName;
    EditText txtEmail;
    EditText txtEmpId;
    EditText txtContact;
     
    // Register button
    Button btnRegister;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         
        cd = new ConnectionDetector(getApplicationContext());
 
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(RegisterActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
 
        // Check if GCM configuration is set
        if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
                || SENDER_ID.length() == 0) {
            // GCM sernder id / server url is missing
            alert.showAlertDialog(RegisterActivity.this, "Configuration Error!",
                    "Please set your Server URL and GCM Sender ID", false);
            // stop executing code by return
             return;
        }
         
        txtName = (EditText) findViewById(R.id.name);
        txtEmail = (EditText) findViewById(R.id.email);
        txtContact = (EditText) findViewById(R.id.contact);
        txtEmpId = (EditText) findViewById(R.id.empId);
        btnRegister = (Button) findViewById(R.id.btnRegister);
         
        /*
         * Click event on Register button
         * */
        btnRegister.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View arg0) {
                // Read EditText dat
                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String contact = txtContact.getText().toString();
                String empId = txtEmpId.getText().toString();
                
                if(empId.trim().length()==0)
                	alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter valid Employee ID", false);
                else if(name.trim().length()==0)
                	alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter valid Name", false);
                else if(email.trim().length()==0  || !validateEmail(email))
                	alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter valid Email", false);
                else if(contact.trim().length()==0)
                	alert.showAlertDialog(RegisterActivity.this, "Registration Error!", "Please enter valid Phone Number", false);
                else {
                    // Launch Main Activity
                    Intent i = new Intent(getApplicationContext(), RegisterDevice.class);
                    
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    i.putExtra("contact", contact);
                    i.putExtra("empId", empId);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
    
    private static boolean validateEmail(String email) {
    	String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    	return email.matches(EMAIL_REGEX);
    }

 
}
