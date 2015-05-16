package com.pc.androidapp;

import java.util.Locale;

import com.google.tts.TextToSpeechBetaExt;
import com.google.tts.TextToSpeechBetaExt.OnInitListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog.Builder;

public class NiHaoTTS extends Activity  {
//	public class NiHaoTTS extends Activity implements OnInitListener {

    /** Called when the activity is first created. */
	private Button mBtn;
	private EditText mText;
	//Ã¤Â½Â¿Ã§â€�Â¨com.google.ttsÃ¥Å’â€¦Ã¤Â¸Â­Ã§Å¡â€žTextToSpeechBeta
	private TextToSpeechBetaExt mTTS;
	
	private static final String TAG = "TTS Demo";  
	private static final int REQ_TTS_STATUS_CHECK = 0;  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nihao_main);
        
        //Ã¦Â£â‚¬Ã¦Å¸Â¥TTSÃ¦â€¢Â°Ã¦ï¿½Â®Ã¦ËœÂ¯Ã¥ï¿½Â¦Ã¥Â·Â²Ã§Â»ï¿½Ã¥Â®â€°Ã¨Â£â€¦Ã¥Â¹Â¶Ã¤Â¸â€�Ã¥ï¿½Â¯Ã§â€�Â¨
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeechBetaExt.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
        
        mText = (EditText)findViewById(R.id.ttstext);
        mBtn = (Button) findViewById(R.id.ttsbtn);
        mBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ttsText = mText.getText().toString();
				if(ttsText != "")
				{
					//Ã¨Â¯Â»Ã¥ï¿½â€“Ã¦â€“â€¡Ã¦Å“Â¬Ã¦Â¡â€ Ã¤Â¸Â­Ã§Å¡â€žÃ¤Â¸Â­Ã¦â€“â€¡
					ttsText = "你好，我能说中文";
					ttsText = "Hello Paul";
					mTTS.speak(ttsText, TextToSpeechBetaExt.QUEUE_ADD, null);
				}
			}
		});
    }
    
  //Ã¥Â®Å¾Ã§Å½Â°TTSÃ¥Ë†ï¿½Ã¥Â§â€¹Ã¥Å’â€“Ã¦Å½Â¥Ã¥ï¿½Â£  
//	@Override
	public void onInit(int status, int version) {
		// TODO Auto-generated method stub
		Log.v(TAG, "version = " + String.valueOf(version));
		//Ã¥Ë†Â¤Ã¦â€“Â­TTSÃ¥Ë†ï¿½Ã¥Â§â€¹Ã¥Å’â€“Ã§Å¡â€žÃ¨Â¿â€�Ã¥â€ºÅ¾Ã§â€°Ë†Ã¦Å“Â¬Ã¥ï¿½Â·Ã¯Â¼Å’Ã¥Â¦â€šÃ¦Å¾Å“Ã¤Â¸Âº-1Ã¯Â¼Å’Ã¨Â¡Â¨Ã§Â¤ÂºÃ¦Â²Â¡Ã¦Å“â€°Ã¥Â®â€°Ã¨Â£â€¦Ã¥Â¯Â¹Ã¥Âºâ€�Ã§Å¡â€žTTSÃ¦â€¢Â°Ã¦ï¿½Â®
		if(version == -1)
		{
			//Ã¦ï¿½ï¿½Ã§Â¤ÂºÃ¥Â®â€°Ã¨Â£â€¦Ã¦â€°â‚¬Ã©Å“â‚¬Ã§Å¡â€žTTSÃ¦â€¢Â°Ã¦ï¿½Â®
			alertInstallEyesFreeTTSData();
		}
		else
		{
			//TTS EngineÃ¥Ë†ï¿½Ã¥Â§â€¹Ã¥Å’â€“Ã¥Â®Å’Ã¦Ë†ï¿½
			if(status == TextToSpeechBetaExt.SUCCESS)
			{
				Log.v(TAG, "success to init tts");
				//Ã¨Â®Â¾Ã§Â½Â®TTSÃ¥Â¼â€¢Ã¦â€œÅ½Ã¯Â¼Å’com.google.ttsÃ¥ï¿½Â³eSpeakÃ¦â€�Â¯Ã¦Å’ï¿½Ã§Å¡â€žÃ¨Â¯Â­Ã¨Â¨â‚¬Ã¥Å’â€¦Ã¥ï¿½Â«Ã¤Â¸Â­Ã¦â€“â€¡Ã¯Â¼Å’Ã¤Â½Â¿Ã§â€�Â¨AndroidÃ§Â³Â»Ã§Â»Å¸Ã©Â»ËœÃ¨Â®Â¤Ã§Å¡â€žpicoÃ¥ï¿½Â¯Ã¤Â»Â¥Ã¨Â®Â¾Ã§Â½Â®Ã¤Â¸Âºcom.svox.pico
// PC522				mTTS.setEngineByPackageNameExtended("com.google.tts");
				mTTS.setEngineByPackageName("com.google.tts");
				int result = mTTS.setLanguage(Locale.CHINA);
				//Ã¨Â®Â¾Ã§Â½Â®Ã¥ï¿½â€˜Ã©Å¸Â³Ã¨Â¯Â­Ã¨Â¨â‚¬
				if(result == TextToSpeechBetaExt.LANG_MISSING_DATA || result == TextToSpeechBetaExt.LANG_NOT_SUPPORTED)
				//Ã¥Ë†Â¤Ã¦â€“Â­Ã¨Â¯Â­Ã¨Â¨â‚¬Ã¦ËœÂ¯Ã¥ï¿½Â¦Ã¥ï¿½Â¯Ã§â€�Â¨
				{
					Log.v(TAG, "Language is not available");
					mBtn.setEnabled(false);
				}
				else
				{
					mTTS.speak("Ã¤Â½Â Ã¥Â¥Â½,Ã¦Å“â€¹Ã¥ï¿½â€¹!", TextToSpeechBetaExt.QUEUE_ADD, null);
					mBtn.setEnabled(true);
				}
			}
			else
			{
				Log.v(TAG, "failed to init tts");
			}
		}
	}
	
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*
		if(requestCode == REQ_TTS_STATUS_CHECK)
		{
			switch (resultCode) {
			case TextToSpeechBetaExt.Engine.CHECK_VOICE_DATA_PASS:
				//Ã¨Â¿â„¢Ã¤Â¸ÂªÃ¨Â¿â€�Ã¥â€ºÅ¾Ã§Â»â€œÃ¦Å¾Å“Ã¨Â¡Â¨Ã¦ËœÅ½TTS EngineÃ¥ï¿½Â¯Ã¤Â»Â¥Ã§â€�Â¨
			{
				//Ã¤Â½Â¿Ã§â€�Â¨Ã§Å¡â€žÃ¦ËœÂ¯TextToSpeechBetaExt
		        mTTS = new TextToSpeechBetaExt(this, this);
//		        mTTS = new TextToSpeech(this, this);

		        Log.v(TAG, "TTS Engine is installed!");  
				
			}
				
				break;
			case TextToSpeechBetaExt.Engine.CHECK_VOICE_DATA_BAD_DATA:
				//Ã©Å“â‚¬Ã¨Â¦ï¿½Ã§Å¡â€žÃ¨Â¯Â­Ã©Å¸Â³Ã¦â€¢Â°Ã¦ï¿½Â®Ã¥Â·Â²Ã¦ï¿½Å¸Ã¥ï¿½ï¿½
			case TextToSpeechBetaExt.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				//Ã§Â¼ÂºÃ¥Â°â€˜Ã©Å“â‚¬Ã¨Â¦ï¿½Ã¨Â¯Â­Ã¨Â¨â‚¬Ã§Å¡â€žÃ¨Â¯Â­Ã©Å¸Â³Ã¦â€¢Â°Ã¦ï¿½Â®
			case TextToSpeechBetaExt.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
				//Ã§Â¼ÂºÃ¥Â°â€˜Ã©Å“â‚¬Ã¨Â¦ï¿½Ã¨Â¯Â­Ã¨Â¨â‚¬Ã§Å¡â€žÃ¥ï¿½â€˜Ã©Å¸Â³Ã¦â€¢Â°Ã¦ï¿½Â®
			{
				//Ã¨Â¿â„¢Ã¤Â¸â€°Ã§Â§ï¿½Ã¦Æ’â€¦Ã¥â€ ÂµÃ©Æ’Â½Ã¨Â¡Â¨Ã¦ËœÅ½Ã¦â€¢Â°Ã¦ï¿½Â®Ã¦Å“â€°Ã©â€�â„¢,Ã©â€¡ï¿½Ã¦â€“Â°Ã¤Â¸â€¹Ã¨Â½Â½Ã¥Â®â€°Ã¨Â£â€¦Ã©Å“â‚¬Ã¨Â¦ï¿½Ã§Å¡â€žÃ¦â€¢Â°Ã¦ï¿½Â®
				Log.v(TAG, "Need language stuff:"+resultCode);
				Intent dataIntent = new Intent();
				dataIntent.setAction(TextToSpeechBetaExt.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(dataIntent);
			}
				break;
			case TextToSpeechBetaExt.Engine.CHECK_VOICE_DATA_FAIL:
				//Ã¦Â£â‚¬Ã¦Å¸Â¥Ã¥Â¤Â±Ã¨Â´Â¥
			default:
				Log.v(TAG, "Got a failure. TTS apparently not available");
				break;
			}
		}
		else
		{
			//Ã¥â€¦Â¶Ã¤Â»â€“IntentÃ¨Â¿â€�Ã¥â€ºÅ¾Ã§Å¡â€žÃ§Â»â€œÃ¦Å¾Å“
		}
		*/
	}
	//Ã¥Â¼Â¹Ã¥â€¡ÂºÃ¥Â¯Â¹Ã¨Â¯ï¿½Ã¦Â¡â€ Ã¦ï¿½ï¿½Ã§Â¤ÂºÃ¥Â®â€°Ã¨Â£â€¦Ã¦â€°â‚¬Ã©Å“â‚¬Ã§Å¡â€žTTSÃ¦â€¢Â°Ã¦ï¿½Â®
	private void alertInstallEyesFreeTTSData()
	{
		Builder alertInstall = new AlertDialog.Builder(this)
			.setTitle("Ã§Â¼ÂºÃ¥Â°â€˜Ã©Å“â‚¬Ã¨Â¦ï¿½Ã§Å¡â€žÃ¨Â¯Â­Ã©Å¸Â³Ã¥Å’â€¦")
			.setMessage("Ã¤Â¸â€¹Ã¨Â½Â½Ã¥Â®â€°Ã¨Â£â€¦Ã§Â¼ÂºÃ¥Â°â€˜Ã§Å¡â€žÃ¨Â¯Â­Ã©Å¸Â³Ã¥Å’â€¦")
			.setPositiveButton("Ã§Â¡Â®Ã¥Â®Å¡", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//Ã¤Â¸â€¹Ã¨Â½Â½eyes-freeÃ§Å¡â€žÃ¨Â¯Â­Ã©Å¸Â³Ã¦â€¢Â°Ã¦ï¿½Â®Ã¥Å’â€¦
					String ttsDataUrl = "http://eyes-free.googlecode.com/files/tts_3.1_market.apk";
					Uri ttsDataUri = Uri.parse(ttsDataUrl);
					Intent ttsIntent = new Intent(Intent.ACTION_VIEW, ttsDataUri);
					startActivity(ttsIntent);
				}
			})
			.setNegativeButton("Ã¥ï¿½â€“Ã¦Â¶Ë†", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			alertInstall.create().show();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mTTS!=null){
			mTTS.shutdown();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mTTS != null)
		{
			mTTS.stop();
		}
	}
//	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
	
}
