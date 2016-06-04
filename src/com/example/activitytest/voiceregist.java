package com.example.activitytest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeakerVerifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VerifierListener;
import com.iflytek.cloud.VerifierResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class voiceregist extends Activity implements OnClickListener 
{
	private String mAuthId = "hahaha233";
//	private String mAuthId = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
	private SpeakerVerifier mVerifier;
	private TextView mShowPwdTextView;
	private TextView mShowMsgTextView;
	private Button regist;
	private Toast mToast;
	private String mNumPwd; //= "03285469";
	private String[] mNumPwdSegs; //= {"03285469","09734658","53894276","57392804","68294073"};
	private EditText mResultEditText;
	private TextView mShowRegFbkTextView;
	private String id;
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.voiceregist);
		
		mShowPwdTextView = (TextView) findViewById(R.id.showPwd);
		mShowMsgTextView = (TextView) findViewById(R.id.showMsg);
		regist = (Button) findViewById(R.id.regist);
		mResultEditText = (EditText) findViewById(R.id.edt_result);
		mShowRegFbkTextView = (TextView) findViewById(R.id.showRegFbk);
		
		findViewById(R.id.regist).setOnClickListener(voiceregist.this);
		findViewById(R.id.isv_getpassword).setOnClickListener(voiceregist.this);
		findViewById(R.id.delete_voice).setOnClickListener(voiceregist.this);
		
		
		mVerifier = SpeakerVerifier.createVerifier(voiceregist.this, new InitListener() {
			
			@Override
			public void onInit(int errorCode) {
				if (ErrorCode.SUCCESS == errorCode) {
					showTip("�����ʼ���ɹ�");
				} else {
					showTip("�����ʼ��ʧ�ܣ������룺" + errorCode);
				}
			}
		});
		
		SpeechUtility.createUtility(this, "appid=" + "56f0bf3d");
		
	}
	
	private void showTip(final String str) {
		Toast.makeText(this, str, 
				Toast.LENGTH_SHORT).show();
//		mToast.setText(str);
//		mToast.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.isv_getpassword:
			// ��ȡ����֮ǰ����ֹ֮ǰ��ע�����֤����
			mVerifier.cancel();
			initTextView();
	//		setRadioClickable(false);
			// ��ղ���
			showTip("get password");
			mVerifier.setParameter(SpeechConstant.PARAMS, null);
			mVerifier.setParameter(SpeechConstant.ISV_PWDT, "3");
			mVerifier.getPasswordList(mPwdListenter);
			showTip("get password done");
			break;
		case R.id.regist:
			mVerifier.setParameter(SpeechConstant.ISV_PWDT, "3");
			if (TextUtils.isEmpty(mNumPwd)) {
				showTip("���ȡ�������в���");
				return;
			}
			
			mVerifier.setParameter(SpeechConstant.ISV_PWD, mNumPwd);
			((TextView) findViewById(R.id.showPwd)).setText("�������"
					+ mNumPwd.substring(0, 8));
			mShowMsgTextView.setText("ѵ�� ��" + 1 + "�飬ʣ��4��");
	//		setRadioClickable(false);
			// ����auth_id����������Ϊ��
			mVerifier.setParameter(SpeechConstant.AUTH_ID, mAuthId);
			// ����ҵ������Ϊע��
			mVerifier.setParameter(SpeechConstant.ISV_SST, "train");
			// ����������������
			mVerifier.setParameter(SpeechConstant.ISV_PWDT, "3");
			// ��ʼע��
			mVerifier.startListening(mRegisterListener);
			break;
		case R.id.delete_voice:
			performModelOperation("del", mModelOperationListener);
			break;
		}
	}
	
	private SpeechListener mPwdListenter = new SpeechListener() {
		@Override
		public void onEvent(int eventType, Bundle params) {
		}
		@Override
		public void onBufferReceived(byte[] buffer) {
	//		setRadioClickable(true);
			String result = new String(buffer);
			StringBuffer numberString = new StringBuffer();
			try {
				JSONObject object = new JSONObject(result);
				if (!object.has("num_pwd")) {
					initTextView();
					return;
				}
				
				JSONArray pwdArray = object.optJSONArray("num_pwd");
				numberString.append(pwdArray.get(0));
				for (int i = 1; i < pwdArray.length(); i++) {
					numberString.append("-" + pwdArray.get(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			mNumPwd = numberString.toString();
			mNumPwdSegs = mNumPwd.split("-");
			mResultEditText.setText("�������룺\n" + mNumPwd);
		}
		@Override
		public void onCompleted(SpeechError error) {
			// TODO Auto-generated method stub
	//		setRadioClickable(true);		
			if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
				showTip("��ȡʧ�ܣ�" + error.getErrorCode());
			}
		}
	};
	
	private VerifierListener mRegisterListener = new VerifierListener() {

		@Override
		public void onBeginOfSpeech() {
			// TODO Auto-generated method stub
			showTip("��ʼ˵��~");
			
		}

		@Override
		public void onEndOfSpeech() {
			// TODO Auto-generated method stub
	//		showTip("����˵��");
			
		}

		@Override
		public void onError(SpeechError error) {
			// TODO Auto-generated method stub
			if (error.getErrorCode() == ErrorCode.MSP_ERROR_ALREADY_EXIST) {
				showTip("ģ���Ѵ��ڣ���������ע�ᣬ����ɾ��");
			} else {
				showTip("onError Code��" + error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onResult(VerifierResult result) {
			// TODO Auto-generated method stub
			((TextView)findViewById(R.id.showMsg)).setText(result.source);
			if (result.ret == ErrorCode.SUCCESS) {
				switch (result.err) {
				case VerifierResult.MSS_ERROR_IVP_GENERAL:
					mShowMsgTextView.setText("�ں��쳣");
					break;
				case VerifierResult.MSS_ERROR_IVP_EXTRA_RGN_SOPPORT:
					mShowRegFbkTextView.setText("ѵ���ﵽ������");
					break;
				case VerifierResult.MSS_ERROR_IVP_TRUNCATED:
					mShowRegFbkTextView.setText("���ֽط�");
					break;
				case VerifierResult.MSS_ERROR_IVP_MUCH_NOISE:
					mShowRegFbkTextView.setText("̫������");
					break;
				case VerifierResult.MSS_ERROR_IVP_UTTER_TOO_SHORT:
					mShowRegFbkTextView.setText("¼��̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TEXT_NOT_MATCH:
					mShowRegFbkTextView.setText("ѵ��ʧ�ܣ����������ı���һ��");
					break;
				case VerifierResult.MSS_ERROR_IVP_TOO_LOW:
					mShowRegFbkTextView.setText("����̫��");
					break;
				case VerifierResult.MSS_ERROR_IVP_NO_ENOUGH_AUDIO:
					mShowMsgTextView.setText("��Ƶ���ﲻ������˵��Ҫ��");
				default:
					mShowRegFbkTextView.setText("");
					break;
				}
				
				if (result.suc == result.rgn) {
		//			setRadioClickable(true);
					mShowMsgTextView.setText("ע��ɹ�");
					
					mResultEditText.setText("����������������ID��\n" + result.vid);
					//һ�¼������ڴ�������ID
					id=result.vid;
					SharedPreferences pref=getSharedPreferences("data_voicepassword",MODE_PRIVATE);
					SharedPreferences.Editor editor=getSharedPreferences("data_voicepassword",MODE_PRIVATE).edit();

					editor.putString("set_voicepassword",id);
					editor.commit();
					
					
				} else {
					int nowTimes = result.suc + 1;
					int leftTimes = result.rgn - nowTimes;
					
					
					mShowPwdTextView.setText("�������" + mNumPwdSegs[nowTimes - 1]);
					
					
					mShowMsgTextView.setText("ѵ�� ��" + nowTimes + "�飬ʣ��" + leftTimes + "��");
				}

			}
			else {
	//			setRadioClickable(true);
				
				mShowMsgTextView.setText("ע��ʧ�ܣ������¿�ʼ��");	
			}
			
		}
			@Override
			public void onVolumeChanged(int volume, byte[] data) {
			//	showTip("��ǰ����˵����������С��" + volume);
			//	Log.d(TAG, "������Ƶ���ݣ�"+data.length);
			}

		
	
		
	};
	
private SpeechListener mModelOperationListener = new SpeechListener() {
		
		@Override
		public void onEvent(int eventType, Bundle params) {
		}
		
		@Override
		public void onBufferReceived(byte[] buffer) {
		//	setRadioClickable(true);
			
			String result = new String(buffer);
			try {
				JSONObject object = new JSONObject(result);
				String cmd = object.getString("cmd");
				int ret = object.getInt("ret");
				
				if ("del".equals(cmd)) {
					if (ret == ErrorCode.SUCCESS) {
						showTip("ɾ���ɹ�");
						mResultEditText.setText("");
					} else if (ret == ErrorCode.MSP_ERROR_FAIL) {
						showTip("ɾ��ʧ�ܣ�ģ�Ͳ�����");
					}
				} else if ("que".equals(cmd)) {
					if (ret == ErrorCode.SUCCESS) {
						showTip("ģ�ʹ���");
					} else if (ret == ErrorCode.MSP_ERROR_FAIL) {
						showTip("ģ�Ͳ�����");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void onCompleted(SpeechError error) {
		//	setRadioClickable(true);
			
			if (null != error && ErrorCode.SUCCESS != error.getErrorCode()) {
				showTip("����ʧ�ܣ�" + error.getPlainDescription(true));
			}
		}
	};
	
	
	
	
	private void initTextView(){
	//	mTextPwd = null;
		mNumPwd = null;
		mResultEditText.setText("");
		mShowPwdTextView.setText("");
		mShowMsgTextView.setText("");
		mShowRegFbkTextView.setText("");
	//	mRecordTimeTextView.setText("");
	}
	private VerifierListener mRegisterListener2 = new VerifierListener() {

		@Override
		public void onBeginOfSpeech() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEndOfSpeech() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onResult(VerifierResult arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onVolumeChanged(int arg0, byte[] arg1) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	private void performModelOperation(String operation, SpeechListener listener) {
		// ��ղ���
		mVerifier.setParameter(SpeechConstant.PARAMS, null);
		mVerifier.setParameter(SpeechConstant.ISV_PWDT, "3");
		
		 

		
	//	setRadioClickable(false);
		// ����auth_id����������Ϊ��
		mVerifier.sendRequest(operation, mAuthId, listener);
	}
}




