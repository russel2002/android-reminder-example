package com.example.reminderexample;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
{
	Gson gson;
	
	
	ArrayList<ReminderModel> reminders;
	
	ContentResolver cr;
	
	int calendarID = 1;
	
	String eventTitle = "";
	long eventID;
	
	SwipeMenuListView listView;
	
	
	String[] list = {"One", "Two", "Three"};
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		
		gson = new Gson();
		
		
		cr = getContentResolver();
		
		
		listView = (SwipeMenuListView) findViewById( R.id.listview );
		
		
		Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
		
		setSupportActionBar( toolbar );
		
		
		
		
		SwipeMenuCreator creator = new SwipeMenuCreator()
		{
			
			@Override
			public void create( SwipeMenu menu )
			{
				
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext() );
				
				// set item background
				deleteItem.setBackground( new ColorDrawable( Color.rgb( 0xF9,
						0x3F, 0x25 ) ) );
				// set item width
				int dp = 60;
				
				int px = Math.round( dp * ( getResources().getDisplayMetrics().densityDpi / 160 ) );
				
				deleteItem.setWidth( px );
				// set a icon
				deleteItem.setIcon( android.R.drawable.ic_menu_delete );
				// add to menu
				menu.addMenuItem( deleteItem );
			}
		};

// set creator
		listView.setMenuCreator( creator );
		
		listView.setOnMenuItemClickListener( new SwipeMenuListView.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick( int position, SwipeMenu menu, int index )
			{
				
				
				switch ( index )
				{
					case 0:
						
						delete( position );
						break;
					case 1:
						// delete
						break;
				}
				// false : close the menu; true : not close the menu
				return false;
			}
		} );
		
		//getAllReminders();
		
		
		//add();
		
		//listView.setAdapter( new SwipeListAdapter() );
		
		
		//init();
		tryFetchingData();
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		MenuInflater menuInflater = getMenuInflater();
		
		menuInflater.inflate( R.menu.reminder_menu, menu );
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle item selection
		switch ( item.getItemId() )
		{
			case R.id.action_add_reminder:
				add();
				return true;
			default:
				return super.onOptionsItemSelected( item );
		}
	}
	
	void init()
	{
		
		
		// Submit the query and get a Cursor object back.
		if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.READ_CALENDAR ) != PERMISSION_GRANTED )
		{
			ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_CALENDAR}, 223 );
			
		}
		else
		{
			prepareList();
		}
		
	}
	
	
	@Override
	public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults )
	{
		if ( requestCode == 223 )
		{
			
			if ( grantResults[ 0 ] == PERMISSION_GRANTED )
			{
				
			}
			
		}
		
		
		if ( requestCode == 224 )
		{
			
			if ( grantResults[ 0 ] == PERMISSION_GRANTED )
			{
				Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				Account[] accounts = AccountManager.get( this ).getAccounts();
				for ( Account account : accounts )
				{
					if ( emailPattern.matcher( account.name ).matches() )
					{
						String possibleEmail = account.name;
						Log.d( "ACCOUNTS", possibleEmail );
						
					}
				}
			}
			
		}
	}
	
	@SuppressWarnings("MissingPermission")
	void prepareList()
	{
		
		
		//	getAllReminders();
		
	/*	String[] EVENT_PROJECTION = new String[]{
				CalendarContract.Calendars._ID};
		
		
		
		Uri uri = CalendarContract.Calendars.CONTENT_URI;
		String selection =   CalendarContract.Calendars.IS_PRIMARY + " = ?";
		String[] selectionArgs = new String[]{"1"};
		
		cur = cr.query( uri, EVENT_PROJECTION, selection, selectionArgs, null );
		
		long calID = 0;
	
		
		// Get the field values
		calID = cur.getLong(PROJECTION_ID_INDEX);
		
		
		Log.d( "Calendar", "prepareList: calendar id"+calID );
	*/
		
	}
	
	
	private void add()
	{
		addReminderInCalendar();
		//getAllReminders();
		tryFetchingData();
	}
	
	
	void show()
	{
		
		
	}
	
	
	void edit()
	{
		
		
	}
	
	
	void delete( int position )
	{
		if ( reminders.size() > 0 && reminders.size() <= position )
		{
			int iNumRowsDeleted = 0;
			
			eventID = reminders.get( position ).EventID;
			
			Uri eventsUri = Uri.parse( getCalendarUriBase( true ) + "events" );
			Uri eventUri = ContentUris.withAppendedId( eventsUri, eventID );
			iNumRowsDeleted = getContentResolver().delete( eventUri, null, null );
			
			Toast.makeText( this, "Deleted " + iNumRowsDeleted + " calendar entry.", Toast.LENGTH_SHORT ).show();
			
			
		}
		
		
		reminders.remove( position );
		
		
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences( getPackageName(), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		
		String json = gson.toJson( reminders.toArray( new ReminderModel[ 0 ] ) );
		
		
		editor.putString( "data", json );
		
		editor.commit();
		
		listView.setAdapter( new SwipeListAdapter() );
		
		
		//return iNumRowsDeleted;
		
	}
	
	void getAllReminders()
	{
		Cursor cursor = null;
		final ContentResolver cr = this.getContentResolver();
		try
		{
			cursor = cr.query( Uri.parse( getCalendarUriBase( true ).toString() + "events" ), new String[]{"calendar_id", "title", "description", "dtstart", "dtend", "eventLocation", CalendarContract.Events._ID}, null, null, null );
			//Cursor cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{ "_id", "name" }, null, null, null);
			
			Log.d( "Sample Activity", "Cursor size " + cursor.getCount() );
			
			cursor.moveToFirst();
			String[] CalNames = new String[ cursor.getCount() ];
			list = new String[ cursor.getCount() ];
			
			int[] CalIds = new int[ cursor.getCount() ];
			
			calendarID = cursor.getInt( 0 );
			for ( int i = 0; i < CalNames.length; i++ )
			{
				
				
				CalIds[ i ] = cursor.getInt( 0 );
				CalNames[ i ] = " \nTitle: " + cursor.getString( 1 );
				
				if ( eventTitle.equals( CalNames[ i ] ) )
				{
					Toast.makeText( this, "Found event that was set" + ": \nTitle: " + cursor.getString( 1 ), Toast.LENGTH_LONG ).show();
				}
				
				
				list[ i ] = CalNames[ i ];
				
				
				cursor.moveToNext();
			}
			cursor.close();
			
			//listView.setAdapter( null );
			listView.setAdapter( new SwipeListAdapter() );
			
			
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		if ( cursor != null && !cursor.isClosed() )
		{
			cursor.close();
		}
		
	}
	
	
	/**
	 * Adds Events and Reminders in Calendar.
	 */
	private void addReminderInCalendar()
	{
		Calendar cal = Calendar.getInstance();
		Uri EVENTS_URI = Uri.parse( getCalendarUriBase( true ).toString() + "events" );
		ContentResolver cr = getContentResolver();
		TimeZone timeZone = TimeZone.getDefault();
		
		
		ReminderModel reminderModel = new ReminderModel();
		reminderModel.ECalendarID = calendarID;
		reminderModel.ETitle = "Sample Reminder with ID ";
		reminderModel.EDescription = "A test Reminder.";
		reminderModel.EAllDay = 0;
		reminderModel.DTSTART = cal.getTimeInMillis() + 2 * 60 * 1000;
		reminderModel.DTEND = cal.getTimeInMillis() + 10 * 60 * 1000;
		reminderModel.ETimeZone = timeZone.getID();
		reminderModel.HAS_ALARM = 1;
		
		// Inserting an event in calendar.
		ContentValues values = new ContentValues();
		values.put( CalendarContract.Events.CALENDAR_ID, calendarID );
		values.put( CalendarContract.Events.TITLE, reminderModel.ETitle );
		values.put( CalendarContract.Events.DESCRIPTION, reminderModel.EDescription );
		values.put( CalendarContract.Events.ALL_DAY, reminderModel.EAllDay );
		// event starts at 1 minutes from now
		values.put( CalendarContract.Events.DTSTART, reminderModel.DTSTART );
		// ends 2 minutes from now
		values.put( CalendarContract.Events.DTEND, reminderModel.DTEND );
		values.put( CalendarContract.Events.EVENT_TIMEZONE, reminderModel.ETimeZone );
		values.put( CalendarContract.Events.HAS_ALARM, reminderModel.HAS_ALARM );
		//values.put( CalendarContract.Events.RRULE,CalendarContract.Events.  );
		Uri event = cr.insert( EVENTS_URI, values );
		
		// Display event id.
		Toast.makeText( getApplicationContext(), "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT ).show();
		
		eventTitle = "Sample Reminder with ID ";
		// Adding reminder for event added.
		eventID = Long.parseLong( event.getLastPathSegment() );
		
		reminderModel.EventID = eventID;
		reminderModel.MEHTOD = CalendarContract.Reminders.METHOD_ALERT;
		reminderModel.MINUTES = 1;
		Uri REMINDERS_URI = Uri.parse( getCalendarUriBase( true ) + "reminders" );
		values = new ContentValues();
		values.put( CalendarContract.Reminders.EVENT_ID, eventID );
		values.put( CalendarContract.Reminders.METHOD, reminderModel.MEHTOD );
		values.put( CalendarContract.Reminders.MINUTES, reminderModel.MINUTES );
		Uri res = cr.insert( REMINDERS_URI, values );
		
		long reminderID = Long.parseLong( res.getLastPathSegment() );
		
		if ( reminderID > 0 )
		{
			
			saveReminder( reminderModel );
			
		}
		
		
		tryFetchingData();
		
	}
	
	/**
	 * Returns Calendar Base URI, supports both new and old OS.
	 */
	private Uri getCalendarUriBase( boolean eventUri )
	{
		Uri calendarURI = null;
		try
		{
			if ( android.os.Build.VERSION.SDK_INT <= 7 )
			{
				calendarURI = ( eventUri ) ? Uri.parse( "content://calendar/" ) : Uri.parse( "content://calendar/calendars" );
			}
			else
			{
				calendarURI = ( eventUri ) ? Uri.parse( "content://com.android.calendar/" ) : Uri
						.parse( "content://com.android.calendar/calendars" );
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		return calendarURI;
	}
	
	
	void tryFetchingData()
	{
		
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences( getPackageName(), Context.MODE_PRIVATE );
		
		
		String json = sharedPref.getString( "data", null );
		
		if ( json != null )
		{
			
			ReminderModel[] reminders = gson.fromJson( json, ReminderModel[].class );
			
			
			
			this.reminders = new ArrayList<>( Arrays.asList( reminders ) );
			
			
			if ( reminders.length > 0 )
				
				listView.setAdapter( new SwipeListAdapter() );
			else
			{
				Toast.makeText( this, "No reminders added yet", Toast.LENGTH_SHORT ).show();
			}
			
		}
		else
		{
			Toast.makeText( context, "No reminders added yet.", Toast.LENGTH_SHORT ).show();
			
			
		}
		
		
	}
	
	
	void saveReminder( ReminderModel reminderModel )
	{
		
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences( getPackageName(), Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = sharedPref.edit();
		
		
		String savedJson = sharedPref.getString( "data", null );
		
		if ( savedJson != null )
		{
			
			ReminderModel[] savedReminders = gson.fromJson( savedJson, ReminderModel[].class );
			
			ReminderModel[] tempReminders = new ReminderModel[ savedReminders.length + 1 ];
			
			for ( int i = 0; i < savedReminders.length; i++ )
			{
				
				tempReminders[ i ] = savedReminders[ i ];
				
			}
			
			tempReminders[ savedReminders.length ] = reminderModel;
			
			String json = gson.toJson( tempReminders );
			
			Log.d( "Saved_Reminders", json );
			
			
			editor.putString( "data", json );
			
		}
		else
		{
			ReminderModel[] remindersModel = new ReminderModel[ 1 ];
			remindersModel[ 0 ] = reminderModel;
			String json = gson.toJson( remindersModel );
			
			Log.d( "Saved_Reminders", json );
			
			
			editor.putString( "data", json );
			
			
		}
		
		
		editor.commit();
		
		
	}
	
	
	public class SwipeListAdapter implements ListAdapter
	{
		
		
		@Override
		public boolean areAllItemsEnabled()
		{
			return true;
		}
		
		@Override
		public boolean isEnabled( int position )
		{
			return true;
		}
		
		@Override
		public void registerDataSetObserver( DataSetObserver observer )
		{
			
		}
		
		@Override
		public void unregisterDataSetObserver( DataSetObserver observer )
		{
			
		}
		
		@Override
		public int getCount()
		{
			return reminders.size();
		}
		
		@Override
		public Object getItem( int position )
		{
			return reminders.get( position ).ETitle;
		}
		
		@Override
		public long getItemId( int position )
		{
			return position;
		}
		
		@Override
		public boolean hasStableIds()
		{
			return false;
		}
		
		@Override
		public View getView( int position, View convertView, ViewGroup parent )
		{
			LayoutInflater inflater = LayoutInflater.from( MainActivity.this );
			
			
			convertView = inflater.inflate( R.layout.list_item, parent, false );
			
			
			TextView textView = (TextView) convertView.findViewById( R.id.item_text );
			
			textView.setText( reminders.get( position ).ETitle + " : " + reminders.get( position ).EventID );
			
			
			return convertView;
		}
		
		@Override
		public int getItemViewType( int position )
		{
			return 0;
		}
		
		@Override
		public int getViewTypeCount()
		{
			return 1;
		}
		
		@Override
		public boolean isEmpty()
		{
			return list.length > 0;
		}
	}
}
