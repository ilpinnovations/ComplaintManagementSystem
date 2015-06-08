package com.ilp.innovations.pushnotification;


import static com.ilp.innovations.pushnotification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.ilp.innovations.pushnotification.CommonUtilities.EXTRA_MESSAGE;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.google.android.gcm.GCMRegistrar;

public class ContentView extends Activity{
	AlertDialogManager alert = new AlertDialogManager();
	private DatabaseHandler db = new DatabaseHandler(this);
	private List<Notification> mAppList = new ArrayList<Notification>();
	private AppAdapter mAdapter;
	private SwipeMenuListView mListView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	    mListView = (SwipeMenuListView) findViewById(R.id.listView);
		mAppList = db.getAllNotifications();
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
	            DISPLAY_MESSAGE_ACTION));
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);
		SwipeMenuCreator creator = new SwipeMenuCreator() {
	
		    @Override
		    public void create(SwipeMenu menu) {
		        // create "open" item
		        SwipeMenuItem callItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        callItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
		                0xCE)));
		        // set item width
		        callItem.setWidth(dp2px(90));
		        // set item title
		        callItem.setTitle("Call");
				// set item title fontsize
				callItem.setTitleSize(18);
				// set item title font color
				callItem.setTitleColor(Color.GREEN);
		        // add to menu
		        menu.addMenuItem(callItem);
	
		        // create "delete" item
		        SwipeMenuItem deleteItem = new SwipeMenuItem(
		                getApplicationContext());
		        // set item background
		        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
		                0x3F, 0x25)));
		        // set item width
		        deleteItem.setWidth(dp2px(90));
		        // set a icon
		        deleteItem.setIcon(R.drawable.ic_delete);
		        // add to menu
		        menu.addMenuItem(deleteItem);
		    }  
		};
	
		// set creator
		mListView.setMenuCreator(creator);
	    
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						Intent callIntent = new Intent(Intent.ACTION_CALL);
						String phNo = db.getPhoneNum(mAppList.get(position).getNotificationId());
						if(phNo!=null)
						{
							callIntent.setData(Uri.parse("tel:"+phNo));
							startActivity(callIntent);
						}
						break; 
					case 1:
						//delete item here
						db.deleteNotification(mAppList.get(position));
						mAppList.remove(position);
						mAdapter.notifyDataSetChanged();
						mListView.setAdapter(mAdapter);
						break;
				}
				return false;
			}
		});
				
		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {
					
			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}
					
			@Override
			public void onSwipeEnd(int position) {
			}
		});
		
		// test item click
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				
				alert.showAlertDialog(ContentView.this,
	                    mAppList.get(position).getCategory(),
	                    mAppList.get(position).getMessage(), false);
				return;
			}
		});
		
				
		// test item long click
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
	
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
				Toast.makeText(getApplicationContext(), position + " long click", 0).show();
				return false;
			}
		});
	}
    
    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }
	
	 class AppAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return mAppList.size();
			}

			@Override
			public Notification getItem(int position) {
				return mAppList.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = View.inflate(getApplicationContext(),
							R.layout.item_list_app, null);
					new ViewHolder(convertView);
				}
				ViewHolder holder = (ViewHolder) convertView.getTag();
				Notification item = getItem(position);
				holder.category.setText(item.getCategory());
				holder.message.setText(item.getMessage());
				holder.details.setText("EmpId:"+item.getNotifierEmpId()+" Name:"+item.getNotifierName()
						+" Contact:"+item.getContact());
				return convertView;
			}

			class ViewHolder {
				TextView category;
				TextView message;
				TextView details;

				public ViewHolder(View view) {
					category = (TextView) view.findViewById(R.id.category);
					message = (TextView) view.findViewById(R.id.message);
					details = (TextView) view.findViewById(R.id.details);
					view.setTag(this);
				}
			}
		}
	 
	   

		private int dp2px(int dp) {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
					getResources().getDisplayMetrics());
		}
		
	    /**
	     * Receiving push messages
	     * */
	    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
	            // Waking up mobile if it is sleeping
	            WakeLocker.acquire(getApplicationContext());
	            try{
		            String[] contents = newMessage.split(":");
		            Notification newNotification = new Notification(0, contents[0], contents[1],
		            		contents[2], contents[3], contents[4]);
		            mAppList = db.getAllNotifications();
		            mAdapter.notifyDataSetChanged();
		            Toast.makeText(getApplicationContext(), "New Message from " + newNotification.getNotifierName(), Toast.LENGTH_LONG).show();
	            } catch(Exception e) {
	            	e.printStackTrace();
	            	Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();
	            }
	            
	             
	            // Releasing wake lock
	            WakeLocker.release();
	        }
	    };
}
