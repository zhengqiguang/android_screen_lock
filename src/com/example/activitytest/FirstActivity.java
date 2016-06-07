package com.example.activitytest;




import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class FirstActivity extends Activity{
	private EditText setEdit_chpassword;
	private Button saveButton_chpassword;
	static boolean t=false;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first_layout);
		
		


		

		
		final EditText setEdit_chpassword = (EditText)findViewById(R.id.set_chpassword);
		saveButton_chpassword = (Button)findViewById(R.id.save_chpassword);
//		setEdit_chpassword.setWidth(10);
//		setEdit_chpassword.setHeight(10);
/*		
		String initial_chpassword="6666";
		SharedPreferences pref=getSharedPreferences("data_chpassword",MODE_PRIVATE);

			SharedPreferences.Editor editor=getSharedPreferences("data_chpassword",MODE_PRIVATE).edit();

			editor.putString("set_chpassword",initial_chpassword);
			editor.commit();
*/	//	}
			String initial_chpassword="6666";
			SharedPreferences pref=getSharedPreferences("data_chpassword",MODE_PRIVATE);
			String saved_password=pref.getString("set_chpassword","");
			while(TextUtils.isEmpty(saved_password)){
				SharedPreferences.Editor editor=getSharedPreferences("data_chpassword",MODE_PRIVATE).edit();

				editor.putString("set_chpassword",initial_chpassword);
				editor.commit();
			}
	
			
			
			
			
			
			
			
		
		saveButton_chpassword.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String inputText=setEdit_chpassword.getText().toString();
				if(!TextUtils.isEmpty(inputText)){
					SharedPreferences.Editor editor=getSharedPreferences("data_chpassword",MODE_PRIVATE).edit();
					editor.putString("set_chpassword",inputText);
					editor.commit();
					Toast.makeText(FirstActivity.this, "密码设置成功！b(￣▽￣)d", Toast.LENGTH_SHORT).show();
				}
				else
					Toast.makeText(FirstActivity.this, "您还未设置密码！ ( ⊙ _ ⊙ )", Toast.LENGTH_SHORT).show();
			}
		});

	
		final Button emergency = (Button) findViewById(R.id.emergency);
		emergency.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
			//	Toast.makeText(FirstActivity.this, "emergency call ...", 
			//			Toast.LENGTH_SHORT).show();
				
				
				
				//voice.call_number();
			}
		});
		
		final Button lockon = (Button) findViewById(R.id.serviceon);
		lockon.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Toast.makeText(FirstActivity.this, "service on...", 
						Toast.LENGTH_SHORT).show();
				switch(v.getId()){
				case R.id.serviceon:
					Intent startIntent = new Intent(FirstActivity.this, Myservice.class);
					startService(startIntent);
					default:
						break;
				}
			}
		});
		
		final Button lockoff = (Button) findViewById(R.id.serviceoff);
		lockoff.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Toast.makeText(FirstActivity.this, "service off...", 
						Toast.LENGTH_SHORT).show();
				switch(v.getId()){
				case R.id.serviceoff:
					Intent startIntent = new Intent(FirstActivity.this, Myservice.class);
					stopService(startIntent);
					default:
						break;
				}
			}
		});
		
		
		
		
		
		final Button voice = (Button) findViewById(R.id.voice);
		voice.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				Intent setvoice = new Intent(FirstActivity.this, voiceregist.class);
				startActivity(setvoice);
			}
		});
		
		
		final ImageView img = (ImageView) findViewById(R.id.imageView1);
        final Button btn = (Button) findViewById(R.id.buttonSafe);
        btn.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				/*Intent intent = new Intent(MainActivity.this, VoiceKey.class);
				startActivity(intent);  */
				lockoff.setVisibility(View.VISIBLE); 			//为什么是final才能调用？？
				voice.setVisibility(View.VISIBLE);
				lockon.setVisibility(View.VISIBLE); 
				emergency.setVisibility(View.VISIBLE); 
				saveButton_chpassword.setVisibility(View.VISIBLE); 
				setEdit_chpassword.setVisibility(View.VISIBLE); 
				btn.setVisibility(View.GONE); 
				img.setVisibility(View.GONE); 
			}
		});
	
		}
	
}
