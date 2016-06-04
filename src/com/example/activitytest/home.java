package com.example.activitytest;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class home extends Activity{

	@Override 
	protected void onCreate(Bundle savedInstanceState){
		Log.e("", "home is called");
		finish();
	}
}
