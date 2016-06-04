package com.example.activitytest;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity
{
//	private EditText edit;
//	private String mAuthId = "hahaha233";
	private String mAuthId;// = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
	private SpeakerVerifier mVerifier;
	private EditText input_chpassword;
	private Button judge_chpassword;
	private Button judge_voicepassword;
	private TextView mShowMsgTextView;
	private TextView mShowPwdTextView;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_layout);
		
		mShowMsgTextView = (TextView) findViewById(R.id.showMsg);
		mShowPwdTextView = (TextView) findViewById(R.id.showPwd);
		
		((TextView) findViewById(R.id.showMsg)).setText("");
		// 清空参数
		
		String t = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		mAuthId = "a" + t.substring(t.length()-10);
		
		Toast.makeText(SecondActivity.this, "ID: " + mAuthId, Toast.LENGTH_SHORT).show();
		
		
		mVerifier = SpeakerVerifier.createVerifier(SecondActivity.this, new InitListener() {
			
			@Override
			public void onInit(int errorCode) {
				if (ErrorCode.SUCCESS == errorCode) {
					showTip("引擎初始化成功");
				} else {
					showTip("引擎初始化失败，错误码：" + errorCode);
				}
			}
		});
		
		
		
		
		
		
		
		
		input_chpassword=(EditText)findViewById(R.id.input_chpassword);
		judge_chpassword=(Button)findViewById(R.id.judge_chpassword);
		

		judge_chpassword.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String inputText=input_chpassword.getText().toString(); //获取用户输入的密码存入字符串inputText中
				SharedPreferences pref=getSharedPreferences("data_chpassword",MODE_PRIVATE);
				String saved_password=pref.getString("set_chpassword","");//从数据文件中读取密码保存到字符串saved_password中
			    if(saved_password.equals(inputText))   //输入密码正确
					finish();                           //解开屏幕
				else
					Toast.makeText(SecondActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
			}
		});


		judge_voicepassword=(Button)findViewById(R.id.judge_voice);
	
		judge_voicepassword.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String inputText=input_chpassword.getText().toString(); //获取用户输入的密码存入字符串inputText中
				
				SharedPreferences pref=getSharedPreferences("data_voicepassword",MODE_PRIVATE);
				String saved_password=pref.getString("set_voicepassword","");//从数据文件中读取密码保存到字符串saved_password中
				
				mVerifier.setParameter(SpeechConstant.PARAMS, null);
				mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
						Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/verify.pcm");
				mVerifier = SpeakerVerifier.getVerifier();
				// 设置业务类型为验证
				mVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
				String verifyPwd = mVerifier.generatePassword(8);
				mVerifier.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
				((TextView) findViewById(R.id.showPwd)).setText("请读出："
						+ verifyPwd);
				mVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
				mVerifier.setParameter(SpeechConstant.ISV_PWDT, "3");
				// 开始验证
				mVerifier.startListening(mVerifyListener);
				
				
		//	    if(saved_password.equals(inputText))   //输入密码正确
		//			finish();                           //解开屏幕
		//		else
		//			Toast.makeText(SecondActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	private VerifierListener mVerifyListener = new VerifierListener() {

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
		//	showTip("当前正在说话，音量大小：" + volume);
		//	Log.d(TAG, "返回音频数据："+data.length);
		}

		@Override
		public void onResult(VerifierResult result) {
			
			mShowMsgTextView.setText(result.source);
			
			if (result.ret == 0) {
				// 验证通过
				mShowMsgTextView.setText("验证通过");
				Toast.makeText(SecondActivity.this, "≧◇≦     通过啦~~~", Toast.LENGTH_SHORT).show();
				finish();
			}
			else{
				// 验证不通过
				switch (result.err) {
				case VerifierResult.MSS_ERROR_IVP_GENERAL:
					mShowMsgTextView.setText("内核异常");
					break;
				case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
					mShowMsgTextView.setText("出现截幅");
					break;
				case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
					mShowMsgTextView.setText("太多噪音");
					break;
				case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
					mShowMsgTextView.setText("录音太短");
					break;
				case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
					mShowMsgTextView.setText("验证不通过，您所读的文本不一致");
					break;
				case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
					mShowMsgTextView.setText("音量太低");
					break;
				case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
					mShowMsgTextView.setText("音频长达不到自由说的要求");
					break;
				default:
					mShowMsgTextView.setText("验证不通过");
					Toast.makeText(SecondActivity.this, "╮(╯▽╰)╭      未通过~~~", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
		// 保留方法，暂不用
		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle arg3) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}

		@Override
		public void onError(SpeechError error) {
		
			
			switch (error.getErrorCode()) {
			case ErrorCode.MSP_ERROR_NOT_FOUND:
				mShowMsgTextView.setText("模型不存在，请先注册");
				break;

			default:
				showTip("onError Code："	+ error.getPlainDescription(true));
				break;
			}
		}

		@Override
		public void onEndOfSpeech() {
			// 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
		//	showTip("结束说话");
		}

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			showTip("开始说话");
		}
	};
	
	
	private void showTip(final String str) {
		Toast.makeText(this, str, 
				Toast.LENGTH_SHORT).show();
//		mToast.setText(str);
//		mToast.show();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
			}
		return super.onKeyDown(keyCode, event);
    }
	
}
