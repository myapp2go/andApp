package com.myapp2go.voicemail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnInitListener {

	public static int increment = 10;
	private static int msgCount = 0;
	private static final int VOICE_RECOGNITION = 1234;
	private static int ttsCount = 0;
	private static String[] mailMessages;
	private static int msgLength;
	public TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tts = new TextToSpeech(this, this);
		tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

			@Override
			public synchronized void onDone(String utteranceId) {
				ttsCount++;
				if (ttsCount == increment) {
					startRecognizer();
					ttsCount = 0;
				}
			}

			@Override
			public void onStart(String utteranceId) {
			}

			@Override
			@Deprecated
			public void onError(String utteranceId) {
			}
		});
       
		PackageManager pm = getPackageManager();
		List<ResolveInfo> listResolveInfo = pm.queryIntentActivities(
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (listResolveInfo.size() == 0) {
			System.out.println("Recognize Error");
//			startRecognizer();
		}		
		final Button sendMail = (Button) this.findViewById(R.id.readMail);
		sendMail.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String myEmail = ((TextView) findViewById(R.id.myEmail)).getText().toString();
				String myPassword = ((TextView) findViewById(R.id.myPassword)).getText().toString();
				new ReadMailTask(MainActivity.this).execute(msgCount, tts, myEmail, myPassword);
			}
		});
	}

	public void startRecognizer() {	
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  
	    intent.putExtra(
	    	RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
	        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); 
	    startActivityForResult(intent, VOICE_RECOGNITION); 
	    System.out.print("^^^^^^^^^^^^^^^^^startRecognizer*********** " );
	}

	public void setMessages(Message[] messages) {
		mailMessages = new String[messages.length];
		for (int i = 0; i < messages.length; i++) {
			try {
				mailMessages[i] = messages[i].getSubject();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.print("^^^^^^^^^^^^^^^^^setMessages*********** " + mailMessages + " " + messages);
	}

	public void initRecognizer() {
		int msgLength = mailMessages.length;

			readMessage(0, msgLength);

	}

	private void readMessage(int count, int msgLength) {
		System.out.print("^^^^^^^^^^^^^^^^^readMessage*********** ");
		
		for (int i = count; (i < count+increment); i++) {
	
//			Message message = mailMessages[i];
			/*
			System.out.println("----------------------------------");
			System.out.println("Email Number " + (i + 1));
			System.out.println("Subject: " + message.getSubject());
			System.out.println("From: " + message.getFrom()[0]);
			System.out.println("Text: " + message.getContent().toString());
			 */
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
    
//			tts.speak("mail number :" + (i + 1) + message.getSubject(), TextToSpeech.QUEUE_ADD, null);
			tts.speak("mail number :" + (i + 1) + mailMessages[i], TextToSpeech.QUEUE_ADD, map);
/*			
			Object msgContent = message.getContent();
			String content = "";
	
			if (msgContent instanceof Multipart) {
				Multipart multipart = (Multipart) msgContent;
				System.out.println("BodyPart" + "MultiPartCount: " + multipart.getCount());

				for (int j = 0; j < multipart.getCount(); j++) {
					BodyPart bodyPart = multipart.getBodyPart(j);

					String disposition = bodyPart.getDisposition();

					if (j == 0) {
					if (disposition != null
						&& (disposition.equalsIgnoreCase("ATTACHMENT"))) {
						System.out.println("Mail have some attachment");

						DataHandler handler = bodyPart.getDataHandler();
						System.out.println("file name : " + handler.getName());
					} else {
						content = bodyPart.getContent().toString();
						System.out.println("getText " + content);
					}
				}
			}
		}
		*/
		}
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
	
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
    	super.onActivityResult(requestCode, resultCode, data); 
    	System.out.print("onActivityResult*********** " );
        if (requestCode == VOICE_RECOGNITION && resultCode == RESULT_OK)
        {  
            ArrayList<String> matches = data.getStringArrayListExtra
            		(RecognizerIntent.EXTRA_RESULTS); 
            /*
            System.out.print("************ " );
            for (int i = 0; i < matches.size(); i++) {
            System.out.print(" " + matches.get(i));
            }
            System.out.println("************ " );
            */
//            if ("stop".equals(matches.get(0))) {
            	msgCount += increment;
            	readMessage(msgCount, msgLength);
            	
//				startRecognizer();
            	//            	rmTask.cancel(true);
            	
//            	for (int j = 0; j < 10; j++)
//            	if (!rmTask.isCancelled()) rmTask.cancel(true);
//            	System.out.println("******************************Cancel: " + rmTask.isCancelled());
//            }
            
//            SystemClock.sleep(7000);
//            createRecognizer();
            System.out.println("******************************Restart: ");
        }
    }
    
}
