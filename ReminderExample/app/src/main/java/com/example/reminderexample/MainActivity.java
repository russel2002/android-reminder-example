package com.example.reminderexample;

import android.Manifest;
import android.content.ContentResolver;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity
{
	
	private static final int PROJECTION_ID_INDEX = 0;
	
	Cursor cur = null;
	
	ContentResolver cr = getContentResolver();
	
	SwipeMenuListView listView;
	String[] list = {"One", "Two", "Three"};
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
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
		if(requestCode==223)
		{
			
			if ( grantResults[ 0 ] == PERMISSION_GRANTED)
			{
				
			}
			
		}
	}
	
	@SuppressWarnings( "MissingPermission" )
	void prepareList()
	{
		
		String[] EVENT_PROJECTION = new String[]{
				CalendarContract.Calendars._ID};
		
		
		
		Uri uri = CalendarContract.Calendars.CONTENT_URI;
		String selection = "((" + CalendarContract.Calendars.IS_PRIMARY + " = ?";
		String[] selectionArgs = new String[]{"1"};
		
		cur = cr.query( uri, EVENT_PROJECTION, selection, selectionArgs, null );
		
		long calID = 0;
	
		
		// Get the field values
		calID = cur.getLong(PROJECTION_ID_INDEX);
	
		
	}
	
	
	private void add()
	{
		
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
