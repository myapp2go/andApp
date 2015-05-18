package com.pc.androidapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void startChart(View view) {
	    // Do something in response to button
		Intent taxIntent = new Intent(this, TaxActivity.class);
		/*
		EditText editText = (EditText) findViewById(R.id.agi1);
		String agi1 = editText.getText().toString();
//		taxIntent.putExtra(EXTRA_MESSAGE, message);
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
		Editor editor = pref.edit();
		editor.putString("agi1", agi1);
		editor.commit();
		*/
		
		ShareTool.sharePut(findViewById(R.id.agi1), "agi1");
		ShareTool.sharePut(findViewById(R.id.url), "url");
		
		startActivity(taxIntent);
	}

	private void share(int id, String name) {
		EditText editText = (EditText) findViewById(id);
		String text = editText.getText().toString();
//		taxIntent.putExtra(EXTRA_MESSAGE, message);
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
		Editor editor = pref.edit();
		editor.putString(name, text);
		editor.commit();
	}
	
	public void startTTS(View view) {
	    // Do something in response to button
		Intent ttsIntent = new Intent(this, PcTTS.class);
//		EditText editText = (EditText) findViewById(R.id.agi1);
//		String agi1 = editText.getText().toString();
//		taxIntent.putExtra(EXTRA_MESSAGE, message);
		
//		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
//		Editor editor = pref.edit();
//		editor.putString("agi1", agi1);
//		editor.commit();
		
		startActivity(ttsIntent);
	}

	public void startTTSExt(View view) {
	    // Do something in response to button
		Intent ttsIntent = new Intent(this, NiHaoTTS.class);
		
		startActivity(ttsIntent);
	}

	public void startJSoup(View view) {
		ShareTool.sharePut(findViewById(R.id.url), "url");
		
	    // Do something in response to button
		Intent ttsIntent = new Intent(this, JSoupTTSActivity.class);
		
		startActivity(ttsIntent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
