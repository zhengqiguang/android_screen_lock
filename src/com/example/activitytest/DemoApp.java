package com.example.activitytest;

import com.iflytek.cloud.SpeechUtility;

import android.app.Application;

public class DemoApp extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		// Ӧ�ó�����ڴ�����,�����ֻ��ڴ��С��ɱ����̨����,���SpeechUtility����Ϊnull
		// �����������Ӧ��appid
		SpeechUtility.createUtility(this, "appid=" + "56f0bf3d");
	}
}
