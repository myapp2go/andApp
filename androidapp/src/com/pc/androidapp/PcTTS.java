package com.pc.androidapp;

import android.app.Activity;
import android.os.Bundle;

import com.google.tts.TTS;

public class PcTTS extends Activity {

	private TTS myTts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myTts = new TTS(this, ttsInitListener, true);
//		addListenerOnButton();
	}
	
	private TTS.InitListener ttsInitListener = new TTS.InitListener() {
		public void onInit(int version) {
			myTts.speak("Hello world", 0, null);
		}
	};
	      
}
