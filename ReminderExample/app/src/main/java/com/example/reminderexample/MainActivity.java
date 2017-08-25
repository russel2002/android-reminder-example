package com.example.reminderexample;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity
{
	
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
