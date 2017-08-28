package com.example.reminderexample;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
{
	
	private static final int PROJECTION_ID_INDEX = 0;
	
	Cursor cur = null;
	
	ContentResolver cr;
	
	int eventID=0;
	
	SwipeMenuListView listView;
	String[] list = {"One", "Two", "Three"};
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		
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
		
		
		listView.setAdapter( new SwipeListAdapter() );
		
		
		init();
		
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
		
		
		getAllReminders();
		
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
		getAllReminders();
	}
	
	
	void show()
	{
		
		
	}
	
	
	void edit()
	{
		
		
	}
	
	
	void delete()
	{
		
		
	}
	
	void getAllReminders()
	{
		Cursor cursor = null;
		final ContentResolver cr = this.getContentResolver();
		try
		{
			cursor = cr.query(Uri.parse(  getCalendarUriBase( true ).toString()+"events"), new String[]{"calendar_id", "title", "description", "dtstart", "dtend", "eventLocation", CalendarContract.Events._ID}, null, null, null );
			//Cursor cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{ "_id", "name" }, null, null, null);
			
			Log.i( "Sample Activity", "Cursor size " + cursor.getCount() );
			String add = null;
			cursor.moveToFirst();
			String[] CalNames = new String[ cursor.getCount() ];
			int[] CalIds = new int[ cursor.getCount() ];
			for ( int i = 0; i < CalNames.length; i++ )
			{
				CalIds[ i ] = cursor.getInt( 0 );
				CalNames[ i ] = "Event" + cursor.getInt( 0 ) + ": \nTitle: " + cursor.getString( 1 ) + "\nDescription: " +
						cursor.getString( 2 ) + "\nStart Date: " + new Date( cursor.getLong( 3 ) )
						+ "\nEnd Date : " + new Date( cursor.getLong( 4 ) ) + "\nLocation : " + cursor.getString( 5 );
				
				if ( eventID == cursor.getLong( 6 ) )
				{
					Toast.makeText( this, "Found event that was set"+  ": \nTitle: " + cursor.getString( 1 ) + "\nDescription: " +
							cursor.getString( 2 ), Toast.LENGTH_LONG ).show();
				}
					
				if ( add == null )
				{
					add = CalNames[ i ];
				}
				else
				{
					add += CalNames[ i ];
				}
				//        ((TextView)findViewById(R.id.calendars)).setText(add);
				Log.i( "SAmple Reminder****", "events from calendar " + add );
				cursor.moveToNext();
			}
			cursor.close();
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
		Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true).toString() + "events");
		ContentResolver cr = getContentResolver();
		TimeZone timeZone = TimeZone.getDefault();
		
		// Inserting an event in calendar.
		ContentValues values = new ContentValues();
		values.put( CalendarContract.Events.CALENDAR_ID, 1);
		values.put(CalendarContract.Events.TITLE, "Sample Reminder 01");
		values.put(CalendarContract.Events.DESCRIPTION, "A test Reminder.");
		values.put(CalendarContract.Events.ALL_DAY, 0);
		// event starts at 1 minutes from now
		values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis() + 1 * 60 * 1000);
		// ends 2 minutes from now
		values.put(CalendarContract.Events.DTEND, cal.getTimeInMillis() + 2 * 60 * 1000);
		values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		values.put(CalendarContract.Events.HAS_ALARM, 1);
		Uri event = cr.insert(EVENTS_URI, values);
		
		// Display event id.
		Toast.makeText(getApplicationContext(), "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT).show();
		
		eventID=Integer.parseInt( event.getLastPathSegment() );
		// Adding reminder for event added.
		Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
		values = new ContentValues();
		values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
		values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
		values.put(CalendarContract.Reminders.MINUTES, 10);
		cr.insert(REMINDERS_URI, values);
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
			return 3;
		}
		
		@Override
		public Object getItem( int position )
		{
			return list[ position ];
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
			
			textView.setText( list[ position ] );
			
			
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
			return false;
		}
	}
}
