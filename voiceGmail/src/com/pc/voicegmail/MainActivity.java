package com.pc.voicegmail;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnInitListener {

	public static int increment = 10;
	private static int msgCount = 0;
	
	public TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tts = new TextToSpeech(this,  this);
		
		final Button sendMail = (Button) this.findViewById(R.id.readMail);
		sendMail.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String myEmail = ((TextView) findViewById(R.id.myEmail)).getText().toString();
				String myPassword = ((TextView) findViewById(R.id.myPassword)).getText().toString();
				new ReadMailTask(MainActivity.this).execute(msgCount, tts, myEmail, myPassword);
			}
		});
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

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		
	}
}
