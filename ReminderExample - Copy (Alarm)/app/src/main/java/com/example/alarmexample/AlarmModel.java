package com.example.alarmexample;

/**
 * Created by tzia on 05-Sep-17.
 */

public class AlarmModel
{
	
	public int alarmID;
	public String alarmDescription;
	
	public AlarmModel()
	{
	}
	
	
	public AlarmModel( int alarmID, String alarmDescription )
	{
		this.alarmID = alarmID;
		this.alarmDescription = alarmDescription;
	}
}
