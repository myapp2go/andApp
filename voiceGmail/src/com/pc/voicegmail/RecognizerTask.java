package com.pc.voicegmail;

import android.content.Intent;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

public class RecognizerTask extends AsyncTask {

	public RecognizerTask() {
		
	}
	
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		SpeechRecognizer sr = (SpeechRecognizer)params[0];
		// TODO Auto-generated method stub
//		  sr.setRecognitionListener(this);
		  Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//		  intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
		  sr.startListening(intent);
		  
		return null;
	}

	@Override
	public void onProgressUpdate(Object... values) {
		
	}
	
	@Override
	public void onPostExecute(Object result) {
		super.onPostExecute(result);
	}
}
