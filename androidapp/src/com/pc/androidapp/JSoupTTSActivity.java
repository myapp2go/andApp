package com.pc.androidapp;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class JSoupTTSActivity extends JSoupMainActivity implements OnInitListener {

	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech tts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.jsoup_main);

		Button descbutton = (Button) findViewById(R.id.descbutton);
		// Capture button click
		descbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Execute Description AsyncTask
				new doTTS().execute();
			}
		});
		
        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
	}

	// Description AsyncTask
	private class doTTS extends AsyncTask<Void, Void, Void> {
		String desc = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(JSoupTTSActivity.this);
			mProgressDialog.setTitle("Android Basic JSoup Tutorial");
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// Connect to the web site
				url = ShareTool.shareGet(getApplicationContext(), "url");

				Document document = Jsoup.connect(url).get();
				// Using Elements to get the Meta data
//				Elements description = document.select("p");
				Elements description = document.getAllElements();

				// Locate the content attribute
				int i = 0;
				for (Element p : description) {
					// desc.append(p.text());
					desc += p.text();
					i++;
					if (i > 0) break;
				}
				tts.speak(desc, TextToSpeech.QUEUE_ADD, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Set description into TextView
			TextView txtdesc = (TextView) findViewById(R.id.desctxt);
			txtdesc.setText(desc);
			mProgressDialog.dismiss();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// 使用新的構造方法，TTS的引擎的包名如果為null，則用的是系統引擎
				tts = new TextToSpeech(this, this);// 初始化TextToSpeech對象
				try {
//					tts.setEngineByPackageName("com.svox.pico");// 指定使用的TTS引擎
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Intent installIntent = new Intent();// 初始化Intent對象
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);// 設置Intent中的action
				startActivity(installIntent); // 發送intent
			}
		}
	}

	public void onInit(int status)// 重寫OnInitListener中的方法
	{
		if (status == TextToSpeech.SUCCESS)// TextToSpeech引擎初始化成功
		{
			Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();// 顯示Toast
			// 利用getEngines()拿到引擎列表，然後打印相應的信息
			List<EngineInfo> ll = tts.getEngines();
			for (int i = 0; i < ll.size(); i++) {
//				Log.v(tag, ll.get(i).toString());// 打印引擎信息
			}
		} else if (status == TextToSpeech.ERROR)// TextToSpeech引擎初始化失敗
		{
			Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();// 顯示Toast
		}
	}
}
