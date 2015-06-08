package com.ilp.innovations.pushnotification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "NotificationManager";
	private static final String TABLE_NOTIFICATIONS = "Notification";
	
	private static final String NOTIFICATION_ID = "ID";
	private static final String NOTIFIER_NAME = "Name";
	private static final String EMPLOYEE_ID = "EmployeeID";
	private static final String CATEGORY = "Category";
	private static final String CONTACT = "Contact";
	private static final String MESSAGE = "Message";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("message", "Database intitialized");
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	public void createTables(SQLiteDatabase db) {
		
		String sqlNotification = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATIONS + "("
				+ NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NOTIFIER_NAME + " TEXT," + EMPLOYEE_ID + " TEXT,"
                + CATEGORY + " TEXT,"+ CONTACT + " TEXT,"
                + MESSAGE + " TEXT)";
		db.execSQL(sqlNotification);
		Log.d("message", "Notification table created....");
	}
	 
	public List<Notification> getAllNotifications() {
		List<Notification> notifications = new ArrayList<Notification>();
		SQLiteDatabase db = this.getReadableDatabase();
		Log.d("message", "Querying data....");
		try{
			Cursor cursor = db.query(TABLE_NOTIFICATIONS, new String[] {NOTIFICATION_ID,NOTIFIER_NAME,EMPLOYEE_ID,CATEGORY,CONTACT,MESSAGE},
					null, null, null, null, NOTIFICATION_ID +" DESC");
			Log.d("message", "Query completed..");
			if(cursor!=null)
			{
				cursor.moveToFirst();
				do
				{
					Notification notification = new Notification(cursor.getInt(0),cursor.getString(1),
							cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
					notifications.add(notification);
					Log.d("4mgetAllNotifications", notification.getNotifierName());
				} while(cursor.moveToNext());
				Log.d("message", "Data extraction completed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notifications;
	}
	
	public boolean addNotification(Notification newNotification) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d("message", "Data insertion started");
	    if(newNotification!=null) {
		        	ContentValues values = new ContentValues();
			        values.put(NOTIFIER_NAME, newNotification.getNotifierName());
			        values.put(EMPLOYEE_ID, newNotification.getNotifierEmpId());
			        values.put(CATEGORY, newNotification.getCategory());
			        values.put(CONTACT, newNotification.getContact());
			        values.put(MESSAGE, newNotification.getMessage());
			        db.insert(TABLE_NOTIFICATIONS, null, values);
			        db.close();
			        Log.d("message", "Data insertion completed");
			        return true;
	        	}
		else
			return false;

	}
	
	public boolean deleteNotification(Notification notification) {
		SQLiteDatabase db = this.getWritableDatabase();
		if(notification!=null)
		{
			db.delete(TABLE_NOTIFICATIONS, NOTIFICATION_ID + " =?", 
					new String[]{String.valueOf(notification.getNotificationId())});
			db.close();
			return true;
		}
		else
			return false;
	}
	
	public String getPhoneNum(int id) {
		String phNo=null;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NOTIFICATIONS, new String[] {CONTACT},
				NOTIFICATION_ID +"=?", new String[]{String.valueOf(id)}, null, null, null);
		if(cursor!=null)
		{
			cursor.moveToFirst();
			phNo = cursor.getString(0);
		}
		return phNo;
	}
	
	public void register()
	{
		File file = new File("/data/data/com.ilp.innovations.pushnotification/reg.dat");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isFirstTimeOpening()
	{
		File file = new File("/data/data/com.ilp.innovations.pushnotification/reg.dat");
		return !file.exists();
	}
	
}
