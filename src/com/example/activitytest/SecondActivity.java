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
		// ��ղ���
		
		String t = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		mAuthId = "a" + t.substring(t.length()-10);
		
		Toast.makeText(SecondActivity.this, "ID: " + mAuthId, Toast.LENGTH_SHORT).show();
		
		
		mVerifier = SpeakerVerifier.createVerifier(SecondActivity.this, new InitListener() {
			
			@Override
			public void onInit(int errorCode) {
				if (ErrorCode.SUCCESS == errorCode) {
					showTip("�����ʼ���ɹ�");
				} else {
					showTip("�����ʼ��ʧ�ܣ������룺" + errorCode);
				}
			}
		});
		
		
		
		
		
		
		
		
		input_chpassword=(EditText)findViewById(R.id.input_chpassword);
		judge_chpassword=(Button)findViewById(R.id.judge_chpassword);
		

		judge_chpassword.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String inputText=input_chpassword.getText().toString(); //��ȡ�û��������������ַ���inputText��
				SharedPreferences pref=getSharedPreferences("data_chpassword",MODE_PRIVATE);
				String saved_password=pref.getString("set_chpassword","");//�������ļ��ж�ȡ���뱣�浽�ַ���saved_password��
			    if(saved_password.equals(inputText))   //����������ȷ
					finish();                           //�⿪��Ļ
				else
					Toast.makeText(SecondActivity.this, "�������", Toast.LENGTH_SHORT).show();
			}
		});


		judge_voicepassword=(Button)findViewById(R.id.judge_voice);
	
		judge_voicepassword.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String inputText=input_chpassword.getText().toString(); //��ȡ�û��������������ַ���inputText��
				
				SharedPreferences pref=getSharedPreferences("data_voicepassword",MODE_PRIVATE);
				String saved_password=pref.getString("set_voicepassword","");//�������ļ��ж�ȡ���뱣�浽�ַ���saved_password��
				
				mVerifier.setParameter(SpeechConstant.PARAMS, null);
				mVerifier.setParameter(SpeechConstant.ISV_AUDIO_PATH,
						Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/verify.pcm");
				mVerifier = SpeakerVerifier.getVerifier();
				// ����ҵ������Ϊ��֤
				mVerifier.setParameter(SpeechConstant.ISV_SST, "verify");
				String verifyPwd = mVerifier.generatePassword(8);
				mVerifier.setParameter(SpeechConstant.ISV_PWD, verifyPwd);
				((TextView) findViewById(R.id.showPwd)).setText("�������"
						+ verifyPwd);
				mVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
				mVerifier.setParameter(SpeechConstant.ISV_PWDT, "3");
				// ��ʼ��֤
				mVerifier.startListening(mVerifyListener);
				
				
		//	    if(saved_password.equals(inputText))   //����������ȷ
		//			finish();                           //�⿪��Ļ
		//		else
		//			Toast.makeText(SecondActivity.this, "�������", Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	private VerifierListener mVerifyListener = new VerifierListener() {

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
		//	showTip("��ǰ����˵����������С��" + volume);
		//	Log.d(TAG, "������Ƶ���ݣ�"+data.length);
		}

		@Override
		public void onResult(VerifierResult result) {
			
			mShowMsgTextView.setText(result.source);
			
			if (result.ret == 0) {
				// ��֤ͨ��
				mShowMsgTextView.setText("��֤ͨ��");
				Toast.makeText(SecondActivity.this, "�R��Q     ͨ����~~~", Toast.LENGTH_SHORT).show();
				finish();
			}
			else{
				// ��֤��ͨ��
				switch (result.err) {
				case VerifierResult.MSS_ERROR_IVP_GENERAL:
					mShowMsgTextView.setText("�ں��쳣");
					break;
				case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
					mShowMsgTextView.setText("���ֽط�");
					break;
				case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
					mShowMsgTextView.setText("̫������");
					break;
				case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
					mShowMsgTextView.setText("¼��̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
					mShowMsgTextView.setText("��֤��ͨ�������������ı���һ��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
					mShowMsgTextView.setText("����̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
					mShowMsgTextView.setText("��Ƶ���ﲻ������˵��Ҫ��");
					break;
				default:
					mShowMsgTextView.setText("��֤��ͨ��");
					Toast.makeText(SecondActivity.this, "�r(�s���t)�q      δͨ��~~~", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
		// �����������ݲ���
		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle arg3) {
			// ���´������ڻ�ȡ���ƶ˵ĻỰid����ҵ�����ʱ���Ựid�ṩ������֧����Ա�������ڲ�ѯ�Ự��־����λ����ԭ��
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}

		@Override
		public void onError(SpeechError error) {
		
			
			switch (error.getErrorCode()) {
			case ErrorCode.MSP_ERROR_NOT_FOUND:
				mShowMsgTextView.setText("ģ�Ͳ����ڣ�����ע��");
				break;

			default:
				showTip("onError Code��"	+ error.getPlainDescription(true));
				break;
			}
		}

		@Override
		public void onEndOfSpeech() {
			// �˻ص���ʾ����⵽��������β�˵㣬�Ѿ�����ʶ����̣����ٽ�����������
		//	showTip("����˵��");
		}

		@Override
		public void onBeginOfSpeech() {
			// �˻ص���ʾ��sdk�ڲ�¼�����Ѿ�׼�����ˣ��û����Կ�ʼ��������
			showTip("��ʼ˵��");
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
