package com.example.activitytest;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class Myservice extends Service{
	
	Receiver r;
	
	IntentFilter f;
	
	
	
	class Receiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			/*
			
			
			KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
			
			keyguardLock.reenableKeyguard();
			
			keyguardLock.disableKeyguard();//解锁系统锁屏
			Intent l = new Intent(context, FirstActivity.class);
			Toast.makeText(Myservice.this, "lock on...", 
					Toast.LENGTH_SHORT).show();
			startActivity(l);//跳转到主界面
*/
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(Myservice.this, "onBind...", 
				Toast.LENGTH_SHORT).show();
		return null;
	}

	@Override
	public void onCreate(){

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		//Toast.makeText(Myservice.this, "onStartCommand...", 
	//			Toast.LENGTH_SHORT).show();
		
		f=new IntentFilter("Intent.ACTION_SCREEN_ON");
		f.addAction(Intent.ACTION_SCREEN_ON);
		
		r = new Receiver(){
			@Override
			public void onReceive(Context context, Intent intent){
				KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
				KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
				
				keyguardLock.reenableKeyguard();
				
				keyguardLock.disableKeyguard();//解锁系统锁屏
				Toast.makeText(Myservice.this, "unlock...", 
						Toast.LENGTH_SHORT).show();
				Intent activity = new Intent(context, SecondActivity.class);
				activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(activity);
//				context.startActivity(activity);
			}
		};
		registerReceiver(r, f);
		
		Intent toMainIntent = new Intent(Myservice.this, SecondActivity.class);
		toMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		
		
		return super.onStartCommand(intent, flags, startId);
		
		
		
	}
	
	@Override
	public void onDestroy(){
		if(FirstActivity.t ==true)
		{
			super.onDestroy();
			return;
		}
		
		Toast.makeText(Myservice.this, "onDestroy...", 
				Toast.LENGTH_SHORT).show();
		super.onDestroy();
		unregisterReceiver(new Receiver());//?
	    //重启此服务
	    startActivity(new Intent(Myservice.this,Myservice.class));
	}
	
	
}
