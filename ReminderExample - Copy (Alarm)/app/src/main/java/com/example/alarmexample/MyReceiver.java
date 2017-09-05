package com.example.alarmexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.widget.Toast;

import com.google.gson.Gson;

public class MyReceiver extends BroadcastReceiver
{
	
	
	@Override
	public void onReceive( Context context, Intent intent )
	{
		PowerManager pm = (PowerManager) context.getSystemService( Context.POWER_SERVICE );
		PowerManager.WakeLock wl = pm.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, "" );
		wl.acquire();
		
		// Put here YOUR code.
		Toast.makeText( context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG ).show(); // For example
		
		
		SharedPreferences sharedPref = context.getSharedPreferences( context.getPackageName(), Context.MODE_PRIVATE );
		
		
		String json = sharedPref.getString( "NEW_DATA_KEY", null );
		
		int alarmID = intent.getIntExtra( "id", 0 );
		
		
		AlarmModel[] alarmModels = new Gson().fromJson( json, AlarmModel[].class );
		
		int i;
		
		for ( i = 0; i < alarmModels.length; i++ )
		{
			
			
			if ( alarmModels[ i ].alarmID == alarmID )
			{
				break;
			}
			
		}
		
		
		if ( i < alarmModels.length )
		{
			Toast.makeText( context, "Alarm is here! " + alarmModels[ i ].alarmDescription, Toast.LENGTH_LONG ).show();
		}
		
		wl.release();
	}
}
